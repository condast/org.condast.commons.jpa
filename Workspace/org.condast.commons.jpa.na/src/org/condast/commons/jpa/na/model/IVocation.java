package org.condast.commons.jpa.na.model;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.strings.StringUtils;

public interface IVocation {

	public enum Nature{
		UNKNOWN,
		FUNCTION,
		STUDENT,
		RETIRED;

		@Override
		public String toString() {
			return StringUtils.prettyString( super.toString() );
		}

		public static String[] getValues(){
			Collection<String> results = new ArrayList<>();
			for( Nature nature: values() ){
				results.add( nature.name() );
			}
			return results.toArray( new String[results.size()]);
		}

		public static boolean isStudySelected( int ordinal ){
			return STUDENT.equals( Nature.values()[ ordinal ]);
		}
	}

	public Nature getNature();

	public void setNature( Nature nature );

	public String getDescription();

	public void setDescription( String description );

	public boolean hasUPas();

	public void setUPas( boolean choice );
}
