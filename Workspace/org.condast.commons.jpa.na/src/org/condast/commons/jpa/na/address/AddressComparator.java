package org.condast.commons.jpa.na.address;

import org.condast.commons.comparator.AbstractCriteriaComparator;
import org.condast.commons.jpa.na.model.ICommunityAddress;

public class AddressComparator extends AbstractCriteriaComparator<ICommunityAddress> {

	public AddressComparator( ICommunityAddress address ) {
		super( new AddressCriteriaScore( address ));
	}
}