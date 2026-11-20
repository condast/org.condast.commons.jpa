package org.condast.commons.jpa.authentication.core;

import org.condast.commons.data.user.IAdmin;
import org.condast.commons.data.user.ILoginUser;

public interface ILoginProvider {

	public IAdmin getAdmin( String domain, ILoginUser user );
	
	public void addAuthenticationListener( IAuthenticationListener listener );

	public void removeAuthenticationListener( IAuthenticationListener listener );

	/**
	 * Returns true if a login user is registered
	 * @return
	 */
	public boolean isRegistered( long loginId );

	/**
	 * Returns true if a login user is logged in
	 * @return
	 */
	public boolean isLoggedIn( long loginId, long security );

	/**
	 * Get the login user. Requires a  security code to use
	 * @return
	 */
	public ILoginUser getLoginUser( String domain, long loginId, long security );

	/**
	 * Get the login user. Requires a valid security code to use
	 * @return
	 */
	public boolean hasLoginUser( String domain, String userName, long security );

	/**
	 * Log off the given user
	 * @param loginId
	 */
	boolean logout(String domain, long loginId, long security);

	boolean logout(String domain, ILoginUser user);
}
