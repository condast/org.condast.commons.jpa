package org.condast.commons.bugzilla.core;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.LogRecord;

import org.condast.commons.bugzilla.api.IBugProduct;
import org.condast.commons.bugzilla.data.BugData;
import org.condast.commons.bugzilla.data.PostBugData;
import org.condast.commons.bugzilla.data.UpdateData;
import org.condast.commons.bugzilla.service.Dispatcher;
import org.condast.commons.log.AbstractLogHandler;
import org.condast.commons.preferences.config.Config;
import org.condast.commons.strings.StringUtils;

import com.google.gson.Gson;

public class BugLogger {

	private String DEFAULT_CONTEXT = "https://bugzillaserver/rest/";//"https://bugzilla.vps66492.public.cloudvps.com/rest";
	private String DEFAULT_API_KEY = "0lW102C1Q12q0D3bvnVImpGs2XhR9e5jF3JXlVQv";

	private enum Fields{
		API_KEY,
		COMPONENT,
		PRODUCT,
		ID_OR_ALIAS,
		ALIAS,
		SUMMARY;

	@Override
	public String toString() {
		return name().toLowerCase();
	}}

	public enum Calls{
		BUG;
	}

	private String context;
	private String apiKey;

	private long bugid;

	private Dispatcher dispatcher = Dispatcher.getInstance();

	@SuppressWarnings("unused")
	private LogHandler handler = new LogHandler( Level.SEVERE );

	public BugLogger() {
		super();
		this.context = DEFAULT_CONTEXT;
		this.apiKey = DEFAULT_API_KEY;
	}

	public BugLogger(String context, String product, String component) {
		this();
		this.context = context;
	}

	public void init() {
		Config config = Config.getInstance(); 
		this.context = config.getBugzillaURL();
		if( StringUtils.isEmpty( config.getBugzillApiKey())) {
			config.setBugzillaApiKey(this.apiKey);
		}
		this.apiKey = config.getBugzillApiKey();
	}

	protected BugData getBug( IBugProduct bugproduct, String alias ) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Fields.API_KEY.toString(), apiKey);
		parameters.put( Fields.PRODUCT.toString(), bugproduct.getProduct() );
		parameters.put( Fields.COMPONENT.toString(), bugproduct.getComponent() );
		parameters.put( Fields.ID_OR_ALIAS.toString(), PostBugData.toCorrectAlias(alias));
		BugzillaService bugcall = new BugzillaService( context, Calls.BUG, parameters );
		BugData bugs = null;
		try {
			bugcall.getBug(parameters);
			bugs = BugData.fromJson( bugcall.getResponseCode(), bugcall.getResponse());
			this.bugid = ( bugs == null )?-1: bugs.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return bugs;
	}

	protected boolean postBug( IBugProduct bugproduct, LogRecord record ) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Fields.API_KEY.toString(), apiKey);
		BugzillaService bugcall = new BugzillaService( context, Calls.BUG, parameters );
		try {
			PostBugData pbd = new PostBugData( bugproduct.getProduct(), bugproduct.getComponent(), record.getMessage(), 1 );
			bugcall.postBug( parameters, PostBugData.toJson(pbd));
			IDData data = fromJson( bugcall.getResponse());
			this.bugid = data.id;
			bugcall.createAttachment(bugid, record, parameters);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return (this.bugid < 0 );
	}

	protected boolean updateBug( String alias, int update ) {
		Map<String, String> parameters = new HashMap<>();
		parameters.put( Fields.API_KEY.toString(), apiKey);
		parameters.put( Fields.ID_OR_ALIAS.toString(), PostBugData.toCorrectAlias( alias ));
		BugzillaService bugcall = new BugzillaService( context, Calls.BUG, parameters );
		try {
			UpdateData data = new UpdateData( update );
			bugcall.updateBug(parameters, data);
			BugData bugs = BugData.fromJson( bugcall );
			this.bugid = ( bugs == null )?-1: bugs.getId();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return (this.bugid < 0 );
	}


	private class LogHandler extends AbstractLogHandler{

		protected LogHandler(Level entryLevel) {
			super(entryLevel);
		}

		@Override
		protected void onPublish( final LogRecord arg0) {
			String msg = arg0.getMessage();
			IBugProduct bugproduct = dispatcher.getProduct(arg0.getLoggerName());
			BugData bugs = getBug(bugproduct, msg);
			if( bugs != null ) {
				int amount = bugs.update();
				updateBug(msg, amount );
			}else {
				postBug( bugproduct, arg0 );

			}
		}

		@Override
		public void close() throws SecurityException {
			// NOTHING
		}
	}

	protected static class IDData{
		long id;

		public IDData(long id) {
			super();
			this.id = id;
		}
	}

	private static IDData fromJson( String data ) {
		if( StringUtils.isEmpty( data ))
			return new IDData(-1);
		Gson gson = new Gson();
		return gson.fromJson(data, IDData.class );
	}
}
