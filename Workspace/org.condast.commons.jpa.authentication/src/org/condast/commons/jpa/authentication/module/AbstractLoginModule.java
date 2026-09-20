package org.condast.commons.jpa.authentication.module;

import java.io.InputStream;
/*
*
* Copyright (c) 2000, 2016, Oracle and/or its affiliates. All rights reserved.
*
* Redistribution and use in source and binary forms, with or
* without modification, are permitted provided that the following
* conditions are met:
*
* -Redistributions of source code must retain the above copyright
* notice, this  list of conditions and the following disclaimer.
*
* -Redistribution in binary form must reproduct the above copyright
* notice, this list of conditions and the following disclaimer in
* the documentation and/or other materials provided with the
* distribution.
*
* Neither the name of Oracle nor the names of
* contributors may be used to endorse or promote products derived
* from this software without specific prior written permission.
*
* This software is provided "AS IS," without a warranty of any
* kind. ALL EXPRESS OR IMPLIED CONDITIONS, REPRESENTATIONS AND
* WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY
* EXCLUDED. SUN AND ITS LICENSORS SHALL NOT BE LIABLE FOR ANY
* DAMAGES OR LIABILITIES  SUFFERED BY LICENSEE AS A RESULT OF  OR
* RELATING TO USE, MODIFICATION OR DISTRIBUTION OF THE SOFTWARE OR
* ITS DERIVATIVES. IN NO EVENT WILL SUN OR ITS LICENSORS BE LIABLE
* FOR ANY LOST REVENUE, PROFIT OR DATA, OR FOR DIRECT, INDIRECT,
* SPECIAL, CONSEQUENTIAL, INCIDENTAL OR PUNITIVE DAMAGES, HOWEVER
* CAUSED AND REGARDLESS OF THE THEORY OF LIABILITY, ARISING OUT OF
* THE USE OF OR INABILITY TO USE SOFTWARE, EVEN IF SUN HAS BEEN
* ADVISED OF THE POSSIBILITY OF SUCH DAMAGES.
*
* You acknowledge that Software is not designed, licensed or
* intended for use in the design, construction, operation or
* maintenance of any nuclear facility.
*/
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.logging.Logger;

import javax.security.auth.Subject;
import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.NameCallback;
import javax.security.auth.callback.PasswordCallback;
import javax.security.auth.callback.UnsupportedCallbackException;
import javax.security.auth.login.FailedLoginException;
import javax.security.auth.login.LoginException;

import org.condast.commons.jpa.authentication.core.DefaultPrincipal;
import org.condast.commons.jpa.authentication.def.ISubjectLoginModule;

/**
* <p> This sample LoginModule authenticates users with a password.
*
* <p> This LoginModule only recognizes one user:       testUser
* <p> testUser's password is:  testPassword
*
* <p> If testUser successfully authenticates itself,
* a <code>SamplePrincipal</code> with the testUser's user name
* is added to the Subject.
*
* <p> This LoginModule recognises the debug option.
* If set to true in the login Configuration,
* debug messages will be output to the output stream, System.out.
*
*/
public abstract class AbstractLoginModule implements ISubjectLoginModule {

	protected static String S_DEF_AUTH_FILE = "/data/authentication.auth";
	private static String S_ERR_NO_CALLBACKHANDLER =
			"No CallbackHandler available to garner authentication information from the user";

	// initial state
	private Subject subject;
	private CallbackHandler callbackHandler;
	@SuppressWarnings("unused")
	private Map<String,?> sharedState;
	@SuppressWarnings("unused")
	private Map<String,?> options;

	// configurable option
	private boolean debug = false;

	// the authentication status
	private boolean succeeded = false;
	private boolean commitSucceeded = false;

	// username and password
	private String username;
	private char[] password;
	private String name;

	// testUser's SamplePrincipal
	private DefaultPrincipal userPrincipal;

	private Logger logger = Logger.getLogger(this.getClass().getName());

	protected AbstractLoginModule( String name ) {
		this.name = name;
	}

	/**
	 * Try to retrieve the handler if it isn't provided through the plugin.xml
	 * @return
	 */
	protected abstract CallbackHandler getHandlerIfNull();

	/**
	 * Initialize this <code>LoginModule</code>.
	 *
	 * <p>
	 *
	 * @param subject the <code>Subject</code> to be authenticated. <p>
	 *
	 * @param callbackHandler a <code>CallbackHandler</code> for communicating
	 *                  with the end user (prompting for user names and
	 *                  passwords, for example). <p>
	 *
	 * @param sharedState shared <code>LoginModule</code> state. <p>
	 *
	 * @param options options specified in the login
	 *                  <code>Configuration</code> for this particular
	 *                  <code>LoginModule</code>.
	 */
	@Override
	public void initialize(Subject subject,
			CallbackHandler callbackHandler,
			Map<java.lang.String, ?> sharedState,
			Map<java.lang.String, ?> options) {

		this.subject = subject;
		this.callbackHandler = callbackHandler;
		logger.info("Callback handler found: " + (this.callbackHandler != null ));
		if( callbackHandler == null ) {
			this.callbackHandler = this.getHandlerIfNull();
		}
		this.sharedState = sharedState;
		this.options = options;

		// initialise any configured options
		debug = "true".equalsIgnoreCase((String)options.get("debug"));
	}


	protected Collection<Callback> createCallbacks( CallbackHandler handler ) {
		Collection<Callback> callbacks = new ArrayList<>();
		callbacks.add( new NameCallback("Gebruiker: "));
		callbacks.add( new PasswordCallback("Toegangscode: ", false));
		return callbacks;
	}

