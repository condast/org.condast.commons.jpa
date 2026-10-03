package org.condast.commons.jpa.postcode.http;

import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.condast.commons.jpa.postcode.rest.EmpRouter;
import org.condast.commons.jpa.postcode.rest.PostCodeResource;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.server.ServerProperties;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

@Component(service = Servlet.class, 
  scope=ServiceScope.PROTOTYPE,
  property= "osgi.http.whiteboard.servlet.pattern=/" + RestServlet.S_CONTEXT_PATH + "/*")
public class RestServlet extends ServletContainer {
	private static final long serialVersionUID = -478694508797364227L;

	public static final String S_CONTEXT_PATH = "postcode";

	private Logger logger = Logger.getLogger(RestServlet.class.getName());

	public RestServlet() {
		super( new RestApplication());
		logger.info("STARTING SERVLET: " + this.getClass().getName()); 
	}
	
	private static class RestApplication extends ResourceConfig {

		public RestApplication() {
			super();
			property(ServerProperties.WADL_FEATURE_DISABLE, true);
			register( PostCodeResource.class );
			register( EmpRouter.class);
		}
	}
}
