package org.condast.commons.jpa.messaging;

public interface IResponseProvider<D extends Object> {

	public void addResponseListener( IResponseListener<D> listener );

	public void removeResponseListener( IResponseListener<D> listener );

}
