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

import java.io.Closeable;

import org.condast.commons.jpa.authentication.utils.StringStyler;
import org.eclipse.equinox.security.auth.ILoginContextListener;

public interface IAuthenticationManager<U extends Object> extends Closeable{

	public enum AuthenticationResults{
		INVALID_EMAIL,
		INVALID_PASSWORD,
		OK,
		UNKNOWN_EXCEPTION, 
		INVALID_NAME;

		public static boolean isValid( String str ){
			for( AuthenticationResults result: values() ){
				if( result.name().equals( StringStyler.styleToEnum(str )))
					return true;
			}
			return false;
		}

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public void registerListener( ILoginContextListener listener );

	public void unregisterListener( ILoginContextListener listener );

	/**
	 * Open the manager
	 */
	public void open();

	/**
	 * Returns true if the manager is open
	 * @return
	 */
	public boolean isOpen();

	/**
	 * Attempt to login. If reset is true, then the system always assumes that
	 * the system is logged off. if not,  logging in is terminated if the user
	 * is already logged in.
	 * @param reset
	 */
	public void login( boolean reset );

	boolean isLoggedIn();

	/**
	 * Get the login user
	 * @return
	 */
	public U getData();

	public void logout();

	/**
	 * Is used by the login module to set the user data object.
	 * Only if this is filled in, then a login can be considered succesfull
	 * @param user
	 */
	void setData(U user);
}