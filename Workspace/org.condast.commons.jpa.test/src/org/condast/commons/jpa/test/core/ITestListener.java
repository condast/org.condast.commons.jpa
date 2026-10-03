package org.condast.commons.jpa.test.core;

import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public interface ITestListener<D,R extends Object> {

	public static enum Types{
		BOOLEAN,
		INTEGER,
		RANGE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public static enum Defaults{
		NOW,
		DEFAULT;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isDefault( String str ) {
			if( StringUtils.isEmpty(str))
				return false;
			for( Defaults def: values()) {
				if( def.name().equals( StringStyler.styleToEnum(str)))
					return true;
			}
			return false;
		}
	}

	public enum Pass{
		NONE_TRUE,
		ALL_TRUE,
		CUSTOM;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	public void init(Attributes attributes);
	public void notifyTestEvent( ITestEvent<D,R> event );
}
