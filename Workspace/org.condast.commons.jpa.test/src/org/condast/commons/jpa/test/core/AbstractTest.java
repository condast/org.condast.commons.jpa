package org.condast.commons.jpa.test.core;

import org.condast.commons.Utils;
import org.condast.commons.jpa.test.performer.AbstractPerformer;
import org.condast.commons.jpa.test.performer.IPerformer;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public abstract class AbstractTest<D,R extends Object> extends AbstractTestNode<D,R> implements ITest<D,R>{

	private boolean active;
	private StringBuffer buffer;

	//optionally an object can be included that is created by the 'class' argument
	private Object created;

	/**
	 * Get the expcted outcome and the actual result of the test
	 */
	private R expected;
	private R result;

	private IPerformer<D,R> performer;

	private AbstractTest<D,R> test;

	protected AbstractTest(Object arg0, String id, String name, Attributes attributes ) {
		this( arg0, id, name, 0, attributes, null );
	}

	protected AbstractTest(Object arg0, String id, String name, int index, Attributes attributes ) {
		this( arg0, id, name, index, attributes, null );
	}

	protected AbstractTest(Object arg0, String id, String name, int index, Attributes attributes, D data ) {
		this( arg0, id, name, String.valueOf( index ), attributes, data, false );
	}

	protected AbstractTest(Object arg0, String id, String name, String phase, Attributes attributes, D data, boolean createperformer ) {
		super(id, attributes, data);
		this.test = this;
		this.buffer = new StringBuffer();
		String active_str = getAttribute(ITest.AttributeNames.ACTIVE);
		this.active = StringUtils.isEmpty( active_str)?true: Boolean.parseBoolean( active_str );
		if( createperformer)
			this.performer = new Performer(this, 1);
	}

	public void clearBuffer(){
		this.buffer = new StringBuffer();
	}

	@Override
	public boolean isActive() {
		return active;
	}

	@Override
	public void setActive(boolean active) {
		this.active = active;
		for( ITestNode<D,R> child: super.getChildren()) {
			if(!( child instanceof ITest))
				continue;
			ITest<D,R> node = (ITest<D,R>) child;
			node.setActive(active);
		}
	}

	/**
	 * Get the context of this test. Mainly used for HTTP tests
	 * @return
	 */
	@Override
	public String getContext() {
		return this.getAttribute( ITest.AttributeNames.CONTEXT);
	}


	@Override
	public ITestEvent<D,R> getTestEvent() {
		return super.getEvent();
	}

	@Override
	public void setData(D data) {
		super.setData(data);
	}

	/**
	 * Optionally an object can be included that is created by the 'class' argument
	 * @return
	 */
	public Object getCreated() {
		return created;
	}

	public void setCreated(Object created) {
		this.created = created;
	}

	@Override
	public IPerformer<D,R> getPerformer() {
		return this.performer;
	}

	@Override
	public void setPerformer(IPerformer<D,R> performer) {
		if( performer != null ) {
			this.performer = performer;
		}else if(this.getParent() instanceof ITest ) {
			ITest<D,R> test=  (ITest<D, R>) getParent();
			this.performer = test.getPerformer();
		}else
			performer = new Performer( test, 1);
	}

	public R getExpected() {
		return expected;
	}

	public void setExpected(R expected) {
		this.expected = expected;
	}

	public R getResult() {
		return result;
	}

	public void setResult(R result) {
		this.result = result;
	}


	public StringBuffer getBuffer() {
		return buffer;
	}

	@Override
	public void onPrepare( ITestEvent<D,R> event ) {
		event.setTest(this.getId());
		this.performer = getPerformer(this);
		ITestEvent.TestEvents testEvent = ITestEvent.TestEvents.PREPARE;
		event.setTestEvent( testEvent);
		if( this.performer != null )
			performer.prepare(event);
	}

	protected IPerformer<D,R> getPerformer( ITestNode<D,R> node ){
		if( !( node instanceof ITest<?,?>))
			return null;
		ITest<D,R> test = (ITest<D,R>) node;
		IPerformer<D,R> performer = test.getPerformer();
		return ( performer != null )? performer: getPerformer( node.getParent() );
	}

	@Override
	public void onPerform( ITestEvent<D,R> event ) {
		ITestEvent.TestEvents testEvent = ITestEvent.TestEvents.PERFORM;
		event.setTestEvent( testEvent);
		ITestListener<D,R> listener = new TestListener();
		if( this.performer != null ) {
			this.performer.init( event );
		}
		performer.perform( event );
		testEvent = ITestEvent.TestEvents.COMPLETE;
		event.setTestEvent( testEvent);
		performer.complete( event );
		performer.removeListener(listener);
		event.setTest(this.getParent().getId());
		notifyTestEvent(event);
	}

	@Override
	public void perform() {
		//do not perform in case of children
		if( !Utils.assertNull( this.getChildren()))
			return;
		super.perform();
		performer.postProcess( super.getEvent());
	}

	@Override
	public boolean isPass() {
		return ( super.getEvent() == null )? false: super.getEvent().isPass();
	}

	/**
	 * The actual test is carried out by the performer, which supplies
	 * methods for the test events INIT, PREPARE, PERFORM, COMPLETE, POST_PROCESS
	 * The default implementation does nothing
	 * @author Kees
	 *
	 */
	private class Performer extends AbstractPerformer<D,R>{

		protected Performer(ITest<D,R> node, int maxCount) {
			super( node, maxCount);
		}

		@Override
		protected R onPrepare(ITestEvent<D,R> event, int index) {
			if( super.getNode().getParent() instanceof ITest ) {
				ITest<D,R> parent = (ITest<D, R>) super.getNode().getParent();
				parent.getPerformer().prepare( event );
			}

			return event.getExpected();
		}

		@Override
		protected void onPerform(ITestEvent<D,R> event, int index ) {/* NOTHING */}

		@Override
		protected R onComplete(ITestEvent<D,R> event, int index ) {
			return event.getResult();
		}

		@Override
		protected boolean onPostProcess(ITestEvent<D, R> event) {
			return event.isPass();
		}
	}

	private class TestListener implements ITestListener<D,R>{

		@Override
		public void init(Attributes attributes) {
			// NOTHING
		}

		@Override
		public void notifyTestEvent(ITestEvent<D, R> event) {
			test.notifyTestEvent(event);
		}

	}

}