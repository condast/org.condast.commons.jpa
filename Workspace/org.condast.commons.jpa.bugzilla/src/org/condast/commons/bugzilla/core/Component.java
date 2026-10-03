package org.condast.commons.bugzilla.core;

import org.condast.commons.messaging.core.IJsonObject;

import com.google.gson.Gson;

/**
 * @See; http://bugzilla.readthedocs.io/en/latest/api/core/v1/component.html#create-component
 * {
 *   "product" : "TestProduct",
 *   "name" : "New Component",
 *   "description" : "This is a new component",
 *   "default_assignee" : "info@condast.com"
 * }
 * @author Kees
 */
public class Component implements IJsonObject{

	private String product;
	private String name;

	private String description;
	private String default_assignee;

	public Component(String product, String name, String description, String default_assignee) {
		super();
		this.product = product;
		this.name = name;
		this.description = description;
		this.default_assignee = default_assignee;
	}

	public String getProduct() {
		return product;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getDefault_assignee() {
		return default_assignee;
	}

	@Override
	public String toJson() {
		Gson gson = new Gson();
		return gson.toJson( this, Component.class );
	}
}