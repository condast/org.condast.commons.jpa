package org.condast.commons.jpa.persistence;

import java.io.Closeable;
import java.net.URL;
import java.util.Enumeration;
import java.util.Properties;
import java.util.logging.Logger;

import javax.persistence.EntityManagerFactory;
import org.eclipse.persistence.config.PersistenceUnitProperties;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.wiring.BundleWiring;

public abstract class AbstractEntityManagerFactoryService implements Closeable{

	public static final String S_PERSISTENCE_LOCATION = "META-INF/persistence.xml";
	public static final String S_TARGET_DATABASE = "MySQL";
	
	public static final String S_INFO_ENTITYMANAGER_CREATED = "EntityManagerFactory created";
	public static final String S_INFO_MANAGED_ENTITY = "Managed entity: ";
	
	public static final String S_ERR_LOCATION_NOT_VISIBLE ="persistence.xml not visible to class loader";
	public static final String S_ERR_LOCATION_NOT_FOUND =  "Persistence unit myPU not found";
	
    private String persistenceUnitName;
    
	private EntityManagerFactory emf;

    private Logger logger = Logger.getLogger( this.getClass().getName() );
    
    protected AbstractEntityManagerFactoryService(String persistenceUnitName) {
		super();
		this.persistenceUnitName = persistenceUnitName;
	}

	public String getPersistenceUnitName() {
		return persistenceUnitName;
	}

	public EntityManagerFactory getFactory() {
		return emf;
	}

	protected abstract void augmentProperties( Properties jdbcProperties );
	
	/**
	 * this method reads the persistence.xml provided by the bundle, and parses this. then it checks for corresponding
	 * implementation of the databases driver (e.g mysql, mariadb or h2) and tries to offer this to the EclipseLink classloader
	 * so that the regular service discovery can find it. 
	 * 
	 * @param context
	 * @throws Exception
	 */
	protected void activate(BundleContext context) throws Exception {

        Bundle bundle = context.getBundle();

        // Check 1: Is persistence.xml inside the bundle?
        URL entry = bundle.getEntry( S_PERSISTENCE_LOCATION);
        logger.info("Bundle resource: " + entry);

        // Check 2: Can the bundle class loader discover it?
        ClassLoader loader = bundle.adapt(
            BundleWiring.class).getClassLoader();

        logger.info("Persistence bundle: " +
        	    bundle.getSymbolicName());

        	logger.info("Persistence classloader: " +
        	    loader);

        	logger.info("Current context classloader: " +
        	    Thread.currentThread().getContextClassLoader());

        	logger.info("Persistence descriptor: " + entry);
        
        Enumeration<URL> resources =
            loader.getResources( S_PERSISTENCE_LOCATION );

        int count = 0;
        while (resources.hasMoreElements()) {
        	logger.info("Persistence descriptor: " + resources.nextElement());
            count++;
        }

        if (count == 0) 
            throw new IllegalStateException( S_ERR_LOCATION_NOT_VISIBLE);


        Properties jdbcProperties = PersistencePropertiesHandler.readPersistenceProperties(entry, persistenceUnitName);
        this.augmentProperties(jdbcProperties);
        String driverName = jdbcProperties.getProperty( PersistenceUnitProperties.JDBC_DRIVER); 
        emf = OsgiJdbcClassLoader.createFactory(context, driverName, persistenceUnitName, jdbcProperties);
     }

    public void close() {
        if (emf != null) {
            emf.close();
            emf = null;
        }
    }
}