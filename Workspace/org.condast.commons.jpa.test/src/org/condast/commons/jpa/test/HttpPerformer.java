package org.condast.commons.jpa.test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Scanner;

import javax.swing.text.html.HTMLDocument;
import javax.swing.text.html.HTMLEditorKit;
import javax.ws.rs.core.Response;

import org.condast.commons.jpa.messaging.AbstractHttpRequest;
import org.condast.commons.jpa.messaging.IHttpClientListener;
import org.condast.commons.jpa.messaging.IHttpRequest;
import org.condast.commons.jpa.messaging.ResponseEvent;
import org.condast.commons.jpa.test.core.ITest;
import org.condast.commons.jpa.test.core.ITestEvent;
import org.condast.commons.jpa.test.performer.AbstractPerformer;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class HttpPerformer<D,R extends Object> extends AbstractPerformer<D,R> {

	public enum Requests{
		ONE;
	}

	public enum Parameters{
		CONTEXT,
		TYPE,
		HTTP,
		URI,
		RESPONSE_CODE;

		public static boolean isValid( String str ) {
			for( Parameters ign: values() ) {
				if( ign.name().equals(str))
					return true;
			}
			return false;
		}

	}

	private IHttpClientListener<Requests> listener = new IHttpClientListener<Requests>(){

		@SuppressWarnings("unchecked")
		@Override
		public void notifyResponse(ResponseEvent<Requests> event) {
			ITestEvent<String,R> testEvent = (ITestEvent<String,R>) getEvent();
			testEvent.setPass( event.getResponseCode() == expectedResponseCode.getStatusCode());
			testEvent.setData(event.getResponse() );
			clients.remove( event.getSource());
		}
	};

	private Collection<WebClient> clients;

	private Class<?> clss;

	private String context;

	private Response.Status expectedResponseCode;

	public HttpPerformer( Class<?> clss, String test_id, ITest<D,R> node) {
		super(node);
		this.clss = clss;
		clients = new ArrayList<>();
	}

	@Override
	public void init( ITestEvent<D,R> event ) {
		super.init( event );
		String resp = getAttribute( Parameters.RESPONSE_CODE);
		this.expectedResponseCode = StringUtils.isEmpty(resp)?Response.Status.OK:
			Response.Status.fromStatusCode(Integer.parseInt(resp));
	}

	protected Map<String,String> getAttributes( Map<String, String> attributes) {
		Iterator<Map.Entry<String, String>> iterator = attributes.entrySet().iterator();
		Map<String, String> results = new HashMap<>();
		while( iterator.hasNext() ) {
			Map.Entry<String, String> entry = iterator.next();
			String key = StringStyler.styleToEnum(entry.getKey());
			if( Parameters.isValid(key))
				continue;
			results.put(entry.getKey(), entry.getValue());
		}
		return results;
	}

	@Override
	protected R onPrepare(ITestEvent<D,R> event, int index ) {
		if( !StringUtils.isEmpty( context ))
			return null;
		context = getAttribute( Parameters.CONTEXT);
		return null;
	}

	@SuppressWarnings({ "unchecked", "deprecation" })
	@Override
	protected void onPerform(ITestEvent<D,R> event, int index) {
		WebClient client = new WebClient( context );
		client.addListener(listener);
		String type = event.getAttribute( ITest.AttributeNames.TYPE);
		String data= this.getDataString( (ITestEvent<String,R>) event );
		IHttpRequest.HTTP http = StringUtils.isEmpty(type)?IHttpRequest.HTTP.GET: IHttpRequest.HTTP.valueOf( StringStyler.styleToEnum( type ));
		String path = client.getContextPath() + StringStyler.xmlStyleString(event.getAttribute( ITest.AttributeNames.ID));
		Map<String, String> parameters = getAttributes( event.getAttributes());
		clients.add(client);
		try {
			switch( http ) {
			case POST:
				client.sendPost(path, parameters, data);
				break;
			case DELETE:
				client.sendDelete(path, parameters);
				break;
			case PUT:
				client.sendPut(path, parameters, data);
				break;
			default:
				client.sendGet(path, parameters);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			ITestEvent<D,R> testEvent = getEvent();
			testEvent.setPass(false);
		}
	}

	@Override
	protected R onComplete(ITestEvent<D,R> event, int index) {
		return null;//event.isPass();
	}

	public String getDataString( ITestEvent<String,R> event) {
		StringBuffer buffer = new StringBuffer();
		String data = event.getAttribute( ITest.AttributeNames.DATA);
		String uri = event.getAttribute( ITest.AttributeNames.URI);
		if(!StringUtils.isEmpty(uri)) {
			Scanner scanner = new Scanner( clss.getResourceAsStream( uri ));
			try {
				while( scanner.hasNextLine() ) {
					buffer.append( scanner.nextLine());
				}
			}
			finally {
				scanner.close();
			}
		}else if( !StringUtils.isEmpty( data ))
			buffer.append(data);
		return buffer.toString();
	}

	/**
	 * Remove HTML from the body
	 * @param str
	 * @return
	 */
	public static String unescapeHtml3( String str ) {
	    try {
	        HTMLDocument doc = new HTMLDocument();
	        new HTMLEditorKit().read( new StringReader( "<html><body>" + str ), doc, 0 );
	        return doc.getText( 1, doc.getLength() );
	    } catch( Exception ex ) {
	        return str;
	    }
	}

	@Override
	protected boolean onPostProcess(ITestEvent<D, R> event) {
		return true;
	}
}

class WebClient extends AbstractHttpRequest<HttpPerformer.Requests>{

	public WebClient( String context) {
		super(context);
	}

	@Override
	public String getContextPath() {
		return super.getContextPath();
	}


	@Override
	protected String onHandleResponse( ResponseEvent<HttpPerformer.Requests> response) throws IOException {
		return response.getResponse();
	}
}