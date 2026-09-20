/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.condast.commons.jpa.authentication.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;
import javax.security.auth.spi.LoginModule;

import org.eclipse.equinox.security.auth.ILoginContext;
import org.eclipse.equinox.security.auth.ILoginContextListener;
import org.eclipse.equinox.security.auth.LoginContextFactory;

public abstract class AbstractAuthenticationManager<U extends Object> implements IAuthenticationManager<U> {

	private static final String S_JAAS_CONFIG_FILE = "/data/jaas.cfg";

	public static final String S_ERR_LOGIN_FAIL_MSG = "Cannot log in";
	public static final String S_MSG_LOGIN_SUCCESSFUL_MSG = "LOGIN SUCCESSFUL!";

	private String configfile;
	private String configName;

	private U userData;

	private Collection<ILoginContextListener> listeners;

	private ILoginContext context;
	private boolean open;

	//Is true when OAuth2 has successfully committed a login procedure
	private boolean commitSuccess;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	private ILoginContextListener lcListener = new ILoginContextListener(){

		@Override
		public void onLoginFinish(Subject arg0, LoginException arg1) {

			logger.info("LOGIN FINISHED SUCCESSFULLY: " + ( arg1 == null ));
			boolean override = (arg1 != null ) && handleLoginFailed(arg0, arg1);
			logger.info("OVERRIDE: " + override );
			for( ILoginContextListener listener: listeners )
				listener.onLoginFinish(arg0, arg1);
		}

		@Override
		public void onLoginStart(Subject arg0) {
			for( ILoginContextListener listener: listeners )
				listener.onLoginStart(arg0);
		}

		@Override
		public void onLogoutFinish(Subject arg0, LoginException arg1) {
			userData = null;
			handleLogoutFailed(arg0, arg1);
			for( ILoginContextListener listener: listeners )
				listener.onLogoutFinish(arg0, arg1);
		}

		@Override
		public void onLogoutStart(Subject arg0) {
			commitSuccess = false;
			for( ILoginContextListener listener: listeners )
				listener.onLogoutStart(arg0);
		}
	};

	protected AbstractAuthenticationManager( String name ) {
		this( S_JAAS_CONFIG_FILE, name );
	}

	protected AbstractAuthenticationManager( String configFile, String name ) {
		this.configfile = configFile;
		this.commitSuccess = false;
		this.configName = name;
		this.open = false;
		listeners = new ArrayList<>();
	}

	@Override
	public U getData() {
		return userData;
	}

	@Override
	public void setData(U user) {
		this.userData = user;
	}

	/* (non-Javadoc)
	 * @see com.condast.authentication.core.IAuthenticationManager#isLoggedin()
	 */
	@Override
	public boolean isLoggedIn() {
		return ( this.userData != null );
	}

	/* (non-Javadoc)
	 * @see com.condast.authentication.core.IAuthenticationManager#addListener(com.condast.authentication.core.IAuthenticationListener)
	 */
	@Override
	public void registerListener( ILoginContextListener listener ){
		this.listeners.add( listener );
	}

	/* (non-Javadoc)
	 * @see com.condast.authentication.core.IAuthenticationManager#removeListener(com.condast.authentication.core.IAuthenticationListener)
	 */
	@Override
	public void unregisterListener( ILoginContextListener listener ){
		this.listeners.remove( listener );
	}

	/**
	 * Open the context factory
	 */
	@Override
	public void open(){
		URL configURL = getClass().getResource( this.configfile );
		context = LoginContextFactory.createContext(configName, configURL );
		context.registerListener( lcListener);
		this.open = true;
	}

	@Override
	public boolean isOpen(){
		return open;
	}

	@Override
	public void close(){
		this.open = false;
		if( context != null )
			context.unregisterListener( lcListener );
	}

	public void reset(){
		this.close();
	}

	/**
	 * Refresh the composite
	 * @param logout
	 */
	protected abstract void refresh( final boolean logout);

	/* (non-Javadoc)
	 * @see com.condast.authentication.core.IAuthenticationManager#login()
	 */
	@Override
	public void login( boolean reset ){
		if( !reset && isLoggedIn() )
			return;
		try {
			context.login();
		} catch (Exception e) {
			if(!commitSuccess )
			logger.warning( e.getMessage());
		}
		if( commitSuccess )
			logger.info( S_MSG_LOGIN_SUCCESSFUL_MSG);
	}

	/**
	 * Get the login module
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	protected abstract LoginModule getLoginModule(Subject arg0, LoginException arg1);

	/**
	 * If a login fails, try an alternative approach, e.g. using declarative
	 * services
	 * @param arg0
	 * @param arg1
	 * @return
	 */
	protected boolean handleLoginFailed(Subject arg0, LoginException arg1) {
		if( arg1 == null )
			return true;
		LoginModule module = getLoginModule(arg0, arg1);
		logger.info(this.getClass().getName() + ": Retrieving login module: " + ( module != null ));
		try {
			module.login();
			commitSuccess = module.commit();
		} catch (LoginException e) {
			e.printStackTrace();
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		return commitSuccess;
	}

	protected boolean handleLogoutFailed(Subject arg0, LoginException arg1) {
		return false;
	}

	/**
	 * Handle logout stuff
	 */
	protected abstract void onLogout( U userData );

	/* (non-Javadoc)
	 * @see com.condast.authentication.core.IAuthenticationManager#logoff()
	 */
	@Override
	public void logout(){
		this.onLogout( this.userData);
		userData = null;
		try {
			context.logout();
		} catch (LoginException e) {
			e.printStackTrace();
			refresh(true);
		}
	}
}