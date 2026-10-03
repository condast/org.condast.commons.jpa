package org.condast.commons.jpa.test.performer;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.condast.commons.jpa.test.core.ITest;
import org.condast.commons.jpa.test.core.ITestEvent;
import org.condast.commons.jpa.test.core.ITestListener;
import org.condast.commons.jpa.test.core.ITestNode;
import org.condast.commons.jpa.test.core.ITestEvent.TestEvents;
import org.condast.commons.strings.StringUtils;

public abstract class AbstractPerformer<D,R extends Object> implements IPerformer<D,R> {

	public enum Results{
		SUCCESS,
		FAIL;

		@Override
		public String toString() {
			return super.toString().toLowerCase();
		}

		public static Results getResult( boolean choice ) {
			return choice? Results.SUCCESS: Results.FAIL;
		}

	}
	private ITest<D,R> node;
	private int index;
	private int maxCount;
	private boolean skip;
	private boolean stop;

	private StringBuilder builder;

	private Collection<ITestListener<D,R>> listeners;

	private Logger logger = Logger.getLogger( this.getClass().getName() );

	protected AbstractPerformer(ITest<D,R> node ) {
		this( node, 1);
	}

	protected AbstractPerformer( ITest<D,R> node, int maxCount ) {
		this.node = node;
		this.index = 0;
		this.maxCount = maxCount;
		this.skip = false;
		this.stop = false;
		this.listeners = new ArrayList<>();
	}

	protected String getTestId() {
		return node.getId();
	}

	protected ITestNode<D,R> getNode() {
		return node;
	}

	@Override
	public void setNode(ITest<D, R> node) {
		this.node = node;
	}

	protected ITestEvent<D,R> getEvent() {
		return node.getEvent();
	}

	@Override
	public void clear() {
		this.index = 0;
	}

	protected int getMaxCount() {
		return maxCount;
	}

	protected void setMaxCount(int maxCount) {
		this.maxCount = maxCount;
	}

	protected int getIndex() {
		return index;
	}

	@Override
	public void addListener( ITestListener<D,R> listener ) {
		this.listeners.add( listener);
	}

	@Override
	public void removeListener( ITestListener<D,R> listener) {
		this.listeners.remove(listener);
	}

	/**
	 * If true, the test can be skipped
	 * @return
	 */
	protected boolean isSkip() {
		return skip;
	}

	protected void setSkip(boolean skip) {
		this.skip = skip;
	}

	protected boolean hasStopped() {
		return stop;
	}

	void notifyTestEvent( ITestEvent<D,R> event){
		for( ITestListener<D,R> listener: this.listeners ) {
			listener.notifyTestEvent(event);
		}
	}

	@Override
	public void init( ITestEvent<D,R> event ) {
		String maxcount_str = node.getAttribute( ITest.AttributeNames.MAX_COUNT);
		if( !StringUtils.isEmpty( maxcount_str))
			this.maxCount = Integer.parseInt(maxcount_str);
	}

	@Override
	public String getAttribute(Enum<?> enm) {
		ITestEvent<D,R> event = node.getEvent();
		String result = event.getAttribute(enm);
		return !StringUtils.isEmpty(result )? result: node.getAttribute(enm);
	}

	protected void putAttribute( Enum<?> enm, String value ) {
		ITestEvent<D,R> event = node.getEvent();
		event.getAttributes().put( enm.name(), value);
	}

	protected Collection<String> getArguments(){
		ITestEvent<D,R> event = node.getEvent();
		return event.getArguments();
	}

	protected StringBuilder createBuilder() {
		builder = new StringBuilder();
		builder.append("\n");//under logger info
		return builder;
	}

	protected void startTest( String name ) {
		builder.append("Testing " + name + ": ");
	}

	protected void startTest( int index ) {
		builder.append("Testing " + index + ": ");
	}

	protected void append( String text ) {
		builder.append( text );
	}

	protected void append( int text ) {
		builder.append( text );
	}

	protected void appendlf( String text ) {
		builder.append( text );
		lf();
	}

	protected void append( boolean choice ) {
		builder.append( choice );
	}

	protected void lf() {
		builder.append("\n");
	}

	protected void tab( String text) {
		builder.append("\t" + text);
	}

	protected void tablf( String text) {
		builder.append("\t" + text + "\n");
	}

	protected void tablf( double text) {
		builder.append("\t" + text + "\n");
	}

	protected void complete( boolean result ) {
		Results complete = result? Results.SUCCESS: Results.FAIL;
		builder.append(complete);
		builder.append("\n");
		if( result )
			logger.info(builder.toString());
		else
			logger.warning( builder.toString());
	}

	/**
	 * allow specific preparation for the test. Returns the EXPECTED result
	 * of the test
	 * @param event
	 * @param index
	 * @return
	 */
	protected abstract R onPrepare( ITestEvent<D,R> event, int index );

	@Override
	public void prepare( ITestEvent<D,R> event) {
		if( this.stop )
			return;
		event.setTestEvent( TestEvents.PREPARE);
	}

	/**
	 * Perform the event. If a true is returned, then the performer
	 * of the parent is also performed
	 * @param event
	 * @return
	 */
	protected abstract void onPerform( ITestEvent<D,R> event, int index );

	@Override
	public void perform( ITestEvent<D,R> event ) {
		if( this.stop )
			return;
		event.setTestEvent( TestEvents.PERFORM);
		index = 0;
		while( hasNext() ) {
			next( event );
		}
	}

	/**
	 * complete the test. Returns the expected result of the test
	 * @param event
	 * @param index
	 * @return
	 */
	protected abstract R onComplete( ITestEvent<D,R> event, int index);

	@Override
	public boolean complete( ITestEvent<D,R> event) {
		if( this.stop )
			return false;
		event.setTestEvent( TestEvents.COMPLETE);
		boolean result = event.isPass();
		return result;
	}

	protected boolean hasNext() {
		return (!this.stop && ( this.index < this.maxCount ));
	}

	protected D next( ITestEvent<D,R> event ) {
		event.setTestEvent( TestEvents.PREPARE);
		notifyTestEvent( event);
		R expected = this.onPrepare( event, index );

		if( !skip ) {
			event.setExpected(expected);
			event.setTestEvent( TestEvents.PERFORM);
			this.onPerform( event, index );
			notifyTestEvent( event);
			event.setTestEvent( TestEvents.COMPLETE);
			R result = this.onComplete( event, index );
			this.stop = !expected.equals( result );
		}
		notifyTestEvent( event);
		this.index++;
		return node.getData();
	}

	@Override
	public boolean isFinished() {
		return this.index > this.maxCount;
	}

	/**
	 * allow for overriding the pass/ fail situation
	 * @param event
	 * @return
	 */
	protected boolean onPostProcess( ITestEvent<D,R> event ) {
		return !this.stop && event.isPass();
	}

	@Override
	public void postProcess( ITestEvent<D,R> event ) {
		boolean pass = onPostProcess(event);
		event.setPass(pass);
		if(( node.getParent() == null ) || !( node.getParent() instanceof ITest )) {
			this.stop = !pass;
			return;
		}
		ITest<D,R> test = (ITest<D,R>) node.getParent();
		test.getPerformer().postProcess(event);
		this.stop = !event.isPass();
	}
}