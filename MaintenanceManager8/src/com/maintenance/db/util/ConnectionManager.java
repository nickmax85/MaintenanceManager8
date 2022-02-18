package com.maintenance.db.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.SQLException;

import org.apache.log4j.Logger;

import com.maintenance.util.ApplicationProperties;

/**
 * Verwaltung der Datenbankverbindung
 * 
 * @author Markus Thaler, Ing.
 */
public class ConnectionManager {

	private static final Logger logger = Logger.getLogger(ConnectionManager.class);

	public static ConnectionManager getInstance() {
		if (instance == null)
			instance = new ConnectionManager();
		return instance;
	}

	private static ConnectionManager instance;

	private Connection connection;

	public Connection getConnection() throws SQLException {

		String url = null;
		String user = null;
		String password = null;

		try {
			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:" + ApplicationProperties.getInstance().getProperty("db_url") + "://"
					+ ApplicationProperties.getInstance().getProperty("db_host") + ":"
					+ ApplicationProperties.getInstance().getProperty("db_port") + "/"
					+ ApplicationProperties.getInstance().getProperty("db_model");
			user = ApplicationProperties.getInstance().getProperty("db_user");
			password = ApplicationProperties.getInstance().getProperty("db_password");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection(url, user, password);

		return connection;
	}

	public Connection getConnection(String host, String port, String model, String user, String password)
			throws SQLException {

		String url = null;

		try {

			Class.forName("com.mysql.jdbc.Driver");
			url = "jdbc:mysql://" + host + ":" + port + "/" + model;

		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if (connection == null || connection.isClosed())
			connection = DriverManager.getConnection(url, user, password);

		return connection;
	}

	public void getMetaData() {

		DatabaseMetaData dm;

		if (connection != null) {

			try {
				dm = connection.getMetaData();
				logger.info("Driver name: " + dm.getDriverName());
				logger.info("Driver version: " + dm.getDriverVersion());
				logger.info("Product name: " + dm.getDatabaseProductName());
				logger.info("Product version: " + dm.getDatabaseProductVersion());
				logger.info(dm.getURL());
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

		}

	}
}
