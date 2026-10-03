package org.condast.commons.jpa.test.xml;

import org.condast.commons.jpa.test.core.AbstractTestSuite;
import org.condast.commons.jpa.test.core.ITestEvent;


public class TestSet<D extends Object> extends AbstractTestSuite<Object,Object> {

	private String id;
	private String name;
	private boolean active;

	public TestSet( String id, String name, boolean active ){
		super(name, null);
		this.active = active;
		this.id = id;
		this.name = name;
	}

	@Override
	public String getId() {
		return id;
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
	protected void onPrepare(ITestEvent<Object,Object> event) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void onPerform(ITestEvent<Object,Object> event) {
		// TODO Auto-generated method stub

	}
}