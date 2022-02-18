package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;

import org.apache.log4j.Logger;

import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class SystemJDBCDAO implements SystemDAO {

	private static final Logger logger = Logger.getLogger(SystemJDBCDAO.class);

	private final static String INSERT_LOGGED_IN_USER = "INSERT INTO history(user, timestamp) VALUES (?, ?)";

	@Override
	public String getServerInfo() throws DAOException {
		Connection con = null;
		String info;

		try {
			con = ConnectionManager.getInstance().getConnection();
			info = con.getMetaData().getURL();

		} catch (SQLException e) {
			throw new DAOException(e);

		}
		return info;
	}

	@Override
	public void insertLoggedInUser() throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		PreparedStatement ps;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_LOGGED_IN_USER);

			ps.setString(1, System.getProperty("user.name"));
			ps.setTimestamp(2, timestamp);
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		}

		if (logger.isInfoEnabled()) {
			logger.info(System.getProperty("user.name"));
		}

	}

	public Integer selectLastID() throws DAOException {

		Integer lastId = null;

		try {
			PreparedStatement ps = ConnectionManager.getInstance().getConnection()
					.prepareStatement("select last_insert_id()");
			ResultSet rs = ps.executeQuery();
			while (rs.next()) {
				lastId = rs.getInt(1);
			}

			if (logger.isInfoEnabled()) {
				logger.info(lastId);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return lastId;
	}

}