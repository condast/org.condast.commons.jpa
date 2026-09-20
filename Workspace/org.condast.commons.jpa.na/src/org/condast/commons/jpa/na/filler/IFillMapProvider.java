package org.condast.commons.jpa.na.filler;

import java.util.Map;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.jpa.na.community.ICommunityQuery;

/**
 * Implementations of this interface can fill a given String map
 * with values. The toString method of the keys will be used
 * @author Kees
 *
 */
public interface IFillMapProvider<T extends Object> {

	public static final String LOCATION_ID = "LOCATION";

	/**
	 * The id of the provider
	 * @return
	 */
	public String getId();

	/**
	 * Given a set of keys, the provider will try to fill them
	 * @param request
	 * @param params
	 * @param keys
	 * @return
	 * @throws FillMapException
	 */
	public Map<T, String> fillMap( String request, String[] params, T[] keys ) throws RuntimeException, FillMapException;

	/**
	 * Try to retrieve the location of the the given postcode and house number
	 * @param postcode
	 * @param houseNumber
	 * @return
	 * @throws FillMapException
	 */
	public LatLng getLocation(String postcode, int houseNumber) throws FillMapException;

	/**
	 * adds information about location to the post code
	 * @return
	 */
	ICommunityQuery getCommunityQuery();
}
