package org.condast.commons.persistence.connector;

import java.util.Collection;

/**
 * This connector allows for completion of a query with alternative data, for instance from
 * another database
 * @author Kees
 *
 */
public interface IQueryConnector<T extends Object> {

	/**
	 * open the connector
	 */
	public void open();

	/**
	 * Close the connector
	 */
	public void close();

	/**
	 * Complete the given query results
	 * @param results
	 */
	public void complete( Collection<T> results );
}
