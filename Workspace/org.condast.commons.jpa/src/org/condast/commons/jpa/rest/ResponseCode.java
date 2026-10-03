package org.condast.commons.jpa.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;

import com.google.gson.Gson;

public class ResponseCode {

	public static final String RESPONSE_EMPTY = "[]";

	/**
	 * Create a response message
	 * @param message
	 * @return
	 */
	public static Response createResponse( String call, String message ){
		ResponseBuilder builder = Response.ok( call + ": " + message );

		builder.status(200)
		.header("Access-Control-Allow-Origin", "*")
		.header("Access-Control-Allow-Headers", "origin, content-type, accept, authorization")
		.header("Access-Control-Allow-Credentials", "true")
		.header("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD")
		.header("Access-Control-Max-Age", "1209600");
		return builder.build();
	}

	@SuppressWarnings("unused")
	private class SimpleResponse{
		private String id;
		private int token;
		private String response;

		private SimpleResponse(String id, int token, String response) {
			super();
			this.id = id;
			this.token = token;
			this.response = response;
		}

		private  String toJSon( ){
			Gson gson = new Gson();
			return gson.toJson( this);
		}
	}

}
