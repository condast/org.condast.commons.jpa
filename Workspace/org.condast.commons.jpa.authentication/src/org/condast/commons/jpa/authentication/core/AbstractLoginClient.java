package org.condast.commons.jpa.authentication.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.Utils;
import org.condast.commons.data.user.IAdmin;
import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.jpa.authentication.utils.AuthenticationUtils;

/**
 * The volunteer who has access to the data
 * @author Kees
 *
 */
@Deprecated
public abstract class AbstractLoginClient implements ILoginProvider{

	private Collection<IAuthenticationListener> alisteners;

	private String domain;
	private Map<Long, ILoginUser> users;
	
	protected AbstractLoginClient( String domain) {
		super();
		this.domain = domain;
		alisteners = new ArrayList<>();
		this.users = new HashMap<>();
	}

	@Override
	public void addAuthenticationListener( IAuthenticationListener listener ) {
		this.alisteners.add(listener);
	}

	@Override
	public void removeAuthenticationListener( IAuthenticationListener listener ) {
		this.alisteners.remove(listener);
	}

	public boolean addUser( ILoginUser user) {
		ILoginUser result = this.users.put(user.getId(), user);
		user.setSecurity(AuthenticationUtils.generateSecurityCode(user));
		return (result != null);
	}

	public boolean removeUser( ILoginUser user) {
		ILoginUser result =this.users.remove(user.getId());
		return (result != null);
	}

	public ILoginUser getUser(long userId) {
		return this.users.get(userId);
	}

	@Override
	public boolean isRegistered(long loginId) {
		ILoginUser user = this.users.get(loginId );
		return (user != null ) && ( user.isRegistered( domain ));
	}

	@Override
	public boolean isLoggedIn(long loginId, long security) {
		ILoginUser user = this.users.get(loginId );
		return ( user == null )? false: (user.getSecurity()==security);
	}

	public boolean isLoggedIn(long userId) {
		ILoginUser user = this.users.get(userId );
		return ( user != null );
	}

	@Override
	public ILoginUser getLoginUser(String domain, long loginId, long security ) {
		ILoginUser user = this.users.get(loginId );
		if(( user == null ) || !user.checkSecurity(security))
			return null;
		IAdmin admin = getAdmin(domain, user);
		return (admin != null )? user: null;
	}

	@Override
	public boolean hasLoginUser( String domain, String userName, long security ) {
		ILoginUser user = getLoginUser(domain, security, security);
		if( user == null )
			return false;
		IAdmin admin = getAdmin(domain, user);
		return ( admin != null );
	}

	@Override
	public IAdmin getAdmin(String domain, ILoginUser user) {
		Collection<IAdmin> admins = user.getAdmins(domain);
		return Utils.assertNull(admins)?null: admins.iterator().next();
	}

	@Override
	public boolean logout(String doamin, long loginId, long security ) {
		this.users.remove( loginId);
		return !this.users.containsKey(loginId );
	}

	@Override
	public boolean logout(String doman,  ILoginUser user ) {
		this.users.remove(user.getId());
		return !this.users.containsValue( user );
	}

	public void dispose(){
		alisteners.clear();
	}

}
