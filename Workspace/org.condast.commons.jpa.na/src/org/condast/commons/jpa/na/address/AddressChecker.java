package org.condast.commons.jpa.na.address;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.Utils;
import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.jpa.na.model.IAddress;
import org.condast.commons.jpa.na.model.IApplicationPersonController;
import org.condast.commons.jpa.na.model.ICommunityAddress;
import org.condast.commons.jpa.na.model.IPersonAddress;
import org.condast.commons.number.NumberUtils;
import org.condast.commons.strings.PostCodeUtils;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.verification.IVerification.VerificationTypes;

public class AddressChecker {

	private IAddress address;
	private IAddressFinder finder;
	private IApplicationPersonController apc;

	public AddressChecker( IApplicationPersonController apc, IAddressFinder finder ) {
		this.apc = apc;
		this.finder = finder;
	}

	public IAddress getAddress() {
		return address;
	}

	/**
	 * Returns true if the post code and number are sufficiently correct to create an address
	 * @param postocde
	 * @param number
	 * @return
	 */
	protected boolean isCorrectEntry( String postcode, String number ){
		boolean correct = VerificationTypes.verify( VerificationTypes.POSTCODE, postcode );
		return StringUtils.isEmpty( number )? false: correct;
	}

	/**
	 * Check the postal code and number
	 * (The in between returns are removed. In the previous version of this method the 'finally' with apc.close()...
	 * ...was not reached, because of the in between returns.)
	 * @param postcode
	 * @param number
	 */
	public Collection<IAddress> findAddresses( String postcode, String number, boolean strict ) throws AddressException{
		if( StringUtils.isEmpty( postcode ) )
			return null;
		Collection<IAddress> person_addresses = null;
		try{
			//look for person_addresses in the db:
			person_addresses = getStrictPersonAddresses( postcode, number, apc.findAddresses( postcode, number ) );
			//person_addresses can contain more than one!!
			apc.open();
			if( !Utils.assertNull( person_addresses ) ){
				appendLnglat( person_addresses );
			}
			else {//no addresses with number found in the db, so now look first for just addresses without number in the db
				person_addresses = new ArrayList<>();
				Collection<ICommunityAddress> addresses = apc.findAddresses( postcode );
				ICommunityAddress address = ( Utils.assertNull( addresses ) )? null: addresses.iterator().next();
				String numberWithoutExtension = Integer.toString( NumberUtils.getFirstDigits( number ) );
				Collection<IPersonAddress> add = finder.getAddresses( address, postcode, numberWithoutExtension );
				//Watch out. This collection is actually a collection of PersonAddressData!!
				//So the address within PersonAddressData.AddressData as a whole,..
				//..can not simply be casted to Address (later on)!!
				if( !Utils.assertNull( add ) ) {
					for( IPersonAddress pa: add ){
						String pc = pa.getPostcode();
						if( ( pc.length() == 4 ) && ( postcode.startsWith( pc ) ) )
							pa.setPostcode( PostCodeUtils.toStyledPostcode( postcode ) );
					}
				}
				if( !Utils.assertNull( add ) )
					person_addresses.addAll( add );
				if( strict )
					person_addresses = getStrictPersonAddresses( postcode, number, person_addresses );
				if( !Utils.assertNull( person_addresses ) )
					this.address = person_addresses.iterator().next();
			}
		}
		catch( AddressException ex ){
			throw ex;
		}
		catch( Exception ex ){
			throw new AddressException( ex.getMessage() );
		}
		finally{
			apc.close();
		}
		return person_addresses;
	}

	/**
	 * append the LngLat data to the person addresses if is not provided
	 * @param person_addresses
	 * @throws AddressException
	 */
	public void appendLnglat( Collection<IAddress> person_addresses ) throws AddressException{
		boolean changed = false;
		apc.isOpen();
		for( IAddress pa: person_addresses ){
			LatLng location = pa.getLocation();
			if( ( location.getLongitude() <= 0 ) || ( location.getLatitude() <= 0 ) ){
				String numberWithoutExtension = Integer.toString( NumberUtils.getFirstDigits( pa.getHouseNumber() ) );
				Collection<IPersonAddress> paddresses = finder.getAddresses( null, pa.getPostcode(), numberWithoutExtension );
				if( Utils.assertNull( paddresses ) )
					continue;
				IPersonAddress check = paddresses.iterator().next();
				pa.setLocation( check.getLatitude(), check.getLongitude() );
				changed = true;
			}
		}
		if( !changed )
			return;
		apc.save();
	}

	/**
	 * get the strict addresses, addresses that conform to both postcode and number
	 * @param postcode
	 * @param number
	 * @param addresses
	 * @return
	 */
	public Collection<IAddress> getStrictPersonAddresses( String postcode, String number, Collection<IAddress> addresses ){
		Collection<IAddress> temp = new ArrayList<>();
		String styled = PostCodeUtils.toStyledPostcode( postcode );
		for( IAddress pa: addresses ){
			if( pa.getPostcode().equals( styled ) && number.contains( pa.getHouseNumber() ) )
				temp.add( pa );
		}
		return temp;

	}
}
