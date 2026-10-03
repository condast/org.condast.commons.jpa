package org.condast.commons.jpa.test.xml;

import org.condast.commons.jpa.test.core.AbstractTestSuite;
import org.condast.commons.jpa.test.core.ITestEvent;
import org.xml.sax.Attributes;

public class TestSuite<D,R extends Object> extends AbstractTestSuite<D,R> {

	private String name;
	private boolean active;

	public TestSuite( String id, String name, Attributes attrs, boolean active ){
		super(id, attrs );
		this.active = active;
		this.name = name;
	}

	protected String getName() {
		return name;
	}

	protected boolean isActive() {
		return active;
	}

	public void runTests(  ){

		try {
			testSuite();
		} catch (Exception e) {
			e.printStackTrace();
		}
		System.exit(0);
	}

	@Override
	protected void testSuite() throws Exception {
		try{
		}
		catch( Exception ex ){
			ex.printStackTrace();
		}
		logger.info("Tests completed");
	}

	@Override
	protected void onPrepare(ITestEvent<D,R> event) {
		// NOTHING
	}

	@Override
	protected void onPerform(ITestEvent<D,R> event) {
		// NOTHING
	}
}