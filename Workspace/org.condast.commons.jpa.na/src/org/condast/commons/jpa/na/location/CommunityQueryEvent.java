package org.condast.commons.jpa.na.location;

import java.util.EventObject;

import org.condast.commons.jpa.na.community.CommunityResource;

public class CommunityQueryEvent extends EventObject {
	private static final long serialVersionUID = -4494461707109793687L;

	String postcode;
	int range;
	String neighbourhood;
	String municipality;
	String locality;

	public CommunityQueryEvent(Object arg0) {
		super(arg0);
	}

	public CommunityQueryEvent( Object arg0, String[] split) {
		this( arg0, split[ CommunityResource.Resources.POSTCODE.getIndex()],
				Integer.parseInt(split[ CommunityResource.Resources.HOUSE_NUMBER.getIndex()]),
				split[ CommunityResource.Resources.LOCALITY.getIndex()],
				split[ CommunityResource.Resources.NEIGHBOURHOOD.getIndex()],
				split[ CommunityResource.Resources.MUNICIPALITY.getIndex()]);
	}

	public CommunityQueryEvent(Object arg0, String postcode, int range, String locality, String neighbourhood, String municipality) {
		super(arg0);
		this.postcode = postcode;
		this.range = range;
		this.neighbourhood = neighbourhood;
		this.municipality = municipality;
		this.locality = locality;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public String getPostcode() {
		return postcode;
	}

	public int getRange() {
		return range;
	}

	public String getNeighbourhood() {
		return neighbourhood;
	}

	public String getMunicipality() {
		return municipality;
	}

	public String getLocality() {
		return locality;
	}
}
