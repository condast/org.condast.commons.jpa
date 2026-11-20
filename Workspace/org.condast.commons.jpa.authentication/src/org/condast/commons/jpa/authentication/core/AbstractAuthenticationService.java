package org.condast.commons.jpa.authentication.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.data.util.LoginData;
import org.condast.commons.data.user.IAdmin;
import org.condast.commons.jpa.persistence.service.AbstractPersistencyService;

public abstract class AbstractAuthenticationService<U extends ILoginUser> extends AbstractPersistencyService implements ILoginProvider{

	private  Set<U> users;

	private Collection<IAuthenticationListener> listeners;

	protected AbstractAuthenticationService( String id, String service ) {
		super( id, service );
		users = new TreeSet<>();
		listeners = new ArrayList<>();
	}

	@Override
	public void addAuthenticationListener( IAuthenticationListener listener ) {
		this.listeners.add(listener);
	}

	@Override
	public void removeAuthenticationListener( IAuthenticationListener listener ) {
		this.listeners.remove(listener);
	}

	protected void notifyListeners( AuthenticationEvent event ) {
		for( IAuthenticationListener listener: this.listeners )
			listener.notifyLoginChanged( event );
	}

	public boolean isRegistered( ILoginUser user ) {
		return this.users.contains( user );
	}

	public boolean addUser( U user ){
		boolean found = this.users.contains(user);
		if( found ) {
			notifyListeners( new AuthenticationEvent( this, LoginData.Requests.LOGIN, user, 0l ));
			return false;
		}
		this.users.add( user );
		notifyListeners( new AuthenticationEvent( this, LoginData.Requests.LOGIN, user, 0l ));
		return true;
	}

	public boolean removeUser( U user ) {
		boolean result = this.users.remove( user );
		notifyListeners( new AuthenticationEvent( this, LoginData.Requests.LOGOFF, user,0l ));
		return result;
	}

	public U getUser( long id ) {
		for( U user: this.users ) {
			if( user.getId() == id )
				return user;
		}
		return null;
	}

	@Override
	public boolean isLoggedIn(long loginId, long security) {
		for( U user: this.users ) {
			if( user.getId() == loginId )
				return true;
		}
		return ( loginId <= 0);
	}

	@Override
	public U getLoginUser(String domain, long loginId, long token ) {
		for( U user: this.users) {
			if( user.getId() == loginId ) {
				Set<IAdmin> admins = user.getAdmins(domain);
				for( IAdmin a : admins) {
					if( a.getDomain().equals(domain))
						return user;
				}
			}
		}
		return null;
	}

	@Override
	public boolean logout(String domain, long loginId, long token ) {
		U user = getLoginUser(domain, loginId, token );
		return ( user == null )? false: this.removeUser(user);
	}
}