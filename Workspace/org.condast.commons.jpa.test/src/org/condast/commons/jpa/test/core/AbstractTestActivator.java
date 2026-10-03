package org.condast.commons.jpa.test.core;

import java.net.URL;
import java.util.Enumeration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public abstract class AbstractTestActivator implements BundleActivator {

	private ITestBuilder manager;

	private static BundleContext context;

	private ExecutorService executors;
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				Enumeration<URL> enumeration = getContext().getBundle().findEntries( manager.getFolder(), ITestBuilder.S_WILDCARD, false);//false = no recursion
				manager.start(enumeration);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	};
	private boolean started = false;

	public static BundleContext getContext() {
		return context;
	}

	/**
	 * The service starts an additional thread when the conditions have been met to do so,
	 */
	protected abstract ITestBuilder createTestBuilder();

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		AbstractTestActivator.context = bundleContext;
		this.executors = Executors.newCachedThreadPool();
		this.manager = createTestBuilder();
		this.start();
	}

	/*
	 * (non-Javadoc)
	 * @see org.osgi.framework.BundleActivator#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		AbstractTestActivator.context = null;
		this.executors.shutdown();
		this.manager.stop();
	}

	protected synchronized void start(){
		if( started)
			return;
		this.started = true;
		this.executors.execute(runnable);
	}
}