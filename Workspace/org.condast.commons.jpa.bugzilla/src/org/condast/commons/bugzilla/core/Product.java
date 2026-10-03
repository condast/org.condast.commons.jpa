package org.condast.commons.bugzilla.core;

import org.condast.commons.messaging.core.IJsonObject;

/**
 * @See; http://bugzilla.readthedocs.io/en/latest/api/core/v1/classification.html
 * {
 *  "name" : "AnotherProduct",
 *  "description" : "Another Product",
 *  "classification" : "Unclassified",
 *  "is_open" : false,
 *  "has_unconfirmed" : false,
 *  "version" : "unspecified"
 * }
 * @author Kees
 */
public class Product implements IJsonObject{

	private String name;

	private String description;
	private String version;


	public Product(String name, String description, String version) {
		super();
		this.name = name;
		this.description = description;
		this.version = version;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
		return description;
	}

	public String getVersion() {
		return version;
	}

	@Override
	public String toJson() {
		return "{}";
		//Gson gson = new Gson();
		//return gson.toJson( this, Product.class );
	}
}