package org.condast.commons.jpa.messaging;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.Reader;
import java.util.EventObject;

import javax.ws.rs.core.Response;

public class ResponseEvent<R extends Object> extends EventObject {
	private static final long serialVersionUID = 1L;

	private String url;

	private R request;
	private String response;

	private int responseCode;

	public ResponseEvent(Object source, String url, R request, Reader reader ) {
		this( source, url, request, printResponse(reader), Response.Status.OK.getStatusCode() );
	}

	public ResponseEvent(Object source, String url, R request, Reader reader, int responseCode ) {
		this( source, url, request, printResponse(reader), responseCode );
	}

	public ResponseEvent(Object source, String url, R request, String response, int responseCode ) {
		super(source);
		this.request = request;
		this.response = response;
		this.url = url;
		this.responseCode = responseCode;
	}

	public String getURL() {
		return url;
	}

	public R getRequest() {
		return request;
	}

	public String getResponse() {
		return response;
	}


	public int getResponseCode() {
		return responseCode;
	}

	public static String printResponse( Reader reader ) {
		if( reader == null )
			return "null";
		BufferedReader in = new BufferedReader(reader);
		String line = null;
		StringBuilder rslt = new StringBuilder();
		try {
			while ((line = in.readLine()) != null) {
			    rslt.append(line);
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return rslt.toString();
	}

}
