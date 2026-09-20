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
package org.condast.commons.jpa.authentication.callback;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.security.auth.callback.Callback;
import javax.security.auth.callback.CallbackHandler;
import javax.security.auth.callback.UnsupportedCallbackException;

import org.condast.commons.jpa.authentication.def.IRegisterCallbackHandler;

public class RegistrationCallbackHandler implements CallbackHandler {

	private Map<String, IRegisterCallbackHandler> handlers;

	public RegistrationCallbackHandler() {
		handlers = new HashMap<>();
	}

	public void addHandler( IRegisterCallbackHandler handler ){
		this.handlers.put( handler.getID(), handler );
	}

	public void removeHandler( IRegisterCallbackHandler handler ){
		this.handlers.remove( handler.getID() );
	}

	@Override
	public void handle(Callback[] callbacks) throws IOException, UnsupportedCallbackException {
		for( CallbackHandler handler: this.handlers.values())
			handler.handle(callbacks);
	}
}