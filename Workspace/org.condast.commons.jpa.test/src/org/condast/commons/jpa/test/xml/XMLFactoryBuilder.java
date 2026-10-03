/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.condast.commons.jpa.test.xml;

import java.io.IOException;
import java.lang.reflect.Constructor;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.logging.Logger;

import org.condast.commons.jpa.test.HttpPerformer;
import org.condast.commons.jpa.test.core.ITest;
import org.condast.commons.jpa.test.core.ITestEvent;
import org.condast.commons.jpa.test.core.ITestListener;
import org.condast.commons.jpa.test.core.ITestNode;
import org.condast.commons.jpa.test.core.ITestSuite;
import org.condast.commons.jpa.test.core.Test;
import org.condast.commons.jpa.test.core.ITest.TestNodes;
import org.condast.commons.jpa.test.performer.IPerformer;
import org.condast.commons.preferences.xml.AbstractXMLBuilder;
import org.condast.commons.preferences.xml.AbstractXmlHandler;
import org.condast.commons.strings.StringUtils;
import org.xml.sax.Attributes;

public class XMLFactoryBuilder<D,R extends Object> extends AbstractXMLBuilder<ITestSuite<D,R>,  ITest.TestNodes> {

	public static final String S_WRN_NO_DATA_PROVIDED = "The test event does not contain data for test: ";

	private Collection<ITestListener<D,R>> listeners;

	@SuppressWarnings("rawtypes")
	private static XMLFactoryBuilder builder;

	public XMLFactoryBuilder( Class<?> clss, String resource ) {
		super( new XMLHandler<D,R>( clss ), clss.getResourceAsStream( resource ));
		this.listeners = new ArrayList<>();
		XMLHandler<D,R> handler = (XMLHandler<D,R>) getHandler();
		handler.setListeners(this.listeners);
		builder = this;
	}

	/**
	 * Build the factories from the given resource in the class file and add them to the container
	 * @param bundleId
	 * @param clss
	 * @param location
	 * @param builder
	 * @throws IOException
	 */
	public XMLFactoryBuilder( Class<?> clss, URL url ) throws IOException {
		super( new XMLHandler<D,R>( clss ), url.openStream());
		this.listeners = new ArrayList<>();
		XMLHandler<D,R> handler = (XMLHandler<D,R>) getHandler();
		handler.setListeners(this.listeners);
		builder = this;
	}

	public void addListener( ITestListener<D,R> listener ){
		this.listeners.add( listener);
	}

	public void removeListener( ITestListener<D,R> listener ){
		this.listeners.remove( listener);
	}

	public void notifyListeners( ITestEvent<D,R> event ){
		for( ITestListener<D,R> listener: this.listeners )
			listener.notifyTestEvent(  event );
	}

	public ITestSuite<D, R> getSuite() {
		XMLHandler<D,R> handler = (XMLHandler<D,R>) getHandler();
		return handler.getSuite();
	}

	@Override
	public ITestSuite<D,R>[] getUnits() {
		return getHandler().getUnits();
	}

