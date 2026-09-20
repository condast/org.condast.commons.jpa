package org.condast.commons.jpa.na.community;

import java.util.Arrays;
import java.util.Collection;
import java.util.TreeSet;

import org.condast.commons.Utils;

public class CommunityResource implements Comparable<CommunityResource>{

	public enum Resources{
		POSTCODE(0),
		HOUSE_NUMBER(1),
		LOCALITY(2),
		NEIGHBOURHOOD(3),
		MUNICIPALITY(4);

		private int index;

		private Resources( int index ) {
			this.index = index;
		}

		public int getIndex() {
			return index;
		}
	}

	private Resources resource;
	private String name;
	private int id;
	private Collection<String> range;//the range of postcodes

	public CommunityResource(Resources resource, int id, String name) {
		this( resource, id, name, null );
	}

	public CommunityResource(Resources resource, int id, String name, String[] range) {
		super();
		this.resource = resource;
		this.name = name;
		this.id = id;
		this.range = new TreeSet<>();
		if( !Utils.assertNull(range))
			this.range.addAll( Arrays.asList(range));
	}
	public Resources getResource() {
		return resource;
	}

	public String getName() {
		return name;
	}

	public int getId() {
		return id;
	}
	public String[] getRange() {
		return range.toArray( new String[ range.size()]);
	}

	public void addPostcode(String postcode) {
		this.range.add(postcode);
	}

	@Override
	public int compareTo(CommunityResource arg0) {
		if( arg0 == null )
			return 1;
		int compare = ( this.resource.getIndex() - arg0.getResource().getIndex());
		if( compare != 0)
			return compare;
		compare = this.id - arg0.getId();
		return compare;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append( this.resource.name());
		builder.append(", ");
		builder.append( this.id );
		builder.append(", ");
		builder.append( this.name );
		builder.append(", ");
		builder.append( this.name );
		builder.append(": [");
		for( String pc: range ) {
			builder.append( pc );
			builder.append( ", " );
		}
		builder.deleteCharAt(builder.length()-1);
		builder.deleteCharAt(builder.length()-1);
		builder.append("]");
		return builder.toString();
	}


}
