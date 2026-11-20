package org.condast.commons.jpa.authentication.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Set;
import java.util.TreeSet;

import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.data.util.LoginData;
import org.condast.commons.jpa.persistence.service.AbstractPersistencyService;

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
			notifyListeners( new AuthenticationEvent( this, LoginData.Requests.LOGIN, user,0l ));
			return false;
		}
		this.users.add( user );
		notifyListeners( new AuthenticationEvent( this, LoginData.Requests.LOGIN, user,0l ));
		return true;
	}

	public boolean removeUser( ILoginUser user ) {
		boolean result = this.users.remove( user );
		notifyListeners( new AuthenticationEvent( this, LoginData.Requests.LOGOFF, user,0l ));
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
	public ILoginUser getLoginUser(String domain, long loginId, long token ) {
		for( ILoginUser user: this.users) {
			if( user.getId() == loginId )
				return user;
		}
		return null;
	}

	
	@Override
	public boolean logout(String domain, long loginId, long token ) {
		ILoginUser user = getLoginUser(domain, loginId, token );
		return this.removeUser(user);
	}
}