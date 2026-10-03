package org.condast.commons.jpa.test.core;

import java.util.Collection;
import java.util.Map;

import org.condast.commons.strings.StringStyler;

public interface ITestEvent<D,R extends Object>{

	enum TestEvents{
		ANY,
		CREATE,
		INIT,
		PREPARE,
		PERFORM,
		COMPLETE,
		POST_PROCESS;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}

		public static boolean isValid( String str ) {
			for( TestEvents te: values()) {
				if( te.name().equals(str ))
					return true;
			}
			return false;
		}
	}

	public String getId();

	/**
	 * The event passes through various (sub-)tests. The id of the active test
	 * is returned here
	 * @return
	 */
	public String getTest();

	/**
	 * Set the test
	 * @param test
	 */
	void setTest(String test);

	public D getData();

	/**
	 * Get/set the expected outcome of the test
	 * @return
	 */
	public R getExpected();
	public void setExpected( R expected );

	/**
	 * Get/set the result of this test
	 * @return
	 */
	public R getResult();
	public void setResult( R result );

	public void setData(D data);

	boolean isPass();

	void setPass(boolean pass);

	ITestNode<D,R> getTestNode();

	TestEvents getTestEvent();

	public void setTestEvent( TestEvents event );

	public String getAttribute( Enum<?> enm );

	String getContext();

	Map<String, String> getAttributes();

	/**
	 * Arguments allow for passing variables between tests
	 * @return
	 */
	public Collection<String> getArguments();
}
