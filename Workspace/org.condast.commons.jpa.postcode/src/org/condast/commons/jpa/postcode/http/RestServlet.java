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
import org.osgi.service.http.whiteboard.propertytypes.HttpWhiteboardServletName;

@Component(service = Servlet.class, 
  scope=ServiceScope.PROTOTYPE,
  property= {
		  "osgi.http.whiteboard.servlet.name=" + RestServlet.S_SERVLET_NAME,		  
		  "osgi.http.whiteboard.servlet.pattern=/" + RestServlet.S_CONTEXT_PATH + "/*"
})
@HttpWhiteboardServletName( RestServlet.S_SERVLET_NAME )
public class RestServlet extends ServletContainer {
	private static final long serialVersionUID = -478694508797364227L;

	public static final String S_CONTEXT_PATH = "postcode";
	protected static final String S_SERVLET_NAME = S_CONTEXT_PATH + ".rest";

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
