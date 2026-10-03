package org.condast.commons.jpa.test.core;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import org.condast.commons.Utils;
import org.condast.commons.strings.StringStyler;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.xml.BuildEvent;
import org.xml.sax.Attributes;

public abstract class AbstractTestNode<D,R extends Object> implements ITestNode<D,R> {

	private String id;
	private D data;
	private Map<String, String> attributes;

	private ITestNode<D,R> parent;

	private Collection<ITestNode<D,R>> children;

	private ITestEvent<D,R> event;

	private Collection<ITestListener<D,R>> listeners;

	protected AbstractTestNode( String id, Attributes attrs ) {
		this( id, attrs, null );
	}

	protected AbstractTestNode( String id, Attributes attrs, D data ) {
		this.id = id;
		this.data = data;
		this.attributes = BuildEvent.getParameters(attrs);
		this.children = new ArrayList<>();
		this.listeners = new ArrayList<>();
	}

	@Override
	public void init( Attributes attributes ) {
		boolean inherit = ( parent != null ) && ( parent instanceof ITest);
		if( !inherit )
			this.event = createEvent();
		else {
			this.event = parent.getEvent();
			Iterator<Map.Entry<String, String>> iterator = getAttributes().entrySet().iterator();
			while( iterator.hasNext() ) {
				Map.Entry<String, String> entry = iterator.next();
				this.event.getAttributes().put(entry.getKey(), entry.getValue());
			}
		}
	}

	@Override
	public void addListener( ITestListener<D,R> listener ) {
		this.listeners.add( listener);
	}

	@Override
	public void removeListener( ITestListener<D,R> listener) {
		this.listeners.remove(listener);
	}

	protected void notifyTestEvent( ITestEvent<D,R> event){
		for( ITestListener<D,R> listener: this.listeners ) {
			listener.notifyTestEvent(event);
		}
	}

	@Override
	public String getId() {
		return id;
	}

	@Override
	public ITestEvent<D,R> getEvent() {
		return event;
	}

	@Override
	public D getData() {
		return data;
	}

	protected void setData(D data) {
		this.data = data;
	}

	protected void init( ITestEvent<D,R> event ) { /* DEFAULT NOTHING */ }

	/**
	 * Get the attribute value for the given enum
	 * @param attributes
	 * @param enm
	 * @return
	 */
	@Override
	public String getAttribute( Enum<?> enm ){
		String result = null;
		if( !Utils.assertNull( attributes )) {
			String str = StringStyler.xmlStyleString( enm.name() );
			result = attributes.get( str);
		}
		return !StringUtils.isEmpty(result)? result: ( parent == null )?null: parent.getAttribute(enm);

	}

	protected Map<String, String> getAttributes() {
		return attributes;
	}

	@Override
	public ITestNode<D,R> getParent(){
		return parent;
	}

	@Override
	public void setParent(ITestNode<D,R> parent) {
		this.parent = parent;
	}

	@Override
	public void addChild( ITestNode<D,R> child ) {
		this.children.add(child);
		child.setParent(this);
	}

	@Override
	public void removeChild( ITestNode<D,R> child ) {
		this.children.remove(child);
		child.setParent(null);
	}

	protected Collection<ITestNode<D,R>> getChildren() {
		return this.children;
	}

	protected abstract void onPrepare( ITestEvent<D,R> event );

	@Override
	public void prepare( ) {
		if( event == null )
			return;
		event.setTest(this.getId());
		ITestEvent.TestEvents testEvent = ITestEvent.TestEvents.PREPARE;
		event.setTestEvent( testEvent);
		onPrepare(event);
		notifyTestEvent(event);
	}

	protected abstract void onPerform( ITestEvent<D,R> event );

	@Override
	public void perform() {
		ITestEvent.TestEvents testEvent = ITestEvent.TestEvents.PERFORM;
		event.setTestEvent( testEvent);
		onPerform(event);
		notifyTestEvent(event);
	}

	@Override
	public void complete() {
		ITestEvent.TestEvents testEvent = ITestEvent.TestEvents.PERFORM;
		event.setTestEvent( testEvent);
		event.setTest(parent.getId());
	}

	protected ITestEvent<D,R> createEvent(){
		String name = getAttribute( ITest.AttributeNames.NAME );
		String type = getAttribute( ITest.AttributeNames.TYPE );
		BuildEvent<D> be = new BuildEvent<>(this, getId(), name, type, getAttributes(), getData());
		TestEvent<D,R> test = new TestEvent<>( be );
		return test;
	}

	@Override
	public String toString() {
		return this.getId();
	}
}