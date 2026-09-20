package org.condast.commons.jpa.authentication.core;

import java.util.Collection;
import java.util.Map;

import org.condast.commons.data.user.IAdmin;
import org.condast.commons.data.user.ILoginUser;

public interface ILoginProvider {

	public IAdmin getAdmin( ILoginUser user );
	
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
	public ILoginUser getLoginUser( long loginId, long security );

	/**
	 * Get the login user. Requires a valid security code to use
	 * @return
	 */
	public boolean hasLoginUser( String userName, long security );

	/**
	 * Get the user names for the given user ids
	 * @param userIds
	 * @return
	 */
	public Map<Long, String> getUserNames( Collection<Long> userIds);

	/**
	 * Log off the given user
	 * @param loginId
	 */
	void logout(long loginId, long security);

	void logout(ILoginUser user);
}
