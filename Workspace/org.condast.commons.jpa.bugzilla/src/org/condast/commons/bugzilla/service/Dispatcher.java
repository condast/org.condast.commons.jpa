package org.condast.commons.bugzilla.service;

import java.util.ArrayList;
import java.util.Collection;

import org.condast.commons.bugzilla.api.IBugProduct;
import org.condast.commons.bugzilla.api.IBugProductFactory;

public class Dispatcher {

	private String DEFAULT_PRODUCT = "Condast";
	private String DEFAULT_COMPONENT = "SystemStartup";

	private static Dispatcher dispatcher = new Dispatcher();

	private Collection<IBugProductFactory> factories;

	private Dispatcher() {
		factories = new ArrayList<>();
	}

	public static Dispatcher getInstance(){
		return dispatcher;
	}

	public void addFactory( IBugProductFactory factory ){
		this.factories.add( factory);
	}

	public void removeFactory( IBugProductFactory factory ){
		this.factories.remove( factory );
	}

	public IBugProduct getProduct( String wildcard ) {
		for( IBugProductFactory factory: factories ) {
			IBugProduct product = factory.createProduct(wildcard);
			if(( product != null ) && ( product.isEnabled() ))
				return product;
		}
		return new SystemBugProduct();
	}

	private class SystemBugProduct implements IBugProduct{

		private String product;
		private String component;


		public SystemBugProduct() {
			super();
			this.product = DEFAULT_PRODUCT;
			this.component = DEFAULT_COMPONENT;
		}

		@Override
		public String getProduct() {
			return this.product;
		}

		@Override
		public String getComponent() {
			return this.component;
		}

		@Override
		public boolean isEnabled() {
			return true;
		}
	}
}