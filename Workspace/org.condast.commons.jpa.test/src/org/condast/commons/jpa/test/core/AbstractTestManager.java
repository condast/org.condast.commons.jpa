package org.condast.commons.jpa.test.core;

import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.BundleContext;

public abstract class AbstractTestManager {

	public static String S_DEFAULT_FOLDER = "TEST-INF";
	public static String S_DEFAULT_DESIGN_FILE = "suite.xml";

	private ITestBuilder manager;

	private ExecutorService executors;
	private Enumeration<URL> enumeration;

	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				manager.start(enumeration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	private boolean started = false;

	protected AbstractTestManager() {
		this.executors = Executors.newCachedThreadPool();
		this.manager = createTestBuilder();
	}

	protected boolean isStarted() {
		return started;
	}

	/**
	 * The service starts an additional thread when the conditions have been met to do so,
	 */
	protected abstract ITestBuilder createTestBuilder();

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	public void stop() throws Exception {
		this.executors.shutdown();
		this.manager.stop();
	}

	protected void start( BundleContext context ) {
		if(( context == null ) || this.isStarted())
			return;
		Enumeration<URL> enumeration = context.getBundle().findEntries( S_DEFAULT_FOLDER, "*.xml", false);//false = no recursion
		start(enumeration);
	}

	protected synchronized void start( Enumeration<URL> enumeration ){
		if( started)
			return;
		this.enumeration = enumeration;
		this.started = true;
		this.executors.execute(runnable);
	}
}
