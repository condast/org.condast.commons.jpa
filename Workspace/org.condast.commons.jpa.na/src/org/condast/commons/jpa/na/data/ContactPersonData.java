package org.condast.commons.jpa.na.data;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;

import org.condast.commons.Utils;
import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.na.model.IContactPerson;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.na.model.IContact.ContactTypes;

/**
 * The persistent class for the eet_tb_persoon database table.
 */
public class ContactPersonData implements IContactPerson, IName, Serializable {
	private static final long serialVersionUID = 1L;

	private long personId;
	
	private String firstName;

	private String surname;

	private String prefix;
	
	private Collection<ContactData> contacts;

	private long createDate;

	public ContactPersonData( ){
		this.contacts = new ArrayList<>();
		this.createDate = Calendar.getInstance().getTime().getTime();
	}

	public ContactPersonData( IContactPerson person ){
		this();
		this.personId = person.getId();
		this.firstName = person.getFirstName();
		this.prefix = person.getPrefix();
		this.surname = person.getSurname();
		for( IContact ct: person.getContactTypes()) {
			this.contacts.add( new ContactData( ct, this.personId));
		}
		this.createDate = getCreateDate(this).getTime();
	}

	public ContactPersonData( IName name ){
		this();
		this.firstName = name.getFirstName();
		this.prefix = name.getPrefix();
		this.surname = name.getSurname();
		this.createDate = Calendar.getInstance().getTimeInMillis();
	}

	@Override
	public long getId() {
		return personId;
	}
	
	public String getFirstName() {
		return this.firstName;
	}

	public void setFirstName(String firstName) {
		this.firstName = firstName;
	}

	public String getPrefix() {
		return this.prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getSurname() {
		return this.surname;
	}

	public void setSurname(String surName) {
		this.surname = surName;
	}

	@Override
	public String getName() {
		return this.firstName;
	}

	@Override
	public void setCallingName(String callingName) {
		this.firstName = callingName;
	}

	@Override
	public void setName(String text) {
		this.firstName = text;
	}

	@Override
	public void addContact( IContact contact ) {
		this.contacts.add((ContactData) contact);
	}

	@Override
	public void addContact(ContactTypes type, String value) {
		this.contacts.add( new ContactData( type, value ));
	}

	@Override
	public void setContacts(IContact[] contacts) {
		this.contacts.clear();
		if( Utils.assertNull(contacts))
				return;
		for( IContact contact: contacts ) 
			this.contacts.add( (ContactData) contact);	
	}

	@Override
	public void removeContact(IContact contact) {
		this.contacts.remove(contact);
	}

	public String getEmail() {
		for( IContact contact: this.contacts){
			if( ContactTypes.EMAIL.equals( contact.getContactType()))
				return contact.getValue();
		}
		return null;
	}

	public void clearContacts() {
		this.contacts.clear();
	}

	public IContact[] getContacts() {
		return this.contacts.toArray( new IContact[this.contacts.size() ]);
	}

	@Override
	public IContact[] getContactTypes() {
		return this.contacts.toArray( new IContact[ this.contacts.size()]);
	}

	public long getCreateDate() {
		return createDate;
	}

	public void setCreateDate(long createDate) {
		this.createDate = createDate;
	}

	@Override
	public int compareTo(IName o) {
		int compare = this.firstName.compareTo(o.getFirstName());
		if( compare != 0 )
			return compare;
		compare = this.surname.compareTo(o.getSurname());
		if( compare != 0 )
			return compare;
		compare = this.prefix.compareTo(o.getPrefix());
		return compare;
	}

	@Override
	public Object clone() throws CloneNotSupportedException {
		ContactPersonData clone = new ContactPersonData( this );
		return clone;
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append( "\n");
		return buffer.toString();
	}

	public static long[] getIds( Collection<IContact> batch ) {
		int size = Utils.assertNull(batch)?0:batch.size();
		long[] results = new long[size];
		if( size == 0)
			return results;
		int i=0;
		for( IContact data: batch) 
			results[i++] = data.getId();
		return results;
	}

	public static Date getCreateDate( IContactPerson contact ) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTimeInMillis(contact.getCreateDate());
		return calendar.getTime();
	}

}