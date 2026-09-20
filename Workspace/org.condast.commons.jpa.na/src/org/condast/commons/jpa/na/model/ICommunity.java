package org.condast.commons.jpa.na.model;

import org.condast.commons.data.latlng.LatLng;

public interface ICommunity {

	public static final String S_Community = "Community";

	public static final String S_WIJK = "Wijk";

	public enum Localities{
		NO_PREFERENCE(0),	//geen voorkeur
		VICINITY(1),		//afstand
		NEIGHBOURHOOD(2), 	//buurt
		LOCALITY(3), 		//wijk
		BOROUGH(4),			//stadsdeel
		TOWN(5),			//plaats, dorp, stad
		MUNICIPALITY(6),	//gemeente
		AREA(7),			//gebied
		REGION(8), 			//regio
		OTHER(255);			//anders

		private int index;

		private Localities( int index ){
			this.index = index;
		}

		public int getIndex(){
			return index;
		}

		public boolean greater( Localities locality ){
			if( Localities.NO_PREFERENCE.equals( this ) )
				return false;
			return ( this.index > locality.getIndex() );
		}
	}

	public String getName();

	public String getPostcode();

	public String getLocality();

	public void setLocality( String locality );

	public String getMunicipality();

	public String getBorough();

	void setBorough( String borough );

	public double getLongtitude();

	public double getLatitude();

	void setLocation( double latitude, double longitude );

	void setMunicipality( String municipality );

	void setLocation( LatLng location );

	/**
	 * Get the max house number of this community.
	 * @return
	 */
	int getRange();

	void setLongitude( double longitude );

	void setLatitude( double latitude );

	void addAddress( IAddress address );

	void removeAddress( IAddress address );
}
