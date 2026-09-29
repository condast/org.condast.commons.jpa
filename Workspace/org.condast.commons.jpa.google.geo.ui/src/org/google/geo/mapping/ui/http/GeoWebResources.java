package org.google.geo.mapping.ui.http;

import java.util.logging.Logger;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardResource;

@Component( service = GeoWebResources.class )
@HttpWhiteboardResource(pattern="/geo/web/*", prefix="/WEB-INF")
public class GeoWebResources {

	private Logger logger = Logger.getLogger(GeoWebResources.class.getName());

	public GeoWebResources() {
		logger.info("**** RESOURCES LOADED: " + this.getClass().getName()); 
	}
}
