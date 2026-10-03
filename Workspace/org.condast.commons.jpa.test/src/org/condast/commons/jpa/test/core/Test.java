package org.condast.commons.jpa.test.core;

import org.condast.commons.jpa.test.core.AbstractTest;
import org.xml.sax.Attributes;

public class Test<D,R extends Object> extends AbstractTest<D,R> {

	private TestNodes node;

	public Test(Object arg0, String id, String name, int index, Attributes attributes ) {
		this( arg0, id, name, ITest.TestNodes.PREPARE, index, attributes, null );
	}

	public Test(Object arg0, String id, String name, int index, Attributes attributes, D data ) {
		this( arg0, id, name, ITest.TestNodes.PERFORM, index, attributes, data );
	}

	public Test(Object arg0, String id, String name, TestNodes event, Attributes attributes ) {
		this( arg0, id, name, event, 0, attributes, null );
	}

	public Test(Object arg0, String id, String name, TestNodes event, Attributes attributes, D data) {
		this( arg0, id, name, event, 0, attributes, data);
	}

	public Test(Object arg0, String id, String name, TestNodes node, int index, Attributes attributes, D data ) {
		super(arg0, id, name, node.name(), attributes, data, false );
		this.node = node;
	}

	@Override
	public TestNodes getTestNode() {
		return node;
	}


}