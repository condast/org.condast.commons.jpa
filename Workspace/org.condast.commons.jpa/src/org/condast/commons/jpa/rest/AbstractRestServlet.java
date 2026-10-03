package org.condast.commons.jpa.rest;

import java.util.logging.Logger;

import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;

public abstract class AbstractRestServlet extends ServletContainer {
	private static final long serialVersionUID = -478694508797364227L;

	public static final String S_DEFAULT_CONTEXT_PATH = "rest";

	private static ResourceConfig restapp = new ResourceConfig(); 
	
	private Logger logger = Logger.getLogger(AbstractRestServlet.class.getName());

	public AbstractRestServlet() {
		super( restapp);
		createRest(restapp);
		logger.info("STARTING SERVLET: " + this.getClass().getName()); 
	}
	
	/**
	 * Register the REST endpoints. Return true if WADL needs to be disabled
	 * @return
	 */
	protected abstract boolean registerEndpoints( ResourceConfig restapp);
	
	protected boolean createRest( ResourceConfig config ) {
		boolean noWadl = registerEndpoints(config);
		config.property(ServerProperties.WADL_FEATURE_DISABLE, noWadl);
		return noWadl;	
	}
}
