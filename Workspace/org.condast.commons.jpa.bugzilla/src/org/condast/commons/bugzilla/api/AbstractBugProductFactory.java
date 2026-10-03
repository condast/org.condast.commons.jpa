package org.condast.commons.bugzilla.api;

public abstract class AbstractBugProductFactory implements IBugProductFactory{

	private IBugProduct bugproduct;

	protected AbstractBugProductFactory( boolean enabled, String product, String component) {
		this.bugproduct = new BugProduct( enabled, product, component);
	}

	protected abstract boolean onWildcardCorrect( String wildcard );

	@Override
	public IBugProduct createProduct(String wildcard) {
		return onWildcardCorrect(wildcard)? bugproduct: null;
	}

	private class BugProduct implements IBugProduct{

		private String product;
		private String component;
		private boolean enabled;

		public BugProduct( boolean enabled, String product, String component) {
			super();
			this.enabled = enabled;
			this.product = product;
			this.component = component;
		}

		@Override
		public boolean isEnabled() {
			return enabled;
		}

		@Override
		public String getProduct() {
			return this.product;
		}

		@Override
		public String getComponent() {
			return this.component;
		}

	}
}
