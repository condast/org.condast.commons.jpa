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
package org.condast.commons.jpa.persistence.service;

import javax.persistence.EntityManager;

public interface IBasePersistenceService<C extends Object> extends IPersistenceService<EntityManager> {


	/**
	 * Get the controller for the given name
	 * @param name
	 * @return
	 */
	public C getController( String name );
}
