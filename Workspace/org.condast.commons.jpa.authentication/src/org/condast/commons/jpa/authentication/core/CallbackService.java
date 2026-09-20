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

import org.condast.commons.jpa.authentication.callback.RegistrationCallbackHandler;
import org.condast.commons.jpa.authentication.def.IRegisterCallbackHandler;

public class CallbackService {

	private RegistrationCallbackHandler handler;

	private static CallbackService service = new CallbackService();

	private CallbackService() {
		handler = new RegistrationCallbackHandler();
	}

	public static CallbackService getInstance(){
		return service;
	}
	public void addHandler( IRegisterCallbackHandler handler ){
		this.handler.addHandler( handler );
	}

	public void removeHandler( IRegisterCallbackHandler handler ){
		this.handler.removeHandler( handler );
	}
}
