package org.condast.commons.persistence.service;

import java.io.Closeable;

import jakarta.persistence.EntityManager;
import org.condast.commons.io.IOUtils;

public class TransactionManager implements Closeable{

	private IPersistenceService service;
	private boolean open;

	public TransactionManager( IPersistenceService service) {
		super();
		this.open = false;
		this.service = service;
	}

	public boolean isConnected() {
		return this.service.isConnected();
	}

	protected IPersistenceService getService() {
		return service;
	}

	protected EntityManager getManager() {
		return this.service.getManager();
	}

	public void open() {
		service.connect();
		EntityManager manager = getManager();
		if( manager == null )
			return;
		try {
			manager.getTransaction().begin();
		} catch (Exception e) {
			e.printStackTrace();
		}
		this.open = true;
	}

	public boolean isOpen() {
		return open;
	}

	@Override
	public void close() {
		if( !this.open )
			return;
		this.open = false;
		EntityManager manager = getManager();
		manager.getTransaction().commit();
	}
	
	public void closeQuietly() {
		IOUtils.close( this );
	}
}