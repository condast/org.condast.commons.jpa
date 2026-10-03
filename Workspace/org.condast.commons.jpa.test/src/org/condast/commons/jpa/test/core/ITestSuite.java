package org.condast.commons.jpa.test.core;

import org.condast.commons.jpa.test.performer.IPerformer;

public interface ITestSuite<D,R extends Object> extends ITestNode<D,R>{

	/**
	 * Run the tests
	 */
	void performTests();

	IPerformer<D, R> getPerformer();

	void setPerformer(IPerformer<D, R> performer);

}