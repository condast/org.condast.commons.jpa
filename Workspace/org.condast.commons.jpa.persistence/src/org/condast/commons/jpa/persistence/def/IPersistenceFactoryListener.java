package org.condast.commons.jpa.persistence.def;

public interface IPersistenceFactoryListener {

	/**
	 * Respond to adding or removing composites
	 * @param event
	 */
	public void notifyFactoryChanged( PersistenceFactoryEvent event );
}
