package org.condast.commons.jpa.authentication.callback;

import javax.security.auth.callback.Callback;

import org.condast.commons.data.user.ILoginUser;

public class LoginUserCallback implements Callback {

	private ILoginUser user;

	public LoginUserCallback() {
	}

	public ILoginUser getUser() {
		return user;
	}

	public void setUser(ILoginUser user) {
		this.user = user;
	}
}
