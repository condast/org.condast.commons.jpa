package org.condast.commons.jpa.na.model;

public interface IPersonAddress extends ICommunityAddress{

	long getId();

	IAddress getAddress();

	IPerson getPerson();

	String getNumber();

	double getLatitude();

	void setLatitude(double latitude);

	double getLongitude();

	void setLongtitude(double longtitude);

	String getExtension();

	void setExtension(String extension);

	void setNumber(String number);

	void setPerson(IPerson person);

	String getHouseNumber();

	void setHousenumber(String text);

	/**
	 * Returns true if the given postcode and number matches this one
	 * @param postcode
	 * @param number
	 * @return
	 */
	public boolean isAddress( String postcode, String number );

	/**
	 * Get a string id of the person address
	 * @return
	 */
	public String toStringId();
}