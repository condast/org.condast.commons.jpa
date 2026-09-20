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
package org.condast.commons.jpa.authentication.def;

import javax.security.auth.callback.CallbackHandler;

public interface IRegisterCallbackHandler extends CallbackHandler {

	/**
	 * Get the id for this callback handler
	 * @return
	 */
	public String getID();

}
