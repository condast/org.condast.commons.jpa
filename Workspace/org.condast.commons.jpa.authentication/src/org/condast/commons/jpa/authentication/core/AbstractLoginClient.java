package org.condast.commons.jpa.authentication.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;
import java.util.logging.Logger;

import org.condast.commons.data.user.IAdmin;
import org.condast.commons.data.user.ILoginUser;

/**
 * The volunteer who has access to the data
 * @author Kees
 *
 */
public abstract class AbstractLoginClient implements ILoginProvider{

	private ILoginProvider factory;

	private Collection<IAuthenticationListener> alisteners;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private IAuthenticationListener listener = e-> onNotifyAuthenticationEvent( e );
	
	protected AbstractLoginClient() {
		super();
		alisteners = new ArrayList<>();
	}

	@Override
	public void addAuthenticationListener( IAuthenticationListener listener ) {
		this.alisteners.add(listener);
	}

	@Override
	public void removeAuthenticationListener( IAuthenticationListener listener ) {
		this.alisteners.remove(listener);
	}

	private void onNotifyAuthenticationEvent(AuthenticationEvent e) {
		for( IAuthenticationListener listener: this.alisteners)
			listener.notifyLoginChanged(e);
	}

	public void setLoginProvider( ILoginProvider factory ){
		logger.info("Adding factory: " + factory.getClass().getName());
		this.factory = factory;
		this.factory.addAuthenticationListener(listener);
	}

	public void unsetLoginProvider( ILoginProvider factory ){
		if( this.factory == null )
			return;
		this.factory.removeAuthenticationListener(listener);
		this.factory = null;
	}

	public ILoginProvider getLoginProvider() {
		return factory;
	}

	@Override
	public boolean isRegistered(long loginId) {
		if( this.factory == null )
			return false;
		return factory.isRegistered(loginId);
	}

	public boolean isRegistered(long loginId, long security ) {
		if( this.factory == null )
			return false;
		ILoginUser user = factory.getLoginUser(loginId, security);
		return (user != null );
	}

	@Override
	public boolean isLoggedIn(long loginId, long security) {
		if( this.factory == null )
			return false;
		return factory.isLoggedIn(loginId, security);
	}

	@Override
	public ILoginUser getLoginUser(long loginId, long security ) {
		if( this.factory == null )
			return null;
		return factory.getLoginUser(loginId, security);
	}

	@Override
	public boolean hasLoginUser( String userName, long security ) {
		return ( factory == null )? false: factory.hasLoginUser(userName, security);
	}

	@Override
	public IAdmin getAdmin(ILoginUser user) {
		if( this.factory == null )
			return null;
		return factory.getAdmin(user);
	}

	@Override
	public Map<Long, String> getUserNames(Collection<Long> userIds) {
		return factory.getUserNames(userIds);
	}

	@Override
	public void logout(long loginId, long security ) {
		factory.logout(loginId, security);
	}

	@Override
	public void logout( ILoginUser user ) {
		factory.logout(user.getId(), user.getSecurity());
	}

	public void dispose(){
		alisteners.clear();
	}

}
