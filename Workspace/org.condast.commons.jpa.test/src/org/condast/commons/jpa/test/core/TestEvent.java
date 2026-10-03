package org.condast.commons.jpa.test.core;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Map;

import org.condast.commons.strings.StringUtils;
import org.condast.commons.xml.BuildEvent;

public class TestEvent<D,R extends Object> implements ITestEvent<D,R> {

	private BuildEvent<D> buildEvent;

	private ITestEvent.TestEvents testEvent;
	private ITestNode<D,R> node;

	private String phase;

	private boolean pass;
	private D data;

	private R expected;
	private R result;

	@SuppressWarnings("unchecked")
	public TestEvent( BuildEvent<D> buildEvent ) {
		this.buildEvent = buildEvent;
		this.pass = true;
		this.node = (ITestNode<D,R>) buildEvent.getSource();
		this.phase = this.node.getId();
	}

	@Override
	public String getId() {
		return buildEvent.getId();
	}

	@Override
	public String getTest() {
		return phase;
	}

	@Override
	public void setTest(String phase) {
		this.phase = phase;
	}

	@Override
	public D getData() {
		return data;
	}

	@Override
	public void setData(D data) {
		this.data = data;
	}

	@Override
	public R getExpected() {
		return this.expected;
	}

	@Override
	public void setExpected(R expected) {
		this.expected = expected;
	}

	@Override
	public R getResult() {
		return result;
	}

	@Override
	public void setResult(R result) {
		this.result = result;
	}

	@Override
	public ITestNode<D,R> getTestNode() {
		return node;
	}

	@Override
	public String getAttribute(Enum<?> enm) {
		return this.buildEvent.getAttribute(enm);
	}

	@Override
	public boolean isPass() {
		return pass;
	}

	@Override
	public void setPass(boolean pass) {
		this.pass = pass;
	}

	@Override
	public ITestEvent.TestEvents getTestEvent() {
		return this.testEvent;
	}

	@Override
	public void setTestEvent(ITestEvent.TestEvents testEvent) {
		this.testEvent = testEvent;
	}

	@Override
	public String getContext() {
		return this.buildEvent.getAttribute( ITest.AttributeNames.CONTEXT );
	}

	@Override
	public Map<String, String> getAttributes() {
		return this.buildEvent.getAttributes();
	}

	/**
	 * Get the given attribute as argument list
	 * @param enm
	 * @return
	 */
	protected Collection<String> getArguments( Enum<?> enm ){
		String str = getAttribute( enm );
		Collection<String> results = new ArrayList<>();
		if( StringUtils.isEmpty(str ))
			return results;
		str = str.replace("[", "");
		str = str.replace("]", "");
		str = str.replace(" ", "");
		String[] split = str.split("[,]");
		results.addAll( Arrays.asList( split));
		return results;
	}


	/**
	 * Get the given attribute as argument list
	 * @param enm
	 * @return
	 */
	@Override
	public Collection<String> getArguments(){
		return getArguments( ITest.AttributeNames.ARGS );
	}
}