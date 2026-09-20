package org.condast.commons.jpa.na.data;

import java.io.Serializable;

import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public class ContactData implements IContact, Serializable {
	private static final long serialVersionUID = -7296625942864747857L;

	public enum Parameters{
		PERSON_ID,
		APPLICATION,
		CONTACT_TYPE,
		VALUE,
		RESTRICTED;

		@Override
		public String toString() {
			return StringStyler.xmlStyleString( name());
		}
	}

	private long id;
	private long personId;
	private String contactType;
	private String value;
	private boolean restricted; //restricted to this application only
	private int application;
	
	public ContactData() {
		this( ContactTypes.UNKNOWN, null );
	}

	public ContactData( IContact contact, long personId ){
		this.id = contact.getId();
		this.personId = personId;
		this.contactType = contact.getContactType().name();
		this.value = contact.getValue();
		this.application = contact.getApplication();
		this.restricted = contact.isRestricted();
	}

	public ContactData( ContactTypes type, String value ){
		this.id = -1;
		this.contactType = type.name();
		this.value = value;
		this.application = -1;
	}
	
	@Override
	public long getId() {
		return id;
	}
	
	public long getPersonId() {
		return this.personId;
	}

	@Override
	public ContactTypes getContactType() {
		if( StringUtils.isEmpty( this.contactType ))
			return ContactTypes.UNKNOWN;
		return ContactTypes.valueOf(this.contactType);
	}

	@Override
	public String getValue() {
		return this.value;
	}

	@Override
	public boolean isRestricted() {
		return restricted;
	}

	@Override
	public int getApplication() {
		return application;
	}

	@Override
	public void setContactType(ContactTypes contactType) {
		this.contactType = contactType.name();
	}

	@Override
	public void setValue(String value) {
		this.value = value;
	}

	@Override
	public void setRestricted(boolean restricted) {
		this.restricted = restricted;
	}

	@Override
	public String toString(){
		StringBuffer buffer = new StringBuffer();
		buffer.append( this.contactType );
		buffer.append(": ");
		buffer.append( this.value );
		return buffer.toString();
	}
}
