package org.condast.commons.persistence.def;

import java.util.EventObject;

public class PersistenceFactoryEvent extends EventObject {
	private static final long serialVersionUID = 1L;

	private String id;

	public PersistenceFactoryEvent(Object source, String id) {
		super(source);
		this.id = id;
	}

	public String getId() {
		return id;
	}
}
