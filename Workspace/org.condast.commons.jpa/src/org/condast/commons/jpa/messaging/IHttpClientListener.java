package org.condast.commons.jpa.messaging;

public interface IHttpClientListener<R extends Object> {

	public void notifyResponse( ResponseEvent<R> event);
}
