package org.condast.commons.persistence.search;

import org.condast.commons.persistence.connector.IQueryConnector;

public interface IConnectableElement<T extends Object> {

	/**
	 * Set the query for this element. Optionally add a selection, so that certain elements of
	 * the query can be highlighted in the composite
	 * @param query
	 */
	public void setQuery( String query, int[] selection );


	/**
	 * Set the connector for this composite
	 * @param connector
	 */
	void setConnector(IQueryConnector<T> connector);

}
