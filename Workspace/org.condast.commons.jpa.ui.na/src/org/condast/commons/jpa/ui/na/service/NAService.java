package org.condast.commons.jpa.ui.na.service;

import org.condast.commons.jpa.na.model.IApplication;
import org.condast.commons.jpa.na.model.IApplicationPerson;
import org.condast.commons.jpa.na.service.INAService;
import org.condast.commons.persistence.service.AbstractPersistencyService;
import org.condast.commons.persistence.service.IPersistencyController;

public class NAService extends AbstractPersistencyService implements INAService {

	//Needs to be the same as in the persistence.xml file
	private static final String S_NAW_SERVICE = "nl.eetmee.na"; 

	private static NAService service = new NAService();
	
	private NAService(  ) {
		super( S_NAW_SERVICE, null);
	}

	public static NAService getInstance(){
		return service;
	}

	
	@Override
	public IPersistencyController<IApplicationPerson,IApplication> getController(String name) {
		//if( ApplicationPersonController.S_NAW_NAME.equals( name ))
		//	return new ApplicationPersonController( this);
		return null;
	}
}