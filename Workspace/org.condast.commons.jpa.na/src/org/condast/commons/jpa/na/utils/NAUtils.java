package org.condast.commons.jpa.na.utils;

import org.condast.commons.date.DateUtils;
import org.condast.commons.jpa.na.model.ICommunityAddress;
import org.condast.commons.jpa.na.model.IName;
import org.condast.commons.jpa.na.model.IProfessional;
import org.condast.commons.jpa.na.model.IAddress.AddressTypes;
import org.condast.commons.strings.StringUtils;

public class NAUtils {

	public static String printPersonDetails( IProfessional person, boolean full ) {
		StringBuffer buffer = new StringBuffer();
		IName naam = person.getName();
		String str = ( naam != null )? naam.toString() : "";
		buffer.append( str );
		buffer.append( "\n");
		ICommunityAddress address = person.getAddress( AddressTypes.MAIN );
		buffer.append( address );
		buffer.append( "\n");
		if( !full )
			return buffer.toString();
		buffer.append( DateUtils.getFormatted( person.getBirthDate() ) + ", ");
		buffer.append( str );
		buffer.append( "\n");
		buffer.append( person.getVocation().toString() + "\n");
		return buffer.toString();
	}

	/**
	 * Prints a name as <surname, firstname infix>
	 * @param name
	 * @return
	 */
	public static String toSurnameFirstnameInfix( IName name ) {
		StringBuffer s = new StringBuffer();
		if (!StringUtils.isEmpty( name.getSurname())) {
			s.append( name.getSurname().trim());
			s.append(", ");
		}
		if (!StringUtils.isEmpty( name.getFirstName())) {
			s.append( name.getFirstName().trim());
		}
		if (!StringUtils.isEmpty( name.getPrefix())) {
			s.append(" ");
			s.append( name.getPrefix().trim());
		}
		return s.toString();
	}

}
