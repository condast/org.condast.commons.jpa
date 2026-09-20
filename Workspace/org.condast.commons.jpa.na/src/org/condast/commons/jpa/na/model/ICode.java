package org.condast.commons.jpa.na.model;

/**
 * Provides information about the application to which the NAW data is assigned
 * @author Kees
 *
 */
public interface ICode extends Comparable<ICode>{

	/**
	 * Create a code that identifies the application at coding level, so that it does not
	 * show in the database
	 * @return
	 */
	public int getCode();
}
