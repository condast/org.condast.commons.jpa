package org.condast.commons.jpa.authentication.core;

import javax.security.auth.Subject;
import javax.security.auth.login.LoginException;

import org.eclipse.equinox.security.auth.ILoginContextListener;

/**
 * Convenience implementation for listeners that only need to overridfe
 * one method
 * @author Kees
 *
 */
public class LoginContextAdapter implements ILoginContextListener {

	public LoginContextAdapter() {
	}

	@Override
	public void onLoginStart(Subject subject) {
		// NOTHING
	}

	@Override
	public void onLoginFinish(Subject subject, LoginException loginException) {
		// NOTHING
	}

	@Override
	public void onLogoutStart(Subject subject) {
		// NOTHING
	}

	@Override
	public void onLogoutFinish(Subject subject, LoginException logoutException) {
		// NOTHING
	}

}
