package org.condast.commons.bugzilla.api;

public interface IBugProductFactory {

	/**
	 * Create a bug product basedd on the given wildcard
	 * @param wildcard
	 * @return
	 */
	public IBugProduct createProduct( String wildcard );
}
