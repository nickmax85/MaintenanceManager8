package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;
import com.maintenance.model.User;

public class UserJDBCDAO implements UserDAO {

	private static final Logger logger = Logger.getLogger(UserJDBCDAO.class);

	private final static String TIMESTAMP_ERROR = "Ein Benutzer hat die Daten gerade verändert.\nBitte öffnen Sie das Fenster erneut oder drücken sie die F5 Taste.";

	private final static String GET_ALL_USER = "SELECT * FROM user ORDER BY lastname ASC";
	private final static String SELECT_USER = "SELECT * FROM user where id = ?";
	private final static String INSERT_USER = "INSERT INTO user(lastname, mail, timestamp, user) VALUES (?, ?, ?, ?)";
	private final static String UPDATE_USER = "UPDATE user SET lastname = ?, mail = ?, timestamp = ?, user = ? WHERE id = ?";
	private final static String DELETE_USER = "DELETE FROM user WHERE id= ?";

	@Override
	public void deleteUser(User user) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_USER);
			ps.setInt(1, user.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(user);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<User> getAllUser() throws DAOException {
		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<User> abteilungList = new ArrayList<User>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALL_USER);

			while (rs.next()) {
				User user = new User();
				user.setId(new Integer(rs.getInt("id")));
				user.setLastName(rs.getString("lastname"));
				user.setMail(rs.getString("mail"));
				abteilungList.add(user);
			}

			if (logger.isInfoEnabled()) {
				logger.info(abteilungList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return abteilungList;
	}

	@Override
	public void insertUser(User user) throws DAOException {

		PreparedStatement ps;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_USER);

			ps.setString(1, user.getLastName());
			ps.setString(2, user.getMail());
			ps.setString(4, System.getProperty("user.name"));
			ps.executeUpdate();

			user.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

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

	@Override
	public User selectUser(int userId) throws DAOException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		User user;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(SELECT_USER);
			ps.setInt(1, userId);
			rs = ps.executeQuery();
			rs.next();

			user = new User();
			user.setId(new Integer(rs.getInt("id")));
			user.setLastName(rs.getString("lastname"));
			user.setMail(rs.getString("mail"));

			if (logger.isInfoEnabled()) {
				logger.info(user);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return user;
	}

	@Override
	public void updateUser(User user) throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {

			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_USER);

			ps.setString(1, user.getLastName());
			ps.setString(2, user.getMail());
			ps.setTimestamp(3, timestamp);
			ps.setString(4, System.getProperty("user.name"));
			ps.setInt(5, user.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(user);
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

	}

}
