package org.condast.commons.jpa.na.service;

import org.condast.commons.jpa.na.model.IApplication;
import org.condast.commons.jpa.na.model.IApplicationPerson;
import org.condast.commons.persistence.service.IBasePersistenceService;
import org.condast.commons.persistence.service.IPersistencyController;

public interface INAService extends IBasePersistenceService<IPersistencyController<IApplicationPerson,IApplication>> {

}
