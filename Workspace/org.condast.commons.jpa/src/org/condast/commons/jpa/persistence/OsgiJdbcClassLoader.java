package org.condast.commons.jpa.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.sql.Driver;
import java.util.Enumeration;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;

import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.eclipse.persistence.jpa.PersistenceProvider;
import org.eclipse.persistence.sessions.DirectConnector;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.wiring.BundleWiring;

public final class OsgiJdbcClassLoader extends ClassLoader {

	private static final String S_INFO_DRIVER_LOADED = "The requested driver was successfully loaded: ";

    private static final String S_ERR_DRIVER_BUNDLE_NOT_FOUND = "The requested driver bundle wasn't found: ";

	private final Bundle driverBundle;
    private final String driverClassName;

    private static Logger logger = Logger.getLogger( OsgiJdbcClassLoader.class.getName() );
    
    public OsgiJdbcClassLoader( ClassLoader parent, Bundle driverBundle, String driverClassName) {
        super(parent);
        this.driverBundle =
            Objects.requireNonNull(driverBundle);

        this.driverClassName =
            Objects.requireNonNull(driverClassName);
    }

    @Override
    protected Class<?> findClass(String name)
            throws ClassNotFoundException {

        if (driverClassName.equals(name)) {
            return driverBundle.loadClass(name);
        }

        throw new ClassNotFoundException(name);
    }
    
    public static EntityManagerFactory createFactory(BundleContext context, String driverName, String persistenceUnitName, Properties properties ){
    	DatasourceInspector inspector = new DatasourceInspector();
    	ClassLoader combinedLoader = null;
    	EntityManagerFactory emf = null;
    	try {

    		Driver driver = inspector.findDriver(context, driverName);
    		Bundle driverBundle =
    				FrameworkUtil.getBundle(driver.getClass());
    		if (driverBundle == null)
    			throw new IllegalStateException( S_ERR_DRIVER_BUNDLE_NOT_FOUND + driverName );

    		//Retrieve the parent classloader
    		Bundle bundle = context.getBundle();
    		ClassLoader loader = bundle.adapt(BundleWiring.class).getClassLoader();
    		combinedLoader = new OsgiJdbcClassLoader( loader, driverBundle, driverName );
    		
    		try {
				Enumeration<URL> resources =
					    combinedLoader.getResources("META-INF/persistence.xml");
					int count = 0;
					while (resources.hasMoreElements()) {
					    logger.fine("JPA descriptor: " + resources.nextElement());
					    count++;
					}
					logger.fine("JPA descriptor count: " + count);
			} catch (IOException e) {
				e.printStackTrace();
			}
    		
    		String[] entityNames = {
    			    "com.condast.authentication.model.Login",
    			    "com.condast.authentication.model.Admin"
    			};

    			for (String entityName : entityNames) {
    			    try {
    			        Class<?> entityClass = Class.forName(
    			            entityName, false, combinedLoader);

    			        logger.info("Entity available: " + entityClass.getName());
    			        logger.info("Entity annotation: " +
    			            entityClass.getAnnotation(javax.persistence.Entity.class));

    			    } catch (ClassNotFoundException e) {
    			        logger.warning("Entity unavailable: " + entityName);
    			    }
    			}

    		// Verify the bridge before starting EclipseLink.
    		Class<?> resolvedDriver = Class.forName( driverName, true, combinedLoader);
    		logger.info(S_INFO_DRIVER_LOADED + resolvedDriver.getName());

    	} catch (ReflectiveOperationException e) {
    		e.printStackTrace();
    		return emf;
    	}

    	// Pass the combined loader to EclipseLink.
    	properties.put( PersistenceUnitProperties.CLASSLOADER, combinedLoader );
    	// Tell EclipseLink to bypass DriverManager
    	properties.put( PersistenceUnitProperties.JDBC_CONNECTOR, DirectConnector.class.getName());
 
    	Thread thread = Thread.currentThread();
    	ClassLoader previous = thread.getContextClassLoader();

    	logger.fine("Requested persistence unit: " + persistenceUnitName);

    	logger.fine("JPA descriptor URL: " +
    	    combinedLoader.getResource("META-INF/persistence.xml"));
    	
    	try {
    	    thread.setContextClassLoader(combinedLoader);

    	    logger.info("Bootstrap classloader: " +
    	            thread.getContextClassLoader());

    	    URL descriptor =
    	    		combinedLoader.getResource("META-INF/persistence.xml");

    	    try {
    	    	try (InputStream stream = descriptor.openStream()) {
    	    		String xml = new String(
    	    				stream.readAllBytes(),
    	    				StandardCharsets.UTF_8);

    	    		logger.fine("Deployed persistence.xml:\n" + xml);
    	    	}
    	    } catch (IOException e) {
    	    	// TODO Auto-generated catch block
    	    	e.printStackTrace();
    	    }

    	    emf = new PersistenceProvider()
    	    		.createEntityManagerFactory(
    	    				persistenceUnitName,
    	    				properties
    	        );

    	} finally {
    	    thread.setContextClassLoader(previous);
    	}    	
    	return emf;
    }
}