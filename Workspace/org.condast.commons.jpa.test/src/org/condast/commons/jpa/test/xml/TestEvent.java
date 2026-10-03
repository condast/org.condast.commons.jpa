package org.condast.commons.jpa.test.xml;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.jpa.test.performer.IPerformer;
import org.condast.commons.jpa.test.xml.ITestListener.AttributeNames;
import org.condast.commons.jpa.test.xml.ITestListener.TestEvents;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.xml.BuildEvent;
import org.xml.sax.Attributes;

public class TestEvent<D,A extends Object> extends BuildEvent<D> {
	private static final long serialVersionUID = 1L;

	private TestEvents event;
	private int index;
	private boolean active, pass;
	private StringBuffer buffer;
	private Collection<A> args;

	private IPerformer<D,A> performer;

	public TestEvent(Object arg0, String id, String name, int index, Attributes attributes ) {
		this( arg0, id, name, TestEvents.PREPARE, index, attributes, null );
	}

	public TestEvent(Object arg0, String id, String name, int index, Attributes attributes, D data ) {
		this( arg0, id, name, TestEvents.PERFORM, index, attributes, data );
	}

	public TestEvent(Object arg0, String id, String name, TestEvents event, Attributes attributes ) {
		this( arg0, id, name, event, 0, attributes, null );
	}

	public TestEvent(Object arg0, String id, String name, TestEvents event, int index, Attributes attributes, D data ) {
		super(arg0, id, name, event.name(), attributes, data);
		this.event = event;
		this.index = index;
		this.pass = false;
		this.buffer = new StringBuffer();
		String active_str = getAttribute(AttributeNames.ACTIVE);
		this.active = StringUtils.isEmpty( active_str)?true: Boolean.parseBoolean( active_str );
		this.args = new ArrayList<>();
	}

	public TestEvents getTestEvent() {
		return event;
	}

	public void clearBuffer(){
		this.buffer = new StringBuffer();
	}

	public boolean isActive() {
		return active;
	}

	public int getIndex() {
		return index;
	}

	void increase(){
		this.index++;
	}

	void setEvent(TestEvents event) {
		super.setEvent( event.name());
		this.event = event;
	}

	@Override
	public void setData(D data) {
		super.setData(data);
	}

	public IPerformer<D,A> getPerformer() {
		return performer;
	}

	public void setPerformer( Attributes attributes ) {
		this.performer = null;// new ConditionalPerformer<>(this, attributes);
	}

	public StringBuffer getBuffer() {
		return buffer;
	}

	public boolean isPass() {
		return pass;
	}

	public void setPass(boolean pass) {
		this.pass = pass;
	}

	public void addArgument( A arg ) {
		this.args.add(arg);
	}

	public void removeArgument( A arg ) {
		this.args.remove(arg);
	}

	public Collection<A> getArguments() {
		return this.args;
	}


	public static boolean isActive( TestEvent<?,?> event ) {
		if( event == null )
			return false;
		return event.isActive();
	}
}