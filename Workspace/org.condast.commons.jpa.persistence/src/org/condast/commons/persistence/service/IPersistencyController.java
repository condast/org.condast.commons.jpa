package org.condast.commons.persistence.service;

import java.util.Collection;
import java.util.Map;

import javax.persistence.Query;

import org.condast.commons.persistence.clone.ICloneSupport;

public interface IPersistencyController<T, U extends Object> {

	/**
	 * Get the name of the entity that is returned
	 */
	public String getEntityName();

	/**
	 * connect to the persistence unit by acquiring an entity manager.
	 * returns true if the unit is connected
	 */
	public boolean connect();

	//Returns true if the controller is connected to the underlying entity manager
	boolean isConnected();

	void open();

	boolean isOpen();

	/**
	 * Get the results for the given query and limit this to maxResults
	 * @param query
	 * @return
	 */
	Collection<T> getQuery(String query);

	/**
	 * Get the results for the given query and limit this to
	 * the given start position and maxResults. if these are negative,
	 * the 0 is assumed
	 * @param query
	 * @param startPosition
	 * @param maxResult
	 * @return
	 */
	Collection<T> getQuery(String query, int startPosition, int maxResult);

	/**
	 * Create a new object. The init values may be needed to complete the creation
	 * @param init
	 * @return
	 */
	T create( U init );

	void persist(Object object);

	T update(T entity);

	void delete(T entity);

	/**
	 * Delete the selection from the database
	 * @param entities
	 */
	void delete(T[] entities);

	/**
	 * Clone the given object
	 * @param obj
	 * @return
	 * @throws CloneNotSupportedException
	 */
	Object clone(ICloneSupport<?> obj) throws CloneNotSupportedException;

	void close();

	/**
	 * this is a query that can be parameterised
	 * @param query
	 * @return
	 */
	Query getBaseQuery(String query);

	/**
	 * Create a query for the given attribute, where the attribute equals the given parameter
	 * @param attr
	 * @param param
	 * @return
	 */
	Collection<T> getQuery( String attr, String param);

	/**
	 * Create a query with a map of Strings
	 * @param params
	 * @return
	 */
	public Collection<T> getQuery( Map<String, String> params );

	/**
	 * Save the persistent objects to the database
	 */
	void save();

	/**
	 * An entity is detached from the entitymanager
	 * @param entity
	 */
	public void detach( T entity );

	/**
	 * Disconnect is performed in the default home screen.
	 */
	void disconnect();
}