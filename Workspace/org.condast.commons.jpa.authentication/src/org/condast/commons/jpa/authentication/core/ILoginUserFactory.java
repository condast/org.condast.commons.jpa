package org.condast.commons.jpa.authentication.core;

import org.condast.commons.data.user.ILoginUser;

public interface ILoginUserFactory {

	public void addAuthenticationListener( IAuthenticationListener listener );

	public void removeAuthenticationListener( IAuthenticationListener listener );

	public ILoginUser registerUser( String userName, String password, String email );

	public void logoff( ILoginUser user );

	public void unregisterUser( ILoginUser user );

}
