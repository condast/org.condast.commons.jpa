package org.condast.commons.jpa.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.jpa.messaging.IHttpRequest;
import org.condast.commons.jpa.messaging.ResponseEvent;
import org.condast.commons.jpa.messaging.http.AbstractWebClientSessionHandler;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;

public abstract class AbstractRestWidgetController<R extends Enum<R>, D, I extends Object> {

	public enum Parameters{
		@Deprecated ID,
		USER_ID,
		@Deprecated TOKEN,
		SECURITY;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( name());
		}
	}

	private String context;
	private String path;
	private WebClient client;
	private boolean visible;
	private boolean disposed;

	private Control widget;
	private I input;

	private Listener listener = new Listener() {
		private static final long serialVersionUID = 1L;

		@Override
		public void handleEvent(Event event) {
			Control control = (Control) event.widget;
			visible = control.isVisible();
		}
	};

	private AbstractRestWidgetController<R,D,I> controller;

	protected AbstractRestWidgetController(Control widget) {
		this( widget, null, null );
	}

	/**
	 * Create the composite.
	 * @param widget
	 * @param style
	 */
	protected AbstractRestWidgetController(Control widget, String context, String path) {
		this.widget = widget;
		this.controller = this;
		this.context = context;
		this.path = path;
		this.visible = widget.isVisible();
		this.disposed = widget.isDisposed();
		this.client = new WebClient();
		widget.addListener( SWT.Show, listener );
		widget.addListener( SWT.Hide, listener );
		widget.addDisposeListener( new DisposeListener() {
			private static final long serialVersionUID = 1L;

			@Override
			public void widgetDisposed(DisposeEvent event) {
				disposed = true;
			}
		});
	}

	public boolean isReady() {
		return !StringUtils.isEmpty(context) && !StringUtils.isEmpty(path);
	}

	public I getInput() {
		return input;
	}

	public void setInput(I input) {
		this.input = input;
	}

	protected String getContext() {
		return context;
	}

	protected String getRestPath() {
		return path;
	}

	protected WebClient getClient() {
		return client;
	}

	public void setInput( String context, String path ){
		this.context = context;
		this.path = path;
		refresh();
	}

	/**
	 * A clear allows the applying system to prepare the widget prior to a request
	 */
	protected void clear( R request ) {
		client.clear( new ResponseEvent<>( this, null, request, null, null ));
	}

	public void request( R request ) throws IOException {
		request( request, new HashMap<String, String>() );
	}

	public void request( R request, Map<String, String> params ) throws IOException {
		client.setContextPath(context + path );
		client.sendGet( request, params);
	}

	public void post( R request, Map<String, String> params, String post ) throws IOException {
		client.setContextPath(context + path );
		client.sendPost( request, params, post );
	}

	public void put( R request, Map<String, String> params, String post ) throws IOException {
		client.setContextPath(context + path );
		client.sendPut( request, params, post );
	}

	public void delete( R request, Map<String, String> params, String post) throws IOException {
		client.setContextPath(context + path );
		client.sendDelete( request, params );
	}

	/**
	 * Handle a refresh of the system
	 * @param request
	 * @param event
	 */
	protected abstract void onRefresh();

	/**
	 * Refresh the system
	 */
	public void refresh(){
		if(( this.disposed ) || widget.isDisposed() || widget.getDisplay().isDisposed() )
			return;
		widget.getDisplay().asyncExec( new Runnable() {

			@Override
			public void run() {
				visible = widget.isVisible();
				if( !visible )
					return;
				try {
					onRefresh(  );
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Handle the response of the given request
	 * @param request
	 * @param event
	 */
	protected abstract void onResponse( R request, ResponseEvent<R> event);

	/**
	 * Handle a failed response of the given request.
	 * If the return value is false, a standard message is returned
	 * @param request
	 * @param event
	 */
	protected boolean responseFail( R request, IHttpRequest.HttpStatus status, ResponseEvent<R> event) {
		return false;
	}

	/**
	 * convenience method creates a hashmap with user id and token
	 * @param user
	 * @return
	 */
	public Map<String, String> getUserParams( long userId, long security ){
		Map<String, String> params = new HashMap<>();
		params.put(Parameters.USER_ID.toString(), String.valueOf( userId ));
		params.put(Parameters.SECURITY.toString(), String.valueOf( security ));
		return params;
	}

	public void dispose() {
		client.dispose();
	}

	private class WebClient extends AbstractWebClientSessionHandler<R,D>{

		protected WebClient() {
			super(widget.getDisplay());
		}

		@Override
		public void onResponse(R request, ResponseEvent<R> event) {
			try {
				controller.onResponse(request, event);
			}
			catch( Exception ex ) {
				ex.printStackTrace();
			}
		}

		@Override
		protected void onHandleResponseFail( final HttpStatus status, final ResponseEvent<R> event) throws IOException {
			final R request = event.getRequest();
			if( HttpStatus.NOT_MODIFIED.equals(status) || disposed || widget.isDisposed() || widget.getDisplay().isDisposed())
				return;
			widget.getDisplay().asyncExec( new Runnable() {

				@Override
				public void run() {
					if( !responseFail(request, status, event)) {
						responseFailMessage(this.getClass(), status.getStatus(), event);
					}
				}

			});
		}
	}
}