package org.condast.commons.persistence.core;

import javax.servlet.http.HttpSession;

public interface ISessionStoreFactory<I,D extends Object> {

	public String getId();

	public D createSessionStore( I session );

	D getSessionStore(HttpSession session);
}
