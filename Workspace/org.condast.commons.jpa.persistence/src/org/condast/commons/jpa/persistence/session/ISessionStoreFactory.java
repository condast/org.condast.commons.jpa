package org.condast.commons.jpa.persistence.session;

@FunctionalInterface
public interface ISessionStoreFactory<I,D extends Object> {

	public D createSessionStore( I session );
}
