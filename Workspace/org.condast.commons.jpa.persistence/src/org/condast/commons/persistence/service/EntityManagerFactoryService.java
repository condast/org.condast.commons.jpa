package org.condast.commons.persistence.service;

import java.util.Map;

import javax.persistence.EntityManagerFactory;

public class EntityManagerFactoryService extends FactoryService<EntityManagerFactory> {

	protected static final String BUNDLE_NAME_KEY = "bundle-name";

	protected EntityManagerFactoryService(String name) {
		super(name);
	}

	protected static boolean compare( EntityManagerFactory emf, String key, String value ) {
		Map<String,Object> props = emf.getProperties();
		String attr = (String) props.get(key);
		return  value.equals(attr);
	}
}
