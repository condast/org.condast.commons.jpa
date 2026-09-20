package org.condast.commons.persistence.session;

@FunctionalInterface
public interface ISessionStoreFactory<I,D extends Object> {

	public D createSessionStore( I session );
}
