/*******************************************************************************
 * Copyright (c) 2014 Chaupal.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Apache License, Version 2.0
 * which accompanies this distribution, and is available at
 * http://www.apache.org/licenses/LICENSE-2.0.html
 *******************************************************************************/
package org.condast.commons.jpa.authentication.ui.entry;

import java.lang.reflect.Constructor;
import java.util.Collection;
import java.util.EnumSet;
import java.util.HashMap;
import java.util.Map;

import org.condast.commons.jpa.authentication.session.DefaultSessionStore;
import org.condast.commons.preferences.xml.AbstractXMLBuilder;
import org.condast.commons.preferences.xml.AbstractXmlHandler;
import org.condast.commons.strings.StringUtils;
import org.condast.commons.ui.widgets.entry.IDataEntryPoint;
import org.condast.commons.ui.widgets.entry.IEntryNode;
import org.condast.commons.ui.widgets.entry.IEntryPointEvent;
import org.condast.commons.xml.BuildEvent;
import org.eclipse.rap.rwt.application.Application;
import org.eclipse.rap.rwt.application.EntryPoint;
import org.eclipse.rap.rwt.application.EntryPointFactory;
import org.eclipse.rap.rwt.client.WebClient;
import org.xml.sax.Attributes;

public class EntryFactoryBuilder<D extends Object> extends AbstractXMLBuilder<EntryFactoryBuilder.EntryNode<D>,  IEntryNode.Entries> {

	public static final String S_WRN_NO_DATA_PROVIDED = "The test event does not contain data for test: ";

	public static final String S_DEFAULT_RESOURCE = "/ENTRY-INF/entries.xml";

	private Application application;

	private DefaultSessionStore<D> defstore;
	private Class<?> clss;
	
	public EntryFactoryBuilder( Application application, Class<?> clss ) {
		this( application,  clss, S_DEFAULT_RESOURCE);
	}

	public EntryFactoryBuilder( Application application, Class<?> clss, String resource ) {
		super( new XMLHandler<D>( clss ), clss.getResourceAsStream( resource ));
		this.application = application;
		this.clss = clss;
	}

	@Override
	public EntryFactoryBuilder.EntryNode<D>[] getUnits() {
		return getHandler().getUnits();
	}

	public static String getLocation( String defaultLocation ){
		if( !StringUtils.isEmpty( defaultLocation ))
			return defaultLocation;
		return defaultLocation;
	}

	@Override
	public void build(  ) {
		super.addListener( e-> onBuildEvent( e ));
		super.build();
		super.removeListener( e-> onBuildEvent( e ));
	}

	private Object onBuildEvent(BuildEvent<EntryFactoryBuilder.EntryNode<D>> e) {
		EntryFactoryBuilder.EntryNode<D> entry = e.getData();
		if(( entry == null ) || IEntryPointEvent.EntryPointEvents.COMPLETE.name().equals( e.getEvent()))
			return null;
		switch( entry.node) {
		case ENTRIES:
			if( !StringUtils.isEmpty(entry.theme))
				application.addStyleSheet( entry.theme, entry.stylesheet );
			break;
		case ENTRY:
			DefaultEntryPointFactory factory = new DefaultEntryPointFactory(entry, defstore);
			application.addEntryPoint( entry.page, factory, entry.properties);
			break;
		default:
			break;
		}
		return null;
	}

	private static class XMLHandler<D extends Object> extends AbstractXmlHandler<EntryFactoryBuilder.EntryNode<D>, IEntryNode.Entries>{

		private String path;

		private String theme;

		private DefaultSessionStore<D> defstore;

		private XMLHandler( Class<?> clss ) {
			super( clss, EnumSet.allOf( IEntryNode.Entries.class));
		}

