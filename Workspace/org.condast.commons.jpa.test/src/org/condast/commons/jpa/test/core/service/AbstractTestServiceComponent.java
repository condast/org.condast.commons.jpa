package org.condast.commons.jpa.test.core.service;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

public abstract class AbstractTestServiceComponent<T extends Object>{

	public static final int DEFAULT_DELAY = 2000;//2 sec.

	private static Logger logger = Logger.getLogger( AbstractTestServiceComponent.class.getName() );

	private Collection<T> factories;

	private int delay;
	private ExecutorService executors;
	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try {
				TimeUnit.MILLISECONDS.sleep( delay );
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			onRunService();
		}
	};
	private boolean started = false;

	protected AbstractTestServiceComponent() {
		this ( DEFAULT_DELAY );
	}

	protected AbstractTestServiceComponent( int delay ) {
		this.delay = delay;
		factories = new ArrayList<>();
		executors = Executors.newCachedThreadPool();
		this.started = false;
	}

	/**
	 * The service starts an additional thread when the conditions have been met to do so,
	 */
	protected abstract void onRunService();

	public void activate(){
		logger.info("Started");
	}

	public void deactivate(){
		logger.info("Stopped");
	}

	protected synchronized void start(){
		if( started)
			return;
		this.started = true;
		this.executors.execute(runnable);
	}

	/**
	 * additional activities when ading a factory. returns true is the
	 * service can be started
	 * @param fc
	 * @return
	 */
	protected abstract boolean onFactoryAdded(T fc);

	/* (non-Javadoc)
	 * @see nl.cultuurinzicht.vastegast.service.ICompositeFactoryCollection#addFactory(org.condast.commons.ui.factory.ICompositeFactory)
	 */
	public void addFactory( T fc ){
		this.factories.add( fc );
		if( this.onFactoryAdded(fc))
			start();
	}

	/* (non-Javadoc)
	 * @see nl.cultuurinzicht.vastegast.service.ICompositeFactoryCollection#removeFactory(org.condast.commons.ui.factory.ICompositeFactory)
	 */
	public void removeFactory( T fc ){
		factories.remove( fc );
	}

	protected Collection<T> getFactories() {
		return factories;
	}
}