	protected abstract boolean verifyCredentials( String username, char[] password );

	/**
	 * Authenticate the user by prompting for a user name and password.
	 *
	 * <p>
	 *
	 * @return true in all cases since this <code>LoginModule</code>
	 *          should not be ignored.
	 *
	 * @exception FailedLoginException if the authentication fails. <p>
	 *
	 * @exception LoginException if this <code>LoginModule</code>
	 *          is unable to perform the authentication.
	 */
	@Override
	public boolean login() throws LoginException {

		// prompt for a user name and password
		if ( this.callbackHandler == null)
			throw new LoginException( S_ERR_NO_CALLBACKHANDLER);

		Collection<Callback> results = createCallbacks( this.callbackHandler);
		Callback[] callbacks = results.toArray( new Callback[ results.size()]);
		return this.onHandleCallbacks(callbacks);
	}

	protected boolean onHandleCallbacks( Callback[] callbacks ) throws LoginException {
		try {
			callbackHandler.handle(callbacks);
			for( Callback cb: callbacks ) {
				if( cb instanceof NameCallback )
					this.username = ((NameCallback) cb).getName();
				if( cb instanceof PasswordCallback )
					this.password = ((PasswordCallback) cb).getPassword();
			}
			succeeded = verifyCredentials( username, password );
		} catch (java.io.IOException | UnsupportedCallbackException ioe) {
			throw new LoginException(ioe.toString());
		}

		// print debugging information
		if (debug) {
			StringBuffer buffer = new StringBuffer();
			buffer.append("\t\t " + this.name +
					"user entered user name: " +
					username);
			buffer.append("\t\t " + this.name +
					"user entered password: ");
			for (char element : password)
				buffer.append(element);
			logger.info( buffer.toString());
		}
		return succeeded;
	}

	/**
	 * <p> This method is called if the LoginContext's
	 * overall authentication succeeded
	 * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * succeeded).
	 *
	 * <p> If this LoginModule's own authentication attempt
	 * succeeded (checked by retrieving the private state saved by the
	 * <code>login</code> method), then this method associates a
	 * <code>SamplePrincipal</code>
	 * with the <code>Subject</code> located in the
	 * <code>LoginModule</code>.  If this LoginModule's own
	 * authentication attempted failed, then this method removes
	 * any state that was originally saved.
	 *
	 * <p>
	 *
	 * @exception LoginException if the commit fails.
	 *
	 * @return true if this LoginModule's own login and commit
	 *          attempts succeeded, or false otherwise.
	 */
	@Override
	public boolean commit() throws LoginException {
		if (!succeeded) {
			return false;
		} else {
			// add a Principal (authenticated identity)
			// to the Subject

			// assume the user we authenticated is the SamplePrincipal
			userPrincipal = new DefaultPrincipal(username);
			if (!subject.getPrincipals().contains(userPrincipal))
				subject.getPrincipals().add(userPrincipal);

			if (debug) {
				logger.info("\t\t" + this.name +
						"added SamplePrincipal to Subject");
			}

			// in any case, clean out state
			username = null;
			for (int i = 0; i < password.length; i++)
				password[i] = ' ';
			password = null;

			commitSucceeded = true;
			return true;
		}
	}

	/**
	 * <p> This method is called if the LoginContext's
	 * overall authentication failed.
	 * (the relevant REQUIRED, REQUISITE, SUFFICIENT and OPTIONAL LoginModules
	 * did not succeed).
	 *
	 * <p> If this LoginModule's own authentication attempt
	 * succeeded (checked by retrieving the private state saved by the
	 * <code>login</code> and <code>commit</code> methods),
	 * then this method cleans up any state that was originally saved.
	 *
	 * <p>
	 *
	 * @exception LoginException if the abort fails.
	 *
	 * @return false if this LoginModule's own login and/or commit attempts
	 *          failed, and true otherwise.
	 */
	@Override
	public boolean abort() throws LoginException {
		if (!succeeded) {
			return false;
		} else if (succeeded && !commitSucceeded) {
			// login succeeded but overall authentication failed
			succeeded = false;
			username = null;
			if (password != null) {
				for (int i = 0; i < password.length; i++)
					password[i] = ' ';
				password = null;
			}
			userPrincipal = null;
		} else {
			// overall authentication succeeded and commit succeeded,
			// but someone else's commit failed
			logout();
		}
		return true;
	}

	/**
	 * Logout the user.
	 *
	 * <p> This method removes the <code>SamplePrincipal</code>
	 * that was added by the <code>commit</code> method.
	 *
	 * <p>
	 *
	 * @exception LoginException if the logout fails.
	 *
	 * @return true in all cases since this <code>LoginModule</code>
	 *          should not be ignored.
	 */
	@Override
	public boolean logout() throws LoginException {

		subject.getPrincipals().remove(userPrincipal);
		succeeded = false;
		succeeded = commitSucceeded;
		username = null;
		if (password != null) {
			for (int i = 0; i < password.length; i++)
				password[i] = ' ';
			password = null;
		}
		userPrincipal = null;
		return true;
	}

	@Override
	public Subject getSubject() {
		return this.subject;
	}

	protected static Map<String, String> getUserNameAndPassword( InputStream in ){
		Scanner scanner = null;
		Map<String, String> results = new HashMap<>();
		try{
			scanner = new Scanner( in );
			while( scanner.hasNext() ){
				String line = scanner.nextLine();
				if( line.trim().startsWith("#"))
					continue;
				String[] split = line.split("[:;]");
				results.put(split[0], split[1]);
			}
		} catch ( Exception e) {
			e.printStackTrace();
		}
		finally{
			scanner.close();
		}
		return results;
	}
}