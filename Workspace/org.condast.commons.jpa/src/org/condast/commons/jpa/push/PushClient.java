package org.condast.commons.jpa.push;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import org.condast.commons.jpa.messaging.AbstractHttpRequest;
import org.condast.commons.jpa.messaging.ResponseEvent;
import org.condast.commons.messaging.push.IPushNotification;
import org.condast.commons.messaging.push.PushNotification;
import org.condast.commons.strings.StringStyler;

import com.google.gson.Gson;

public class PushClient extends AbstractHttpRequest<PushClient.Requests>{

	public enum Requests{
		PUSH,
		NOTIFY;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( this.name());
		}
	}

	public enum Parameters{
		USER_NAME,
		PASSWORD,
		USER_ID,
		TOKEN;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( this.name());
		}
	}

	public PushClient(String path) {
		super(path);
	}

	@Override
	public void setContextPath(String path) {
		String context = super.getContextPath() + path;
		super.setContextPath(context);
	}

	public void push( long userId, long token) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put( Parameters.USER_ID.toString(), String.valueOf( userId ));
		params.put( Parameters.TOKEN.toString(), String.valueOf( token ));
		sendGet( Requests.PUSH, params );
	}

	public void notify( long userId, long token, IPushNotification notification ) throws Exception {
		Map<String, String> params = new HashMap<>();
		params.put( Parameters.USER_ID.toString(), String.valueOf( userId ));
		params.put( Parameters.TOKEN.toString(), String.valueOf( token ));
		Gson gson = new Gson();
		String data  = gson.toJson(notification, PushNotification.class);
		sendPost( Requests.PUSH, params, data );
	}

	@Override
	protected void sendGet(Requests request, Map<String, String> parameters) throws IOException {
		super.sendGet(request, parameters);
	}

	@Override
	protected String onHandleResponse(ResponseEvent<Requests> event) throws IOException {
		Requests request = event.getRequest();
		switch( request ) {
		case PUSH:
			ScriptEngineManager manager = new ScriptEngineManager();
		    ScriptEngine engine = manager.getEngineByName("javascript");
			try {
				engine.eval( event.getResponse() );
			} catch (ScriptException e) {
				throw new IOException( e );
			}
			break;
		case NOTIFY:
			break;
		default:
			break;
		}
		return null;
	}
	}
