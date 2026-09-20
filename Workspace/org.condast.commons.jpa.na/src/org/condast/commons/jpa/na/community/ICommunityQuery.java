package org.condast.commons.jpa.na.community;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.jpa.na.location.ICommunityQueryListener;
import org.condast.commons.jpa.na.model.ICommunity;

public interface ICommunityQuery {

	/**
	 * Prepare the postcodes within the given range
	 * @param start
	 * @param end
	 */
	void prepare( String start, String end);

	void addlistener( ICommunityQueryListener listener );

	void removelistener( ICommunityQueryListener listener );

	ICommunity locationQuery(String postcode, String houseNumber, LatLng location);

	void complete( ICommunity community, String houseNumber );
}