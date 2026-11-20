package org.condast.commons.jpa.persistence;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Driver;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

import javax.sql.DataSource;

public final class OSGIDriverDataSource implements DataSource {

    private static final String S_ERR_URL =
        "JDBC driver does not accept URL: ";

    private static final String S_ERR_UNWRAP =
        "Cannot unwrap DataSource as: ";

    private static final String S_ERR_TIMEOUT =
        "Login timeout is not supported";

    private static final Logger LOGGER =
        Logger.getLogger(OSGIDriverDataSource.class.getName());

    private final Driver driver;
    private final String url;
    private final Properties connectionProperties;

    private volatile PrintWriter logWriter;

    public OSGIDriverDataSource(
            Driver driver,
            String url,
            Properties properties) {

        this.driver = Objects.requireNonNull(driver);
        this.url = Objects.requireNonNull(url);

        this.connectionProperties = new Properties();

        if (properties != null) {
            this.connectionProperties.putAll(properties);
        }
    }

    @Override
    public Connection getConnection() throws SQLException {
        return connect(copyProperties());
    }

    @Override
    public Connection getConnection(
            String username,
            String password) throws SQLException {

        Properties properties = copyProperties();

        if (username != null) {
            properties.setProperty("user", username);
        }

        if (password != null) {
            properties.setProperty("password", password);
        }

        return connect(properties);
    }

    private Connection connect(Properties properties)
            throws SQLException {

        Connection connection =
            driver.connect(url, properties);

        if (connection == null) {
            throw new SQLException(S_ERR_URL + url);
        }

        return connection;
    }

    private Properties copyProperties() {
        Properties copy = new Properties();
        copy.putAll(connectionProperties);
        return copy;
    }

    @Override
    public PrintWriter getLogWriter() {
        return logWriter;
    }

    @Override
    public void setLogWriter(PrintWriter writer) {
        logWriter = writer;
    }

    @Override
    public void setLoginTimeout(int seconds)
            throws SQLException {

        if (seconds != 0) {
            throw new SQLFeatureNotSupportedException(
                S_ERR_TIMEOUT
            );
        }
    }

    @Override
    public int getLoginTimeout() {
        return 0;
    }

    @Override
    public Logger getParentLogger() {
        return LOGGER;
    }

    @Override
    public boolean isWrapperFor(Class<?> type) {
        return type != null && type.isInstance(this);
    }

    @Override
    public <T> T unwrap(Class<T> type)
            throws SQLException {

        if (type != null && type.isInstance(this)) {
            return type.cast(this);
        }

        throw new SQLException(
            S_ERR_UNWRAP + type
        );
    }
}