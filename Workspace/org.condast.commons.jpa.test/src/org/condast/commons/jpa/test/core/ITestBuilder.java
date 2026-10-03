package org.condast.commons.jpa.test.core;

import java.net.URL;
import java.util.Enumeration;

public interface ITestBuilder{

	public static String S_DEFAULT_FOLDER = "TEST-INF";
	public static String S_DEFAULT_DESIGN_FILE = "suite.xml";

	public static final String S_WILDCARD = "*.xml";

	public static int DEFAULT_DELAY = 5000;//5 seconds delay prior to starting

	String getFolder();

	/*
	 * Start the tests
	 */
	void start( Enumeration<URL> enumeration) throws Exception;

	/**
	 * Stop the tests
	 */
	void stop();

	int getDelay();
}