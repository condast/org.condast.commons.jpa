package org.condast.commons.jpa.authentication.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.jpa.authentication.core.IAuthenticationListener.AuthenticationEvents;
import org.condast.commons.persistence.service.AbstractPersistencyService;
import org.condast.commons.persistence.service.IPersistenceService;

public abstract class AbstractAuthenticationService<U extends ILoginUser> extends AbstractPersistencyService implements ILoginProvider, IPersistenceService{

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
			notifyListeners( new AuthenticationEvent( this, AuthenticationEvents.LOGIN, user ));
			return false;
		}
		this.users.add( user );
		notifyListeners( new AuthenticationEvent( this, AuthenticationEvents.LOGIN, user ));
		return true;
	}

	public boolean removeUser( U user ) {
		boolean result = this.users.remove( user );
		notifyListeners( new AuthenticationEvent( this, AuthenticationEvents.LOGOUT, user ));
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
	public U getLoginUser(long loginId, long token ) {
		for( U user: this.users) {
			if( user.getId() == loginId )
				return user;
		}
		return null;
	}

	@Override
	public void logout(long loginId, long token ) {
		U user = getLoginUser(loginId, token );
		this.removeUser(user);
	}
}