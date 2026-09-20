package org.condast.commons.jpa.na.model;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.strings.StringUtils;

public interface IAddress {

	public enum Attributes{
		ADRESS_ID,
		WOONPLAATS,
		STRAATNAAM,
		POSTCODE,
		BUURT;

		@Override
		public String toString() {
			return StringUtils.prettyString( super.toString() );
		}
	}

	public enum AddressTypes{
		SECUNDARY(0),
		CONTACT(1),
		BILLING(2),
		INTERNATIONAL(4),
		MAIN(128);

		private byte value;

		private AddressTypes(){
			this.value = (byte)128;
		}

		private AddressTypes( int value ){
			this.value = (byte) value;
		}

		public byte getValue(){
			return value;
		}

		protected void setType( byte value ){
			this.value = value;
		}

		public void addType( AddressTypes type){
			value |= type.getValue();
			switch( type ){
			case SECUNDARY:
				value &= 0xFE;
				break;
			default:
				break;
			}
		}

		public void removeType( AddressTypes type){
			byte temp = (byte) (0xF ^ type.getValue());
			value &= temp;
			switch( type ){
			case MAIN:
				value |= SECUNDARY.getValue();
				break;
			default:
				break;
			}
		}

		public AddressTypes[] getAddressTypes(){
			Collection<AddressTypes> results = new ArrayList<>();
			for( AddressTypes type: AddressTypes.values() ){
				if(( value & type.getValue() ) >0  )
					results.add( type );
			}
			return results.toArray( new AddressTypes[results.size()]);
		}

		/**
		 * Create a new address type
		 * @param value
		 * @return
		 */
		public static AddressTypes create(byte value) {
			AddressTypes type = AddressTypes.SECUNDARY;
			type.setType(value);
			return type;
		}

		@Override
		public String toString() {
			return StringUtils.prettyString( super.toString() );
		}
	}

	public enum Countries{
		BELGIUM,//BE
		ENGLAND,//UK
		GERMANY,//DE
		THE_NETHERLANDS;//NL

		@Override
		public String toString() {
			return StringUtils.prettyString( super.toString() );
		}

		public static Countries getStandardCountry() {
			return Countries.THE_NETHERLANDS;
		}
	}

	public long getAddressId();

	/**
	 * @return the street
	 */
	public abstract String getStreet();

	/**
	 * @return the street extension
	 */
	public abstract String getStreetExtension();

	void setNumber(String number);

	String getHouseNumber();

	/**
	 * @return the post code
	 */
	public abstract String getPostcode();

	void setPostcode(String styledPostcode);

	/**
	 * @return the town
	 */
	public abstract String getTown();

	@Override
	public abstract String toString();

	String getCountry();

	/**
	 * Get the lon- and latitude of this address, or null if it is not provided
	 * @return
	 */
	public LatLng getLocation();

	public boolean hasValidLocation();

	void setLocation( double latitude, double longtitude);

	/**
	 * Prints the street (optional), postcode and number
	 * @return
	 */
	public String printStreet( boolean skipStreet );

	String getName();
}