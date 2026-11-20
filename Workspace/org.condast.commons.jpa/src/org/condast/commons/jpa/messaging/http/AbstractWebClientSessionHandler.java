package org.condast.commons.jpa.messaging.http;

import java.io.IOException;
import java.util.Map;

import org.condast.commons.jpa.messaging.AbstractHttpRequest;
import org.condast.commons.jpa.messaging.ResponseEvent;
import org.condast.js.commons.session.AbstractSessionHandler;
import org.condast.js.commons.session.SessionEvent;
import org.eclipse.swt.widgets.Display;

public abstract class AbstractWebClientSessionHandler<R extends Enum<R>, D extends Object> extends AbstractHttpRequest<R>{

	private SessionHandler handler;

	protected AbstractWebClientSessionHandler( Display display ) {
		super();
		handler = new SessionHandler(display );
	}

	@Override
	public void setContextPath(String contextPath) {
		super.setContextPath(contextPath);
	}

	/**
	 * A clear allows the applying system to prepare the widget prior to a request
	 */
	public void clear( ResponseEvent<R> data) {
		handler.addData( data );
	}

	@Override
	public void sendGet(R request, Map<String, String> parameters) throws IOException{
		super.sendGet(request, parameters);
	}

	@Override
	public void sendPost(R request, Map<String, String> args, String post) throws IOException{
		super.sendPost(request, args, post);
	}

	@Override
	public void sendPut(R request, Map<String, String> args, String post) throws IOException {
		super.sendPut(request, args, post);
	}

	@Override
	protected String onHandleResponse( ResponseEvent<R> event) throws IOException {
		String str = event.getResponse();
		handler.addData(event);
		return str;
	}

	protected abstract void onResponse( R request, ResponseEvent<R> event);

	public void dispose() {
		handler.dispose();
	}

	private class SessionHandler extends AbstractSessionHandler<ResponseEvent<R>>{

		protected SessionHandler(Display display) {
			super(display);
		}

		@Override
		protected void onHandleSession(SessionEvent<ResponseEvent<R>> sevent) {
			if( sevent == null )
				return;
			if( sevent.getData() instanceof ResponseEvent) {
				ResponseEvent<R> response = sevent.getData();
				if( response.getResponseCode() != HttpStatus.OK.getStatus())
					return;
				onResponse( sevent.getData().getRequest(), sevent.getData());
			}
		}
	}
}
