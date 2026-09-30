package org.condast.commons.jpa.postcode.http;

import java.util.logging.Logger;

import javax.servlet.Servlet;

import org.condast.commons.jpa.postcode.rest.EmpRouter;
import org.condast.commons.jpa.postcode.rest.PostCodeResource;
import org.condast.commons.messaging.http.AbstractServletWrapper;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ServiceScope;

import javax.ws.rs.ApplicationPath;


@Component(service = Servlet.class, 
  scope=ServiceScope.PROTOTYPE,
  property= "osgi.http.whiteboard.servlet.pattern=/postcode")
public class RestServlet extends AbstractServletWrapper {

	private Logger logger = Logger.getLogger(RestServlet.class.getName());

	public RestServlet() {
		super( S_CONTEXT_PATH );
		logger.info("STARTING SERVLET: " + this.getClass().getName()); 
	}
	
	@Override
	protected ServletContainer onCreateServlet(String contextPath) {
		RestApplication resourceConfig = new RestApplication();
		return new ServletContainer(resourceConfig);
	}
	
	@ApplicationPath("/" + RestApplication.S_CONTEXT_PATH)
	public class RestApplication extends ResourceConfig {

		public static final String S_CONTEXT_PATH = "postcode";

		public RestApplication() {
			super();
			register( PostCodeResource.class );
			register( EmpRouter.class);
		}
	}
}