	public static String getLocation( String defaultLocation ){
		if( !StringUtils.isEmpty( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}

	private static class XMLHandler<D,R extends Object> extends AbstractXmlHandler<ITestSuite<D,R>, ITest.TestNodes>{

		private boolean greedy;
		private boolean stop;
		private ITestSuite<D,R> suite;

		private ITestNode<D,R> root;

		private Collection<ITestListener<D,R>> listeners;

		private Logger logger = Logger.getLogger( this.getClass().getName());

		private ITestListener<D,R> listener = new ITestListener<D,R>() {

			@Override
			public void init(Attributes attributes) {
				for( ITestListener<D,R> listener: listeners )
					listener.init(attributes);
			}

			@Override
			public void notifyTestEvent(ITestEvent<D,R> event) {
				for( ITestListener<D,R> listener: listeners )
					listener.notifyTestEvent(event);
			}
		};

		private XMLHandler( Class<?> clss ) {
			super( clss, EnumSet.allOf( ITest.TestNodes.class));
		}

		ITestSuite<D, R> getSuite() {
			return suite;
		}

		private void setListeners(Collection<ITestListener<D,R>> listeners) {
			this.listeners = listeners;
		}

		@Override
		protected ITestSuite<D,R> parseNode( ITest.TestNodes node, Attributes attributes) {
			if( stop )
				return suite;
			String active_str = getAttribute( attributes, ITest.AttributeNames.ACTIVE );
			boolean active = StringUtils.isEmpty(active_str)? true: Boolean.parseBoolean( active_str);
			String id   = getAttribute( attributes, ITest.AttributeNames.ID );
			String name = getAttribute( attributes, ITest.AttributeNames.NAME );
			String class_str = getAttribute( attributes, ITest.AttributeNames.CLASS );
			String perform_str = getAttribute( attributes, ITest.AttributeNames.PERFORMER );
			String maxcount_str = getAttribute( attributes, ITest.AttributeNames.MAX_COUNT );
			int maxcount = StringUtils.isEmpty(maxcount_str)?1: Integer.parseInt( maxcount_str);
			//String data_str = getAttribute( attributes, ITestListener.AttributeNames.DATA );
			//String file_str = getAttribute( attributes, ITestListener.AttributeNames.FILE );
			//String count_str = getAttribute( attributes, ITestListener.AttributeNames.COUNT );
			Test<D,R>test = null;
			switch( node ){
			case TEST_SUITE:
				String greedy_str = getAttribute( attributes, ITest.AttributeNames.GREEDY );
				greedy = StringUtils.isEmpty(greedy_str)? true: Boolean.parseBoolean( greedy_str);
				suite = new TestSuite<>( id, name, attributes, active );
				root = suite;
				if( !active )
					return suite;
				root.addListener( listener);
				break;
			case INIT:
				if( StringUtils.isEmpty( perform_str ))
					break;
				IPerformer<D,R> performer = createPerformer(getHandlerClass(), perform_str, test, maxcount);
				if( performer != null )
					suite.setPerformer( performer );
				break;
			case LISTENER:
			case PREPARE:
			case PERFORM:
			case COMPLETE:
				if(!StringUtils.isEmpty( class_str)) {
					ITestListener<D,R> plistener = createListener( getHandlerClass(), class_str );
					this.listeners.add( plistener);
					plistener.init( attributes );
				}
				break;
			case TEST:
			case SUB_TEST:
			case HTTP_TEST:
				D data = ( root == null )? null: root.getData();
				test = new Test<>( builder, id, name, node, attributes, data );
				test.init(attributes);
				root.addChild(test);
				root = test;
				if( !active )
					break;
				if( !StringUtils.isEmpty(class_str))
					test.setCreated( super.createObject( getHandlerClass(), class_str));
				root.addListener( listener);
				performer = getPerformer(test, id, perform_str, maxcount);
				test.setPerformer( performer);
				test.prepare();
				break;
			case REASONER:
				class_str = getAttribute( attributes, ITest.AttributeNames.CLASS );
				if( !StringUtils.isEmpty(class_str)) {
					//ITestReasoner<D,?> listener = (ITestReasoner<D, ?>) createObject( clss, class_str);
				}
				break;
			default:
				break;
			}
			return suite;
		}

		@Override
		protected void completeNode(Enum<ITest.TestNodes> node) {
			switch( ITest.TestNodes.valueOf( node.name()) ){
			case TEST:
			case SUB_TEST:
			case HTTP_TEST:
				//perform the test
				if( root instanceof TestSuite)
					break;
				ITest<D,R> test = (ITest<D,R>) root;
				root = root.getParent();
				if( !test.isActive() )
					break;
				if( test.getEvent().getData() == null )
					logger.warning( S_WRN_NO_DATA_PROVIDED + test.getId() + "\n\n");
				test.perform();
				if( greedy && !test.isPass()) {
					logger.severe("TEST FAILED " + test.getId() + ": Stopping suite");
					System.exit(0);
				}
				break;
			default:
				break;
			}
		}

		/**
		 * Get the performer
		 * @param test
		 * @param id
		 * @param perform_str
		 * @param maxcount
		 * @return
		 */
		protected IPerformer<D,R> getPerformer( ITest<D,R> test, String id, String perform_str, int maxcount ){
			IPerformer<D,R> performer = suite.getPerformer();
			if( performer != null )
				performer.setNode(test);
			if( !StringUtils.isEmpty(perform_str))
				performer = createPerformer(super.getHandlerClass(), perform_str, test, maxcount );
			else if( TestNodes.HTTP_TEST.equals( test.getTestNode() ))
				performer = new HttpPerformer<>(super.getHandlerClass(), id, test );
			return performer;
		}

		@Override
		protected void addValue(Enum<ITest.TestNodes> node, String value) {
			// NOTHING
		}

		@SuppressWarnings("unchecked")
		@Override
		public ITestSuite<D,R>[] getUnits() {
			Collection<ITestSuite<D,R>> results = super.getResults();
			return results.toArray( new ITestSuite[ results.size() ]);
		}

		@Override
		public ITestSuite<D,R> getUnit(String id) {
			for( ITestSuite<D,R> suite: super.getResults()){
				if( suite.getId().equals(id))
					return suite;
			}
			return null;
		}

		@SuppressWarnings("unchecked")
		protected ITestListener<D,R> createListener( Class<?> clss, String className){
			Class<ITestListener<D,R>> builderClass;
			ITestListener<D,R> builder = null;
			try {
				builderClass = (Class<ITestListener<D,R>>) clss.getClassLoader().loadClass( className );
				Constructor<?> constructor = builderClass.getConstructor();
				builder = (ITestListener<D,R>) constructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return builder;
		}

		@SuppressWarnings("unchecked")
		protected IPerformer<D,R> createPerformer( Class<?> clss, String className, ITest<?,?> event, int count ){
			Class<IPerformer<D,R>> builderClass;
			IPerformer<D,R> performer = null;
			try {
				builderClass = (Class<IPerformer<D,R>>) clss.getClassLoader().loadClass( className );
				Constructor<?> constructor = builderClass.getConstructor( ITest.class, Integer.TYPE );
				performer = (IPerformer<D,R>) constructor.newInstance( event, count );
			} catch (Exception e) {
				e.printStackTrace();
			}
			return performer;
		}
	}
}