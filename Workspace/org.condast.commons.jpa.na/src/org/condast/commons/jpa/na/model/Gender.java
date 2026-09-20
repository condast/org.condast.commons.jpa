package org.condast.commons.jpa.na.model;

import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.List;

import org.condast.commons.strings.StringUtils;

public enum Gender {
	UNKNOWN,
	MALE,
	FEMALE,
	ANDROGYNOUS,
	TRANSGENDER;

	@Override
	public String toString() {
		if( super.toString().equals( UNKNOWN.name() ) )
			return "-";
		return StringUtils.prettyString( super.toString() );
	}

	public static String[] getItems(){
		Collection<String> results = new ArrayList<>();
		for( Gender gender: values() ){
			results.add( gender.name() );
		}
		return results.toArray( new String[ results.size()] );
	}

	public static boolean isValid( String str ){
		if( StringUtils.isEmpty( str ))
			return false;
		for( Gender gender: values()){
			if( gender.name().equals( str ))
				return true;
		}
		return false;
	}

	public static String[] getStandardItems1() {
		ArrayList<String> genders = new ArrayList<>();
		genders.add( Gender.MALE.name() );
		genders.add( Gender.FEMALE.name() );
		return genders.toArray( new String[genders.size()] );
	}

	public static String[] getStandardItems(){
		Collection<String> results = new ArrayList<>();
		List<Gender> selection = new ArrayList<>( EnumSet.of( Gender.MALE, Gender.FEMALE ) );
		for( Gender gender: selection )
			results.add( gender.name() ) ;
		return results.toArray( new String[ results.size()] );
	}

	public static ArrayList<Gender> getStandardGenders() {
		ArrayList<Gender> standardGenders = new ArrayList<>();
		standardGenders.add( Gender.MALE );
		standardGenders.add( Gender.FEMALE );
		return standardGenders;
	}

	public static int getStandardValue( Gender gender ) {
		List<Gender> selection = new ArrayList<>( EnumSet.of( Gender.MALE, Gender.FEMALE ) );
		int number = selection.indexOf( gender );
		return number;
	}

	public String getAbbreviation(){
		Gender g = Gender.valueOf( super.name() );
		switch( g ){
		case MALE:
			return "M";
		case FEMALE:
			return "F";
		case ANDROGYNOUS:
			return "A";
		case UNKNOWN:
			return "U";
		case TRANSGENDER:
			return "T";
		default:
			return "-";
		}
	}

	public static Gender convertFrom( String value ){
		Gender gender = UNKNOWN;
		if( StringUtils.isEmpty( value ) ) {
			value = UNKNOWN.name();
		}
		value = value.toUpperCase();
		Character c = value.charAt( 0 );
		switch( c ) {
			case 'M':
				gender = MALE;
			break;
			case 'F'://fall through
			case 'V':
				gender = FEMALE;
			break;
			case 'A'://fall through
			case '*':
				gender = ANDROGYNOUS;
			break;
			case 'T':
				gender = TRANSGENDER;
				break;
			default:
				gender = UNKNOWN;

		}
		return gender;
	}

	public static String convertTo( Gender geslacht ){
		if( geslacht.name() == null )
			return null;
		return String.valueOf( geslacht.name().trim().toUpperCase() );
	}
}