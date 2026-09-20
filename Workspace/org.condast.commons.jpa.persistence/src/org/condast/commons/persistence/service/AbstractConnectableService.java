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

import org.condast.commons.service.ServiceConnectionException;

public abstract class AbstractConnectableService<T extends Object> implements IBaseService<T> {

	private boolean open;
	private T service;

	public AbstractConnectableService( T service ) {
	 this.service = service;
	}

	/**
	 * Connect to the eetmee service
	 * @return
	 */
	public boolean connect() {
		if( service == null )
			throw new NullPointerException( S_ERR_NO_SERVICE_FOUND );
		return this.isConnected();
	}

	protected boolean isConnected() {
		return ( this.service != null );
	}

	public void disconnect() {
		this.close();
		this.service = null;
	}

	/**
	 * actions required to open the service. Shoul return true if the service opened correctly
	 * @param service
	 * @return
	 */
	protected abstract boolean onOpen( T service );

	@Override
	public boolean open() {
		if( !this.isConnected() )
			throw new ServiceConnectionException( S_ERR_NO_SERVICE_FOUND );
		if( this.open ){
			throw new ServiceConnectionException( S_ERR_SERVICE_ALREADY_OPEN );
		}
		  this.open = this.onOpen(service);
		return open;
	}

	@Override
	public boolean isOpen() {
		this.open =  true;
		return this.open;
	}

	/**
	 * Returns true if the service is open, and throws an exception otherwise
	 * @return
	 */
	public boolean checkOpen(){
		if( !open )
			throw new ServiceConnectionException( ServiceConnectionException.S_ERR_SERVICE_NOT_OPENED );
		return this.open;
	}

	/**
	 * actions required to open the service. Should return true if the service closed correctly
	 * @param service
	 * @return
	 */
	protected abstract boolean onClose( T service );

	@Override
	public void close() {
		if( !this.open )
			return;
		this.open = !this.onClose(service);
	}

	/**
	 * Reopen the service. This is needed after an external commit, for instance when persisting a new object
	 */
	public void reopen() {
		this.open = false;
		this.open();
	}
}
