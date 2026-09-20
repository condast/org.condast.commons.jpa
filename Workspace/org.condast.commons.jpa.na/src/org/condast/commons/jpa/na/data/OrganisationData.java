package org.condast.commons.jpa.na.data;

import java.io.Serializable;
import java.util.Calendar;
import java.util.Collection;

import org.condast.commons.Utils;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.jpa.na.model.IContactPerson;

public class OrganisationData implements Serializable, Cloneable {
	private static final long serialVersionUID = 1L;

	private long organisationId;
	
	private LatLng location;
	
	private AddressData address;

	private ContactPersonData contact;
	
	private String name;
	
	private String description;
	
	private String website;
		
	public OrganisationData() {
		super();
		this.organisationId = -1;
		Calendar calendar = Calendar.getInstance();
		calendar.add( Calendar.YEAR, 1);
	}

	public OrganisationData( LatLng location ){
		this();
		this.location = location;
		this.name = location.getId();
		this.description = this.location.getDescription();
	}

	public OrganisationData( LatLng location, String name, String description ){
		this( location );
		this.name = name;
		this.description = description;
	}

	public long getId() {
		return this.organisationId;
	}

	protected void setOrganisationId(long organisationId) {
		this.organisationId = organisationId;
	}

	public LatLng getLocation() {
		return location;
	}

	public void setLocation(LatLng location) {
		this.location = location;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getWebsite() {
		return website;
	}

	public void setWebsite(String website) {
		this.website = website;
	}

	public IContactPerson getContact() {
		return (IContactPerson) contact;
	}

	public void setContact(ContactPersonData contact) {
		this.contact = contact;
	}

	public AddressData getAddress() {
		return address;
	}
		
	public static long[] getIDs( Collection<? extends OrganisationData> organisations) {
		if( Utils.assertNull(organisations))
			return new long[0];
		long[] results = new long[ organisations.size()];
		int index = 0;
		for( OrganisationData organisation: organisations ) {
			results[index++] = organisation.getId();
		}
		return results;
	}
}