		@SuppressWarnings("unchecked")
		@Override
		protected EntryFactoryBuilder.EntryNode<D> parseNode( IEntryNode.Entries entryNode, Attributes attributes) {
			String id   = getAttribute( attributes, IEntryNode.AttributeNames.ID );
			String class_str = getAttribute( attributes, IEntryNode.AttributeNames.CLASS );
			String path_str = getAttribute( attributes, IEntryNode.AttributeNames.PATH );
			String page = getAttribute( attributes, IEntryNode.AttributeNames.PAGE);
			String title = getAttribute( attributes, IEntryNode.AttributeNames.TITLE );
			String theme_str = getAttribute( attributes, IEntryNode.AttributeNames.THEME );
			String stylesheet = getAttribute( attributes, IEntryNode.AttributeNames.STYLESHEET );
			EntryFactoryBuilder.EntryNode<D> node = new EntryNode<D>( entryNode, id, page, path, class_str);
			switch( entryNode ){
			case ENTRIES:
				String store = getAttribute( attributes, IEntryNode.AttributeNames.STORE );
				if( !StringUtils.isEmpty(store))
					defstore = (DefaultSessionStore<D>) createObject(super.getHandlerClass(), store);

				node.stylesheet = stylesheet;
				node.store = defstore;
				if( !StringUtils.isEmpty(path_str))
					this.path = path_str;
				if( !StringUtils.isEmpty(theme_str)) {
					this.theme = theme_str;
					node.theme = theme;
				}
				else

				break;
			case ENTRY:
				node.setTheme(theme_str);
				if( !StringUtils.isEmpty(title))
					node.setProperty(WebClient.PAGE_TITLE, title);
				if( !StringUtils.isEmpty(theme_str)) {
					node.setProperty(WebClient.THEME_ID, theme_str);
				}else if( !StringUtils.isEmpty(theme)) {
					node.setProperty(WebClient.THEME_ID, theme);
				}
				String enableStoreStr = getAttribute( attributes, IEntryNode.AttributeNames.ENABLE_STORE );
				boolean enableStore = StringUtils.isEmpty(enableStoreStr)?true: Boolean.parseBoolean(enableStoreStr);
				if(( this.defstore != null ) && (enableStore))
					node.store = this.defstore;
				break;
			case TIMER:
			default:
				break;
			}
			return node;
		}

		@Override
		protected void completeNode(Enum<IEntryNode.Entries> node) {
			switch( IEntryNode.Entries.valueOf( node.name()) ){
			case ENTRY:
			case TIMER:
				//perform the test
				break;
			default:
				break;
			}
		}

		@Override
		protected void addValue(Enum<IEntryNode.Entries> node, String value) {
			// NOTHING
		}

		@SuppressWarnings("unchecked")
		@Override
		public EntryFactoryBuilder.EntryNode<D>[] getUnits() {
			Collection<EntryFactoryBuilder.EntryNode<D>> results = super.getResults();
			return results.toArray( new EntryFactoryBuilder.EntryNode[ results.size() ]);
		}

		@Override
		public EntryFactoryBuilder.EntryNode<D> getUnit(String id) {
			if( StringUtils.isEmpty(id))
				return null;
			for( EntryFactoryBuilder.EntryNode<D> node: super.getResults()){
				if( id.equals(node.id))
					return node;
			}
			return null;
		}
	}

	protected static class EntryNode<D extends Object>{

		private IEntryNode.Entries node;
		private String id;
		private String path;
		private String page;
		private String clss;
		private String theme;
		private String stylesheet;
		private DefaultSessionStore<D> store;

		private Map<String, String> properties;

		public EntryNode(IEntryNode.Entries node, String id, String page, String path, String clss) {
			this.id = id;
			this.node = node;
			this.page = (page == null )?null: page.startsWith("/")? page: "/" + page;
			this.path = path;
			this.clss = clss;
			this.properties = new HashMap<>();
		}

		public void setTheme(String theme) {
			this.theme = theme;
		}

		public void setProperty( String key, String value ) {
			this.properties.put(key, value);
		}

		public String getFullPath() {
			return path + page;
		}
	}

	private class DefaultEntryPointFactory implements EntryPointFactory{

		private EntryFactoryBuilder.EntryNode<D> node;

		public DefaultEntryPointFactory( EntryFactoryBuilder.EntryNode<D> node, DefaultSessionStore<D> store) {
			super();
			this.node = node;
		}

		@SuppressWarnings("unchecked")
		@Override
		public EntryPoint create() {
			EntryPoint entryPoint = createEntryPoint( clss, this.node.clss);
			if(( this.node.store != null ) && ( entryPoint instanceof IDataEntryPoint )) {
				IDataEntryPoint<DefaultSessionStore<D>> ae = (IDataEntryPoint<DefaultSessionStore<D>>) entryPoint;
				ae.setData( this.node.store);
			}
			return entryPoint;
		}

		@SuppressWarnings("unchecked")
		protected <E extends EntryPoint> E createEntryPoint( Class<?> clss, String className){
			if( StringUtils.isEmpty( className ))
				return null;
			Class<E> builderClass;
			E builder = null;
			try {
				builderClass = (Class<E>) clss.getClassLoader().loadClass( className );
				Constructor<E> constructor = builderClass.getConstructor();
				builder = constructor.newInstance();
			} catch (Exception e) {
				e.printStackTrace();
			}
			return builder;
		}

	}
}