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

import javax.security.auth.Subject;
import javax.security.auth.spi.LoginModule;

public interface ISubjectLoginModule extends LoginModule{

	/**
	 * Get the subject of this login module
	 * @return
	 */
	public Subject getSubject();
}
