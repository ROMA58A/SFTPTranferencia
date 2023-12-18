package com.ols.doci.db;

import java.sql.Connection;
import java.sql.SQLException;
import com.ols.doci.EnvLoader;
import com.ols.doci.Log;
import org.apache.tomcat.jdbc.pool.DataSource;
import org.apache.tomcat.jdbc.pool.PoolProperties;

/**
 * Singleton class to handle connections with the database.
 *
 * @author Luis Brayan
 */
public class Database
{
    public static boolean DEBUG = false;
    public static String SCHEMA = "Doci";

    protected static PoolProperties connectionProperties;
    protected static Connection connection;

    /**
     * Load the credentials and configuration for the database connection.
     */
    public static void init()
    {
        DEBUG = EnvLoader.getLogLevel("db-connection").canDebug();
        SCHEMA = EnvLoader.getString("db-connection", "schema");
    }

    /**
     * Load the connection properties for the database connection.
     */
    public static void initConnectionProperties()
    {
        connectionProperties = new PoolProperties();

        connectionProperties.setUrl(EnvLoader.getString("db-connection", "url"));
        connectionProperties.setUsername(EnvLoader.getString("db-connection", "username"));
        connectionProperties.setPassword(EnvLoader.getString("db-connection", "password"));

        // connection settings
        connectionProperties.setJmxEnabled(EnvLoader.getBoolean("db-connection", "JmxEnabled"));
        connectionProperties.setTestWhileIdle(EnvLoader.getBoolean("db-connection", "TestWhileIdle"));
        connectionProperties.setTestOnBorrow(EnvLoader.getBoolean("db-connection", "TestOnBorrow"));
        connectionProperties.setTestOnReturn(EnvLoader.getBoolean("db-connection", "TestOnReturn"));
        connectionProperties.setLogAbandoned(EnvLoader.getBoolean("db-connection", "LogAbandoned"));
        connectionProperties.setRemoveAbandoned(EnvLoader.getBoolean("db-connection", "RemoveAbandoned"));

        connectionProperties.setRemoveAbandonedTimeout(EnvLoader.getInteger("db-connection", "RemoveAbandonedTimeout"));
        connectionProperties.setValidationInterval(EnvLoader.getInteger("db-connection", "ValidationInterval"));
        connectionProperties.setInitialSize(EnvLoader.getInteger("db-connection", "InitialSize"));
        connectionProperties.setMaxActive(EnvLoader.getInteger("db-connection", "MaxActive"));
        connectionProperties.setMaxWait(EnvLoader.getInteger("db-connection", "MaxWait"));
        connectionProperties.setMaxIdle(EnvLoader.getInteger("db-connection", "MaxIdle"));
        connectionProperties.setMinIdle(EnvLoader.getInteger("db-connection", "MinIdle"));
        connectionProperties.setMinEvictableIdleTimeMillis(EnvLoader.getInteger("db-connection", "MinEvictableIdleTimeMillis"));
        connectionProperties.setTimeBetweenEvictionRunsMillis(EnvLoader.getInteger("db-connection", "TimeBetweenEnvictionRunsMillis"));

        connectionProperties.setValidationQuery("SELECT 1");
        connectionProperties.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
        connectionProperties.setJdbcInterceptors(
                "org.apache.tomcat.jdbc.pool.interceptor.ConnectionState;"
                + "org.apache.tomcat.jdbc.pool.interceptor.StatementFinalizer");
    }

    /**
     * Returns the active database connection, if there is not a connection or
     * the connection has been closed creates a new one.
     *
     * @return
     */
    public static Connection getConnection()
    {
        if (!isConnected()) {
            openConnection();
        }
        return connection;
    }

    /**
     * Ensures the connection is open if its close then opens a new one.
     */
    public static void ensureConnection()
    {
        if (isConnected()) {
            Log.log("An old database connection is been re-used.");
        } else {
            openConnection();
            Log.log("A new database connection has been created.");
        }
    }

    /**
     * Forces a fresh connection.
     */
    public static void refreshConnection()
    {
        closeConnection();
        openConnection();
    }

    /**
     * Checks if the database connection is active.
     *
     * @return
     */
    public static boolean isConnected()
    {
        try {
            return connection != null && !connection.isClosed();
        } catch (SQLException ex) {
            Log.error(ex, "On Database#isConnected");
        }
        return false;
    }

    /**
     * Establishes a new connection to the database.
     */
    public static void openConnection()
    {
        try {
            // prepare connection properties
            DataSource datasource = new DataSource();
            datasource.setPoolProperties(connectionProperties);

            // initialize connection
            connection = datasource.getConnection();
            Log.log("Connection with the database was established.");
        } catch (SQLException ex) {
            Log.error(ex, "On Database#openConnection");
        }
    }

    /**
     * Close the established connection to the database.
     */
    public static void closeConnection()
    {
        try {
            if (!isConnected()) {
                return;
            }
            connection.close();
            Log.log("Connection with the database was closed.");
        } catch (SQLException ex) {
            Log.error(ex, "On Database#closeConnection");
        }
    }
}
