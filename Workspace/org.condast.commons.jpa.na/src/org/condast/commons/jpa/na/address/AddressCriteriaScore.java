package org.condast.commons.jpa.na.address;

import org.condast.commons.comparator.AbstractCriteriaScore;
import org.condast.commons.comparator.ICriteria;
import org.condast.commons.comparator.ICriteria.Score;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.data.latlng.LatLngUtils;
import org.condast.commons.jpa.na.model.ICommunityAddress;
import org.condast.commons.jpa.na.model.IPersonAddress;
import org.condast.commons.strings.StringUtils;

public class AddressCriteriaScore extends AbstractCriteriaScore<ICommunityAddress> {

	public enum Criteria implements ICriteria{
		ADDRESS(1),
		POST_CODE(2),
		DISTANCE(3),
		LNG_LAT(4);

		private int index;

		private Criteria( int index ){
			this.index = index;
		}

		protected int getIndex() {
			return index;
		}

		@Override
		public float getWeight() {
			return ((float)index/LNG_LAT.getIndex());
		}

		public static boolean isValid( ICriteria reference ){
			for( Criteria criterion: values() ){
				if( criterion.name().equals( reference.name() ))
					return true;
			}
			return false;
		}
	}

	public AddressCriteriaScore(ICommunityAddress reference) {
		super(reference);
	}

	@Override
	protected ICriteria[] onAddSelection() {
		return Criteria.values();
	}

	@Override
	public Object getValue(ICriteria criterion) {
		return findValue(criterion, getReference());
	}

	@Override
	public Object getValue(ICriteria criterion, ICommunityAddress test) {
		return findValue(criterion, test);
	}

	@Override
	protected Score onScore(ICriteria criterion, ICommunityAddress reference, ICommunityAddress test) {
		Score score = Score.IS_NULL;
		if( !( criterion instanceof Criteria))
			return score;

		Criteria crit = (Criteria) criterion;
		String postcode1 = reference.getPostcode();
		String postcode2 = test.getPostcode();
		switch( crit ){
		case ADDRESS:
			score = getNullScore( reference, test );
			break;
		case POST_CODE:
			score = getStringIsEmptyScore(postcode1, postcode2);
			break;
		case DISTANCE:
			int distance = StringUtils.computeStringDistance( postcode1, postcode2, true );
			score = getIntScore( distance, 0 );
			break;
		case LNG_LAT:
			if(!( reference instanceof IPersonAddress ) || !( test instanceof IPersonAddress ))
				return score;
			IPersonAddress pa1 = (IPersonAddress) reference;
			IPersonAddress pa2 = (IPersonAddress) test;
			double lat1 = pa1.getLatitude();
			double lng1 = pa1.getLongitude();
			double lat2 = pa2.getLatitude();
			double lng2 = pa2.getLongitude();
			distance = (int) LatLngUtils.getDistance( new LatLng( lat1, lng1), new LatLng( lat2, lng2), 0, 0);;
			score = getIntScore( distance, 0 );
			break;
		default:
			break;
		}
		return score;
	}

	public static Object findValue(ICriteria criterion, ICommunityAddress address) {
		Object result = null;
		if( !( criterion instanceof Criteria))
			return result;

		Criteria crit = (Criteria) criterion;
		String postcode1 = address.getPostcode();
		switch( crit ){
		case ADDRESS:
			result = ( address != null );
			break;
		case POST_CODE:
			result = !StringUtils.isEmpty(postcode1 );
			break;
		case DISTANCE:
			result = postcode1;
			break;
		case LNG_LAT:
			if(!( address instanceof IPersonAddress ) )
				return result;
			IPersonAddress pa1 = (IPersonAddress) address;
			double lat1 = pa1.getLatitude();
			double lng1 = pa1.getLongitude();
			result = new LatLng(lat1, lng1);
			break;
		default:
			break;
		}
		return result;
	}
}
