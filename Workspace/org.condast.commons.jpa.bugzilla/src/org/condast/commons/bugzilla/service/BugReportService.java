package org.condast.commons.bugzilla.service;

import org.condast.commons.bugzilla.api.IBugProductFactory;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;

@Component( name="org.condast.commons.bugzilla.service",
			immediate=true)
public class BugReportService {

	private Dispatcher dispatcher = Dispatcher.getInstance();

	@Reference( cardinality = ReferenceCardinality.AT_LEAST_ONE,
			policy=ReferencePolicy.DYNAMIC)
	public void bind( IBugProductFactory factory){
		this.dispatcher.addFactory( factory );
	}

	public void unbind( IBugProductFactory factory ){
		this.dispatcher.removeFactory( factory );
	}

}
