package org.condast.commons.jpa.na.address;

import java.util.Collection;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.jpa.na.model.IAddress;
import org.condast.commons.jpa.na.model.IPersonAddress;

public interface IAddressFinder{

	/**
	 * Get all the addresses with the given postcode and number. If the IAddress is not
	 * null, then that address is used
	 * We use multiple options in order to allow multiple check methods
	 * @param addresses
	 * @param postcode
	 * @param number
	 * @exception AddressException
	 * @return
	 */
	Collection<IPersonAddress> getAddresses( IAddress address, String postcode, String number ) throws AddressException;

	/**
	 * Get the location (latlng) for the given postcode and house number
	 * @param postcode
	 * @param houseNumber
	 * @return
	 */
	public LatLng getLocation( String postcode, int houseNumber);
}