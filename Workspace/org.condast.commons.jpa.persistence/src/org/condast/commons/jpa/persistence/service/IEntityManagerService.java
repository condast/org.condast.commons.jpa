package org.condast.commons.jpa.persistence.service;

//See if this can be removed
@Deprecated
public interface IEntityManagerService<T,U extends Object> {

	/**
	  * The factory can provide a persistence controller
	  * @param controller
	  * @return
	  */
	IPersistencyController<T, U> getController(String controller);

}