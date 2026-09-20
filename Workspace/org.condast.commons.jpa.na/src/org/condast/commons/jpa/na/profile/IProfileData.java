package org.condast.commons.jpa.na.profile;

import org.condast.commons.data.util.LoginData;
import org.condast.commons.jpa.na.data.AddressData;
import org.condast.commons.jpa.na.data.OrganisationData;
import org.condast.commons.jpa.na.model.IContact;
import org.condast.commons.jpa.na.model.IContactPerson;

public interface IProfileData extends IContactPerson{

	LoginData getLoginUser();

	AddressData getAddress();

	void setAddress(AddressData address);

	void addOrganisation(OrganisationData organisation);

	void removeOrganisation(OrganisationData organisation);

	OrganisationData[] getOrganisation();

	String getEmail();

	IContact[] getContacts();

	long getId();
}