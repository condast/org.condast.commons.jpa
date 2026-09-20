package org.condast.commons.persistence.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import org.condast.commons.Utils;
import org.condast.commons.service.IPersistencyServiceListener;
import org.condast.commons.service.IPersistencyServiceListener.Services;
import org.condast.commons.service.PersistencyServiceEvent;
import org.condast.commons.service.ServiceConnectionException;

/**
 * This utility class is provided with the example to execute the JDBC code that is
 * required as part of the example.  It provides a main method that can be used to run the code
 * outside of OSGi and several methods that actually populate the DB.
 *
 * @author keesp
 *
 */
public abstract class AbstractPersistencyService implements IPersistenceService{

	private EntityManagerFactory factory;
	private EntityManager manager;

	private String id;
	private String name;
	private boolean connected;

	private List<IPersistencyServiceListener> listeners;

	private Lock lock;

	private final Logger logger = Logger.getLogger( this.getClass().getCanonicalName());

	protected AbstractPersistencyService( String id, String name ) {
		this.id = id;
		this.name = name;
		this.connected = false;
		lock = new ReentrantLock();
		listeners = new ArrayList<>();
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public String getName() {
		return name;
	}

	/**
	 * returns true if the service is connected
	 * @return
	 */
	@Override
	public boolean isEnabled(){
		return ( factory != null );
	}

	@Override
	public EntityManager getManager(){
		return manager;
	}

	@Override
	public EntityManagerFactory getFactory() {
		return factory;
	}

	public synchronized void setEMF(EntityManagerFactory factory) {
		logger.info("Manager loaded: " + this.name + ": " + ( factory != null ) + "\n\n");
		this.factory = factory;
		this.notifyListeners( Services.ADD );
	}

	/**
	 * If the factory does not seem to enable for REST services, then maybe the RestServlet loads on startup. Try setting
	 * it to false
	 */
	@Override
	public synchronized void connect() {
		if( !this.isEnabled() )
			throw new ServiceConnectionException( "The " + this.name + S_ERR_NO_SERVICE_FOUND );
		if( this.connected )
			return;
		lock.lock();
		try{
			logger.info("CONNECTING Manager " + name + ": " + ( factory != null ));
			Map<String, String> map = new HashMap<>();
			manager = Utils.assertNull(map)? factory.createEntityManager():
				factory.createEntityManager( map );
			if( manager == null )
				return;
			logger.info("Manager CONNECTED " + name + ": " + ( manager != null ));
			connected = true;
			notifyListeners( Services.OPEN);

		}catch( Exception ex ){
			ex.printStackTrace();
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	public boolean isConnected() {
		return this.connected;
	}

	/**
	 * Returns true if the service is open, and throws an exception otherwise
	 * @return
	 */
	public boolean checkOpen(){
		if( !connected )
			throw new ServiceConnectionException( ServiceConnectionException.S_ERR_SERVICE_NOT_OPENED );
		return this.connected;
	}

	@Override
	public synchronized void disconnect() {
		if( !this.connected )
			return;
		this.connected = false;
		lock.lock();
		try{
			if( this.manager != null ){
				this.manager.clear();
				this.manager.close();
			}
			this.manager = null;
			logger.info("DISCONNECTING Manager  " + name + ": ");
		}
		finally{
			lock.unlock();
		}
		this.notifyListeners( Services.CLOSE);
	}

	/**
	 * Reopen the service. This is needed after an external commit, for instance when persisting a new object
	 */
	public void reopen() {
		this.connected = false;
		this.connect();
	}

	@Override
	public void addListener(
			IPersistencyServiceListener persistencyServiceListener) {
		lock.lock();
		try{
			listeners.add(persistencyServiceListener);
		}
		finally{
			lock.unlock();
		}
	}

	@Override
	public synchronized void removeListener(
			IPersistencyServiceListener persistencyServiceListener) {
		lock.lock();
		try{
			listeners.remove(persistencyServiceListener);
		}
		finally{
			lock.unlock();
		}
	}

	protected synchronized void notifyListeners( Services action) {
		lock.lock();
		try{
			for (IPersistencyServiceListener l : listeners) {
				l.notifyServiceChanged( new PersistencyServiceEvent( this, action ));
			}
		}
		finally{
			lock.unlock();
		}
	}
}