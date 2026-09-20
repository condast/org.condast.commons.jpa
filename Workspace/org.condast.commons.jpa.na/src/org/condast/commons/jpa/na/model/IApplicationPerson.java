package org.condast.commons.jpa.na.model;

import org.condast.commons.jpa.na.model.IContact.ContactTypes;

public interface IApplicationPerson extends Cloneable{

	/**
	 * Returns true if the details of the application person has been filled in coorectly
	 * @return
	 */
	public boolean isValid();
	public void setValid( boolean choice );

	void addMailingPreference( IMailPreferences mailpreferences);

	void removeMailingPreference( IMailPreferences mailpreferences);

	IContact[] getContactTypes();

	/**
	 * Clear the contacts
	 */
	void clearContacts();

	void addContact(ContactTypes type, String value);

	void addContact( IContact contact );

	void setContacts( IContact[] contacts );

	void removeContact(IContact contact);

	IProfessional getPerson();

	IApplication getApplication();

	public long getApplicationPersonId();

	Object clone() throws CloneNotSupportedException;
}