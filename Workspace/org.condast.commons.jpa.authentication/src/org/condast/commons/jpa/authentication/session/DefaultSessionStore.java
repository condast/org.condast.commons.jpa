package org.condast.commons.jpa.authentication.session;

import java.util.Locale;
import java.util.logging.Logger;

import javax.servlet.http.HttpSession;

import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.persistence.core.ISessionStoreFactory;

public class DefaultSessionStore<D extends Object> {

	public static final String S_ERR_NO_STORE_FOUND = "The session store is not active";

	private String id;

	private ILoginUser user;
	
	private Locale locale;

	private D data;

	public DefaultSessionStore( String id) {
		this.id = id;
		this.locale = Locale.getDefault();
	}

	public void clear() {
		this.user = null;
	}

	protected String getId() {
		return id;
	}

	public ILoginUser getLoginUser() {
		return user;
	}

	public void setLoginUser(ILoginUser user) {
		this.user = user;
	}

	public Locale getLocale() {
		return locale;
	}

	public void setLocale(Locale locale) {
		this.locale = locale;
	}

	public D getData() {
		return data;
	}

	public void setData(D data) {
		this.data = data;
	}

	/**
	 * Get the correct store for the given session from the factory
	 * @param storeFactory
	 * @param session
	 * @return
	 */
	public static <D extends Object> DefaultSessionStore<D> getStore( ISessionStoreFactory<HttpSession, DefaultSessionStore<D>> storeFactory, HttpSession session ) {
		try {
			Logger logger = Logger.getLogger( DefaultSessionStore.class.getName());
			if( storeFactory == null ) {
				logger.warning(S_ERR_NO_STORE_FOUND);
				return null;
			}
			DefaultSessionStore<D> sessionStore = storeFactory.createSessionStore(session);
			return sessionStore;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
