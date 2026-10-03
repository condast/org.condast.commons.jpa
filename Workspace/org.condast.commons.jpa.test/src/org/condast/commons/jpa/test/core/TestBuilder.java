package org.condast.commons.jpa.test.core;

import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Enumeration;
import java.util.concurrent.TimeUnit;
import java.util.logging.Logger;

import org.condast.commons.jpa.test.XMLFactoryBuilder;
import org.condast.commons.jpa.test.core.ITestListener;

public class TestBuilder<D,R extends Object> implements ITestBuilder{

	private boolean autostart = true;

	private String folder, file;

	private int delay;

	private boolean started = false;

	private Class<?> clss;

	private Logger logger = Logger.getLogger( this.getClass().getName());

	private Collection<ITestListener<D,R>> listeners;

	public TestBuilder( Class<?> clss) {
		this( clss, true, S_DEFAULT_FOLDER, S_DEFAULT_DESIGN_FILE, DEFAULT_DELAY );
	}

	public TestBuilder( Class<?> clss, int delay ) {
		this( clss, true, S_DEFAULT_FOLDER, S_DEFAULT_DESIGN_FILE, delay );
	}

	protected TestBuilder( Class<?> clss, boolean autostart, String folder, String file, int delay ) {
		this.autostart = autostart;
		this.clss = clss;
		this.folder = folder;
		this.file = file;
		this.delay = delay;
		this.listeners = new ArrayList<>();
	}

	protected boolean isAutostart() {
		return autostart;
	}

	@Override
	public int getDelay() {
		return delay;
	}

	@Override
	public String getFolder() {
		return folder;
	}

	protected String getFile() {
		return file;
	}

	protected boolean isStarted() {
		return started;
	}


	public void addListener( ITestListener<D,R> listener ){
		this.listeners.add( listener);
	}

	public void removeListener( ITestListener<D,R> listener ){
		this.listeners.remove( listener);
	}

	/* (non-Javadoc)
	 * @see org.condast.commons.test.core.ITestManager#start(java.util.Enumeration)
	 */
	@Override
	public void start( Enumeration<URL> enumeration) throws Exception {
		TimeUnit.MILLISECONDS.sleep( this.delay );
		build( enumeration);
	}

	@Override
	public void stop() {
		// NOTHING
	}

	public void build( Enumeration<URL> enumeration) throws Exception {
		logger.info("\n\n STARTING TESTS !!!");
		while( enumeration.hasMoreElements() ){
			XMLFactoryBuilder<D,R> builder = new XMLFactoryBuilder<>( clss, enumeration.nextElement() );
			for( ITestListener<D,R> listener: listeners )
				builder.addListener(listener);
			builder.build();
			for( ITestListener<D,R> listener: listeners )
				builder.removeListener( listener );
		}
		logger.info("Tests completed\n\n");
	}
}
