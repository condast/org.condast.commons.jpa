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
package org.condast.commons.persistence.service;

import javax.persistence.EntityManager;

import org.condast.commons.service.IPersistencyServiceListener;

public interface IBaseService<C extends Object> {

	public static final String S_ERR_NO_SERVICE_FOUND = " Service was not found. The persistency bundle may not be initialised correctly";
	public static final String S_ERR_SERVICE_ALREADY_OPEN = " Service is already open. The workflow may not be correct";

	/**
	 * open the service
	 * @return
	 */
	public boolean open();

	/**
	 * Returns true if the service is open
	 * @return
	 */
	public boolean isOpen();

	/**
	 * Close the service
	 */
	public void close();

	/**
	 * Get the entity manager
	 * @return
	 */
	public EntityManager getManager();

	/**
	 * Get the controller for the given name
	 * @param name
	 * @return
	 */
	public C getController( String name );

	void addListener(IPersistencyServiceListener persistencyServiceListener);

	void removeListener(IPersistencyServiceListener persistencyServiceListener);
}
