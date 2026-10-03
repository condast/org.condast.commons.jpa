package org.condast.commons.jpa.test.core;

import org.xml.sax.Attributes;

public interface ITestNode<D,R extends Object>  {

	/**
	 * initialise the test
	 * @param attributes
	 */
	void init(Attributes attributes);

	public String getId();

	public D getData();

	public ITestNode<D,R> getParent();

	void setParent(ITestNode<D,R> parent);

	void addChild(ITestNode<D,R> child);

	void removeChild(ITestNode<D,R> child);

	String getAttribute(Enum<?> enm);

	/**
	 * the test event
	 * @return
	 */
	ITestEvent<D,R> getEvent();

	void addListener( ITestListener<D,R> listener);

	void removeListener( ITestListener<D,R> listener );

	/**
	 * The life cycle of the test node
	 */
	void prepare();
	void perform();
	void complete();

}
