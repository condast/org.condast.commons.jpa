package org.condast.commons.jpa.test.core;

import org.condast.commons.strings.StringStyler;
import org.xml.sax.Attributes;

public abstract class AbstractTestListener<D,R extends Object> implements ITestListener<D,R>{

	private ITestEvent<D,R> event;
	private ITestEvent.TestEvents tevent;

	private D data;

	protected AbstractTestListener( ITestEvent.TestEvents tevent) {
		this.tevent = tevent;
	}

	@Override
	public void init(Attributes attributes) {
		// NOTHING
	}

	protected D getData() {
		return data;
	}

	protected void setData(D data) {
		this.data = data;
	}

	/**
	 * Handle the test event. Returns true if all went well
	 * @param event
	 * @return
	 */
	protected abstract boolean onHandleTestEvent(ITestEvent<D,R> event);

	protected void setPass( boolean choice ){
		if( event.isPass())
			event.setPass( choice );
	}

	/**
	 * returns true if the given event is acceptable for further handling by this listener
	 * @param event
	 * @return
	 */
	protected boolean accept( ITestEvent<D,R> event ) {
		return ITestEvent.TestEvents.ANY.equals( tevent ) || ITestEvent.TestEvents.ANY.equals( event.getTestEvent() ) ||
				tevent.equals( event.getTestEvent());
	}

	@Override
	public void notifyTestEvent( ITestEvent<D,R> event) {
		if( !accept ( event ))
			return;
		this.event = event;
		this.event.setPass(true);
		boolean pass = this.onHandleTestEvent(event);
		if( !event.isPass())
			return;
		this.event.setPass(pass);
	}

	/**
	 * Get the attribute value for the given enum
	 * @param attributes
	 * @param enm
	 * @return
	 */
	protected static String getAttribute( Attributes attributes, String key ){
		return attributes.getValue( StringStyler.xmlStyleString( key) );
	}
}
