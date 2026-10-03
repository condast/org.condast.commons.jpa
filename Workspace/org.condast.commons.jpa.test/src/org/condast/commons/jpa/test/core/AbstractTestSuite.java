package org.condast.commons.jpa.test.core;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import org.condast.commons.jpa.test.performer.IPerformer;
import org.xml.sax.Attributes;

public abstract class AbstractTestSuite<D,R extends Object> extends AbstractTestNode<D,R> implements ITestSuite<D,R> {

	protected Logger logger = Logger.getLogger( this.getClass().getName() );

	private ExecutorService service;

	private Runnable runnable = new Runnable(){

		@Override
		public void run() {
			try{
				testSuite();
			}
			catch( Exception ex ){
				ex.printStackTrace();
			}
		}
	};

	private IPerformer<D,R> performer;

	protected AbstractTestSuite( String id ) {
		this(id, null );
	}

	protected AbstractTestSuite( String id, Attributes attrs ) {
		super( id, attrs );
		service = Executors.newCachedThreadPool();
	}

	/**
	 * Implement the test suite
	 * @throws Exception
	 */
	protected abstract void testSuite() throws Exception;

	@Override
	public IPerformer<D, R> getPerformer() {
		return performer;
	}

	@Override
	public void setPerformer(IPerformer<D, R> performer) {
		this.performer = performer;
	}


	/**
	 * Run the tests
	 */
	@Override
	public void performTests(){
		service.execute(runnable);
	}

	@Override
	public void perform() {
		super.notifyTestEvent(super.getEvent());
	}
}