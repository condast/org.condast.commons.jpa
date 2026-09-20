package org.condast.commons.jpa.na.model;

import org.condast.commons.strings.StringUtils;

public interface IName extends Comparable<IName>{

	public enum Attributes{
		NAME,
		FIRST_NAME,
		PREFIX,
		SURNAME;

		@Override
		public String toString() {
			return StringUtils.prettyString( super.toString() );
		}
	}

	/**
	 * @return the first name
	 */
	public abstract String getFirstName();

	/**
	 * @param firstname the first name to set
	 */
	public abstract void setFirstName(String firstName );

	/**
	 * @return the name (optional, defaults to first name)
	 */
	public abstract String getName();

	/**
	 * @param roepnaam the roepnaam to set
	 */
	public abstract void setCallingName(String callingName );

	/**
	 * @return the prefix
	 */
	public abstract String getPrefix();

	/**
	 * @param prefix the Prefix to set
	 */
	public abstract void setPrefix(String prefix);

	/**
	 * @return the SurName
	 */
	public abstract String getSurname();

	/**
	 * @param surname the SurName to set
	 */
	public abstract void setSurname(String surName);

	public abstract void setName(String text);
}