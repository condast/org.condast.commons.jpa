package org.condast.commons.jpa.na.model;

public interface ICommunityAddress extends IAddress{

	public void setCommunity( ICommunity community );

	/**
	 * Get the community to which this address belongs
	 * @return
	 */
	public ICommunity getCommunity();
}