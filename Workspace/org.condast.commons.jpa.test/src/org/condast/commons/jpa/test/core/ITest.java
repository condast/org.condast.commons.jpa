package org.condast.commons.jpa.test.core;

import org.condast.commons.jpa.test.performer.IPerformer;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;

public interface ITest<D,R extends Object> extends ITestNode<D,R> {

	public enum TestNodes{
		INIT,
		TEST_SUITE,
		TEST,
		SUB_TEST,
		HTTP_TEST,
		CONDITION,
		COMPLETE,
		LISTENER,
		PREPARE,
		PERFORM,
		REASONER;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ) {
			for( TestNodes te: values()) {
				if( te.name().equals(str ))
					return true;
			}
			return false;
		}
	}

	enum AttributeNames{
		ACTIVE,
		ARGS,
		CLASS,
		CONTEXT,
		COUNT,
		ON_COMPLETE,
		DATA,
		END_DATE,
		EXIT,
		FILE,
		GREEDY,
		ID,
		IGNORE,
		INDEX,
		MAX_COUNT,
		NAME,
		PERFORMER,
		PASS,
		RESPONSE_CODE,
		START_DATE,
		TYPE,
		URI;

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

	public enum IgnoreTypes{
		ALLOW,
		DECLINE
	}


	/**
	 * sometimes a completion will return a different value than the test expects.
	 * for instance two non-null values will cause the next score to start.
	 * This option tells the test maanger to ignore the result of the score
	 * @author Kees
	 *
	 */
	public enum OnCompleteTypes{
		CONTINUE,
		STOP
	}

	boolean isActive();

	void setActive(boolean active);

	TestNodes getTestNode();

	/**
	 * Set the data
	 * @param data
	 */
	public void setData(D data);

	boolean isPass();

	public IPerformer<D,R> getPerformer();

	public void setPerformer(IPerformer<D,R> performer);

	String getContext();

	ITestEvent<D,R> getTestEvent();

	/**
	 * Create a new test event and prepare the test
	 */
	@Override
	void prepare();

	/**
	 * Perform the test
	 */

	@Override
	void perform();
}
