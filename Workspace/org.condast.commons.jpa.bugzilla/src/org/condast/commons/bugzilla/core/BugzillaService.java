package org.condast.commons.bugzilla.core;

import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.logging.LogRecord;
import java.util.logging.Logger;

import org.condast.commons.bugzilla.data.AttachmentData;
import org.condast.commons.bugzilla.data.UpdateData;
import org.condast.commons.jpa.messaging.AbstractHttpRequest;
import org.condast.commons.jpa.messaging.ResponseEvent;
import org.condast.commons.messaging.core.IJsonObject;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class BugzillaService extends AbstractHttpRequest<Object> {

	public enum Ignore{
		RESPONSE_CODE,
		CONTEXT;

		public static boolean isValid( String str ) {
			for( Ignore ign: values() ) {
				if( ign.name().equals(str))
					return true;
			}
			return false;
		}
	}

	private String call;
	private String response;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	public BugzillaService(String context, Enum<?> call, Map<String, String> attributes) {
		this( context, StringStyler.xmlStyleString(call.name()), attributes );
	}

	public BugzillaService(String context, String call, Map<String, String> attributes) {
		super(context);
		this.call = getAttributes(call, attributes);
	}


	public String getResponse() {
		return super.unescapeHtml3( this.response );
	}

	public void getBug(Map<String, String> parameters) throws Exception {
		super.sendGet( super.getContextPath() + call, parameters);
	}

	public void postBug( Map<String, String> parameters, String post ) throws Exception {
		String url = super.getContextPath() + call;
		logger.info("\n\nPOSTING: " + url);
		super.sendPost(url, parameters, post );
	}

	public void postBug( IJsonObject report) throws Exception {
		String url = super.getContextPath() + call;
		super.sendPost(url, null, report.toJson() );
	}

	public void updateBug(Map<String, String> parameters, UpdateData data) throws Exception {
		super.sendPut( super.getContextPath() + call, parameters, data.toJson());
	}

	public void createAttachment( long bugid, LogRecord record, Map<String, String> parameters ) throws Exception {
		String url = super.getContextPath() + call + "/" + bugid + "/";
		AttachmentData data = new AttachmentData(bugid, record );
		super.sendPost(url, parameters, AttachmentData.toJson(data));
	}

	protected String getAttributes( String call, Map<String, String> attributes) {
		String attrs = new String();
		Iterator<Map.Entry<String, String>> iterator = attributes.entrySet().iterator();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			String key = StringStyler.styleToEnum(entry.getKey());
			if( Ignore.isValid(key))
				continue;
			attrs = "&" + entry.getKey() + "=" + entry.getValue();
		}
		if( !StringUtils.isEmpty(attrs ))
			attrs = attrs.replaceFirst("&", "?");
		return call + attrs;
	}

	@Override
	protected String onHandleResponse(  ResponseEvent<Object> event) throws IOException {
		logger.info( event.getResponse());
		this.response = event.getResponse();
        int start = response.indexOf("<pre>");
        if( start < 0 )
        	start = 0;
        int end = response.indexOf("</pre>");
        if( end < 0 )
        	end = response.length();
        response = response.substring(start+5, end).replaceAll("\\s+","");
		return super.unescapeHtml3( response );
	}

}
