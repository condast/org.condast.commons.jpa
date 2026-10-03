package org.condast.commons.jpa.test.xml;

import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public interface ITestListener<D extends Object> {

	public enum TestEvents{
		INIT,
		TEST_SUITE,
		TEST,
		TEST_OBJ,
		CONDITION,
		COMPLETE,
		CREATE,
		PREPARE,
		PERFORM,
		REASONER;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

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

	public enum AttributeNames{
		ID,
		NAME,
		CLASS,
		COUNT,
		TYPE,
		ACTIVE,
		INDEX,
		EXIT,
		GREEDY,
		PASS,
		START_DATE,
		END_DATE;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public String toXmlStyle() {
			return StringStyler.xmlStyleString( super.toString() );
		}

		public static boolean isAttribute( String value ){
			if( StringUtils.isEmpty( value ))
				return false;
			for( AttributeNames attr: values() ){
				if( attr.toString().equals( value ))
					return true;
			}
			return false;
		}
	}

	public void notifyTestEvent( TestEvent<D,?> event );
}
