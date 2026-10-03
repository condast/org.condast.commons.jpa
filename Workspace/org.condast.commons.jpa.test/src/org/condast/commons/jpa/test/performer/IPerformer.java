package org.condast.commons.jpa.test.performer;

import org.condast.commons.jpa.test.core.ITest;
import org.condast.commons.jpa.test.core.ITestEvent;
import org.condast.commons.jpa.test.core.ITestListener;

public interface IPerformer<D,R extends Object>{

	public enum Types{
		NULL,
		BOOLEAN,
		INTEGER;
	}

	void setNode(ITest<D, R> node);

	public void clear();

	void init(ITestEvent<D,R> event);

	public void prepare( ITestEvent<D,R> event );

	public void perform( ITestEvent<D,R> event );

	public boolean complete( ITestEvent<D,R> event);

	/**
	 * Used to start the parent post process
	 * @param event
	 */
	public void postProcess( ITestEvent<D,R> event);

	/**
	 * returns true if the performer is finished
	 * @return
	 */
	public boolean isFinished();

	String getAttribute(Enum<?> enm);

	void addListener(ITestListener<D, R> listener);

	void removeListener(ITestListener<D, R> listener);
}
