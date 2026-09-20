package org.condast.commons.jpa.na.model;

import org.condast.commons.jpa.na.model.IContact.ContactTypes;

public interface IContactPerson extends IName{

	public static final String S_TEAM = "Team";
	public static final String S_MARIA = "Maria";
	
	public long getId();

	/**
	 * @return the name
	 */
	public abstract String getName();
		
	IContact[] getContactTypes();

	/**
	 * Clear the contacts
	 */
	void clearContacts();

	void addContact(ContactTypes type, String value);

	void addContact( IContact contact );

	void setContacts( IContact[] contacts );

	void removeContact(IContact contact);

	@Override
	public abstract String toString();

	public abstract Object clone() throws CloneNotSupportedException;
	
	public long getCreateDate();
}