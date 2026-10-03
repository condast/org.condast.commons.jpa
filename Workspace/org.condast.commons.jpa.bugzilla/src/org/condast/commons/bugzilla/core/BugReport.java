package org.condast.commons.bugzilla.core;

import org.condast.commons.messaging.core.IJsonObject;
import org.condast.commons.strings.StringStyler;

import com.google.gson.Gson;

/**
 * @See; http://bugzilla.readthedocs.io/en/latest/api/core/v1/bug.html#create-bug
 * {
  "product" : "TestProduct",
  "component" : "TestComponent",
  "version" : "unspecified",
  "summary" : "'This is a test bug - please disregard",
  "alias" : "SomeAlias",
  "op_sys" : "All",
  "priority" : "P1",
  "rep_platform" : "All"
 * @author Kees
 *
 */
public class BugReport implements IJsonObject {

	private enum Priority{
		P1;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private enum Platform{
		ALL;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private enum OperatingSystem{
		ALL;

		@Override
		public String toString() {
			return StringStyler.prettyString( super.toString() );
		}
	}

	private String product;
	private String component;

	private String summary;
	private String alias;

	private Platform platform;

	private OperatingSystem opsys;

	private Priority priority;

	public BugReport(String product, String component, String summary, String alias ) {
		this( product, component, summary, alias, Platform.ALL, OperatingSystem.ALL, Priority.P1 );
	}

	public BugReport(String product, String component, String summary, String alias, Platform platform,
			OperatingSystem opsys, Priority priority) {
		super();
		this.product = product;
		this.component = component;
		this.summary = summary;
		this.alias = alias;
		this.platform = platform;
		this.opsys = opsys;
		this.priority = priority;
	}

	public String getProduct() {
		return product;
	}

	public String getComponent() {
		return component;
	}

	public String getSummary() {
		return summary;
	}

	public String getAlias() {
		return alias;
	}

	public Platform getPlatform() {
		return platform;
	}

	public OperatingSystem getOpsys() {
		return opsys;
	}

	public Priority getPriority() {
		return priority;
	}

	@Override
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson( this, BugReport.class );
	}
}