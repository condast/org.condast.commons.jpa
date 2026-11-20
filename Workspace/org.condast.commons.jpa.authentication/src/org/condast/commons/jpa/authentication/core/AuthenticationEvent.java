/*******************************************************************************
 * Copyright (c) 2016 Condast and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Contributors:
 *     Condast                - EetMee
 *     IBM Corporation - initial API and implementation
 *******************************************************************************/
package org.condast.commons.jpa.authentication.core;

import java.util.EventObject;

import org.condast.commons.data.user.ILoginUser;
import org.condast.commons.data.util.LoginData;

public class AuthenticationEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private LoginData.Requests event;

	private ILoginUser user;

	private String password;
	
	private long token;

	public AuthenticationEvent(Object arg0, LoginData.Requests event, long token ) {
		this(arg0, event, null, null, token );
	}

	public AuthenticationEvent(Object arg0, LoginData.Requests event, ILoginUser user, long token ) {
		this(arg0, event, user, null, token );
	}

	public AuthenticationEvent(Object arg0, LoginData.Requests event, ILoginUser user, String password, long token ) {
		super(arg0);
		this.event = event;
		this.user = user;
		this.password = password;
		this.token = token;
	}

	public LoginData.Requests getEvent(){
		return this.event;
	}

	public ILoginUser getUser(){
		return this.user;
	}

	public String getPassword() {
		return password;
	}

	public long getToken() {
		return token;
	}
}
