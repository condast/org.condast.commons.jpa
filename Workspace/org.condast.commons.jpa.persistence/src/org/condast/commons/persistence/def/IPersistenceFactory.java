package org.condast.commons.persistence.def;

import org.condast.commons.persistence.service.IBasePersistenceService;
import org.condast.commons.persistence.service.IPersistencyController;

public interface IPersistenceFactory<T,U extends Object> {

	public void addFactoryListener( IPersistenceFactoryListener listener );

	public void removeFactoryListener( IPersistenceFactoryListener listener );

	/**
	  * The factory can provide a persistence service
	  * @param controller
	  * @return
	  */
	 public IBasePersistenceService<IPersistencyController<T,U>> getService();

}
