package org.condast.commons.jpa.authentication.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.jpa.authentication.core.IAuthenticationListener.AuthenticationEvents;
import org.condast.commons.persistence.service.AbstractPersistencyService;

public abstract class AbstractLoginProvider extends AbstractPersistencyService implements ILoginProvider{

	private  Set<ILoginUser> users;

	private Collection<IAuthenticationListener> listeners;

	protected AbstractLoginProvider( String id, String service ) {
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

	public boolean addUser( ILoginUser user ){
		boolean found = this.users.contains(user);
		if( found ) {
			notifyListeners( new AuthenticationEvent( this, AuthenticationEvents.LOGIN, user ));
			return false;
		}
		this.users.add( user );
		notifyListeners( new AuthenticationEvent( this, AuthenticationEvents.LOGIN, user ));
		return true;
	}

	public boolean removeUser( ILoginUser user ) {
		boolean result = this.users.remove( user );
		notifyListeners( new AuthenticationEvent( this, AuthenticationEvents.LOGOUT, user ));
		return result;
	}

	public ILoginUser getUser( long id ) {
		for( ILoginUser user: this.users ) {
			if( user.getId() == id )
				return user;
		}
		return null;
	}

	@Override
	public boolean isLoggedIn(long loginId, long security) {
		for( ILoginUser user: this.users ) {
			if(( user.getId() == loginId ) && ( user.getSecurity() == security))
				return true;
		}
		return ( loginId <= 0);
	}

	@Override
	public ILoginUser getLoginUser(long loginId, long token ) {
		for( ILoginUser user: this.users) {
			if( user.getId() == loginId )
				return user;
		}
		return null;
	}

	@Override
	public Map<Long, String> getUserNames(Collection<Long> userIds) {
		Map<Long, String> results = new HashMap<>();
		for( ILoginUser user: this.users) {
			if( userIds.contains( user.getId()))
				results.put( user.getId(), user.getUserName());
		}
		return results;
	}

	@Override
	public void logout(long loginId, long token ) {
		ILoginUser user = getLoginUser(loginId, token );
		this.removeUser(user);
	}
}