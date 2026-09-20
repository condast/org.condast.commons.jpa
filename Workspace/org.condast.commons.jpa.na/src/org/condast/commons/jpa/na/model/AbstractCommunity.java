package org.condast.commons.jpa.na.model;

import org.condast.commons.data.latlng.LatLng;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractCommunity implements ICommunity {

	private transient String locality;//is wijk geworden, was buurt
	//neighbourhood had eerst 2 betekenissen: buurt en stadsdeel
	//neighbourhood heeft nu alleen de betekenis van buurt en wordt uitgedrukt in meters
	//de betekenis stadsdeel is nu toegekend aan borough.
	//Kolom Neighbourhood in de community tabel is nu borough geworden.
	//private transient String neighbourhood;//neighbourhood is buurt geworden, was wijk
	private transient String borough;//stadsdeel
	private transient int administration;
	private transient String municipality;

	protected AbstractCommunity() {
		this( null, null, null );
	}

	protected AbstractCommunity( String locality, String borough, String municipality ) {
		this.locality= locality;
		this.borough = borough;
		this.administration = 0;
		this.municipality = municipality;
	}

	@Override
	public String getLocality() {
		return this.locality;
	}

	@Override
	public String getBorough() {
		return this.borough;
	}

	protected int getAdministration() {
		return administration;
	}

	@Override
	public String getMunicipality() {
		return this.municipality;
	}

	@Override
	public void setLocality(String locality) {
		if( StringUtils.isEmpty( locality))
			return;
		this.locality = locality;
	}

	@Override
	public void setBorough(String borough) {
		if( StringUtils.isEmpty( borough))
			return;
		this.borough = borough;
		if( borough.startsWith(S_WIJK)) {
			String adm = borough.substring(0,6);
			this.administration = Integer.parseInt(adm.replace(S_WIJK, ""));
			this.borough = borough.replace(adm, "");
		}
	}

	@Override
	public void setMunicipality(String municipality) {
		if( !StringUtils.isEmpty(municipality))
			this.municipality = municipality;
	}

	protected LatLng getLocation() {
		return new LatLng( this.getName(), this.getLatitude(), this.getLongtitude());
	}

	@Override
	public String toString() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("[");
		buffer.append( this.getPostcode());
		buffer.append("]: ");
		buffer.append( this.locality);
		buffer.append(", ");
		buffer.append( this.borough);
		buffer.append(", ");
		buffer.append( this.municipality);
		buffer.append(" {");
		buffer.append( this.getLocation().toLocation(10));
		buffer.append("} ");
		return buffer.toString();
	}
}
