package org.condast.commons.jpa.messaging;

public interface IResponseListener<D extends Object> {

	public void notifyResponseEvent( ResponseEvent<D> event );
}
