package org.condast.commons.jpa.test;

import org.condast.commons.jpa.test.core.Test;
import org.condast.commons.jpa.test.xml.ITestReasoner;
import org.xml.sax.Attributes;

public abstract class AbstractTestReasoner<D,R extends Object> implements ITestReasoner<D, R> {

	private int maxCount;
	private XMLFactoryBuilder<D,R> builder;

	public AbstractTestReasoner( XMLFactoryBuilder<D,R> builder) {
		this( builder, Integer.MAX_VALUE );
	}

	public AbstractTestReasoner( XMLFactoryBuilder<D,R> builder, int maxCount ) {
		this.maxCount = maxCount;
		this.builder = builder;
	}

	/**
	 * First transform the counter to values that can be used for reasoning
	 * @param counter
	 * @return
	 */
	protected abstract R onTransformCounter( int counter );

	/**
	 * Then apply the transformed values on the data
	 * @param values
	 * @param data
	 */
	protected abstract void reason( R values, D data );

	/* (non-Javadoc)
	 * @see org.condast.commons.test.xml.ITestReasoner#performTest(java.lang.String, java.lang.String, org.xml.sax.Attributes, D)
	 */
	@Override
	public void performTest( String id, String name, Attributes attributes, D data ) {
		for( int i=0; i<this.maxCount; i++ ) {
			R transform = onTransformCounter(i);
			reason( transform, data);
			Test<D,Object> event = new Test<>( builder, id, name, i, attributes );
			if( event.isPass() )
				return;
		}
	}

}
