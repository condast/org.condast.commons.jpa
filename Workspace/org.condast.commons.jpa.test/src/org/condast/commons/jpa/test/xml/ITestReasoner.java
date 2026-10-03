package org.condast.commons.jpa.test.xml;

import org.xml.sax.Attributes;

public interface ITestReasoner<D extends Object, R extends Object> {

	void performTest(String id, String name, Attributes attributes, D data);

}