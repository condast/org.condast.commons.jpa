package org.condast.commons.jpa.persistence;

import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Properties;
import java.util.logging.Logger;

import org.condast.commons.Utils;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;
import org.osgi.framework.FrameworkUtil;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.service.jdbc.DataSourceFactory;

public class DatasourceInspector {

	private static final String S_INFO_JDBC_FACTORY = "Available JDBC factory: ";

	private static final String S_INFO_DRIVER_FOUND =
			"Found JDBC driver in bundle: ";

	private static final String S_ERR_DRIVER_NOT_FOUND =
			"JDBC driver not found: ";

	private static final String S_INFO_CONNECTED =
			"JDBC connection established with: ";

	private static final String S_ERR_CONNECTION =
			"JDBC driver did not accept the connection URL";


	private Logger logger = Logger.getLogger(DatasourceInspector.class.getName());


	/**
	 * First find all the JDBC factories that are registered 
	 * @param context
	 * @throws InvalidSyntaxException
	 */
	public void inspectJdbcFactories(BundleContext context)
			throws InvalidSyntaxException {

		Collection<ServiceReference<DataSourceFactory>> refs =
				context.getServiceReferences(
						DataSourceFactory.class, null);

		if (Utils.assertNull(refs)) {
			logger.info("No JDBC factories registered");
			return;
		}

		for (ServiceReference<DataSourceFactory> ref : refs) {
			Object driver = ref.getProperty(
					DataSourceFactory.OSGI_JDBC_DRIVER_CLASS);

			logger.info(S_INFO_JDBC_FACTORY + driver);
		}
	}

	/**
	 * Then find the JDBC driver with the given name 
	 * @param context
	 * @param driverName
	 * @return
	 * @throws ReflectiveOperationException
	 */
	public Driver findDriver(
			BundleContext context,
			String driverName)
					throws ReflectiveOperationException {

		for (Bundle bundle : context.getBundles()) {

			if (bundle.getState() == Bundle.UNINSTALLED) 
				continue;

			try {
				Class<?> type = bundle.loadClass(driverName);
				if (!Driver.class.isAssignableFrom(type))
					continue;

				Bundle owner = FrameworkUtil.getBundle(type);
				if (owner != null && !owner.equals(bundle))
					continue;

				logger.info(S_INFO_DRIVER_FOUND + bundle.getSymbolicName()	);
				return (Driver) type
						.getDeclaredConstructor()
						.newInstance();

			} catch (ClassNotFoundException ex) {
				//logger.warning( ex.getMessage());
			}
		}

		throw new IllegalStateException( S_ERR_DRIVER_NOT_FOUND + driverName );
	}

	/**
	 * Test the connection with the data source
	 * @param driver
	 * @param url
	 * @param jdbcProperties
	 * @throws SQLException
	 */
	public void testConnection(
			Driver driver,
			String url,
			Properties jdbcProperties) throws SQLException {

		try (Connection connection =
				driver.connect(url, jdbcProperties)) {

			if (connection == null)
				throw new SQLException(S_ERR_CONNECTION);

			logger.info( S_INFO_CONNECTED +	connection.getMetaData().getDatabaseProductName());
		}
	}
}
