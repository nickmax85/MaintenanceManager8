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

import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class LeerflaecheJDBCDAO implements LeerflaecheDAO {

	private static final Logger logger = Logger.getLogger(LeerflaecheJDBCDAO.class);

	private final static String TIMESTAMP_ERROR = "Ein Benutzer hat die Daten gerade verändert.\nBitte öffnen Sie das Fenster erneut oder drücken sie die F5 Taste.";

	private final static String GET_ALLLEERFLAECHE = "SELECT * FROM leerflaeche ORDER BY name ASC";
	private final static String GET_LEERFLAECHE = "SELECT * FROM leerflaeche where id = ?";
	private final static String INSERT_LEERFLAECHE = "INSERT INTO leerflaeche(name, timestamp, user, panelFormatId) VALUES (?, ?, ?, ?)";
	private final static String UPDATE_LEERFLAECHE = "UPDATE leerflaeche SET name = ?, panelFormatId = ?,  timestamp = ?, user = ? where id = ?";
	private final static String DELETE_LEERFLAECHE = "DELETE FROM leerflaeche WHERE id= ?";

	@Override
	public void deleteLeerflaeche(Leerflaeche leerflaeche) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_LEERFLAECHE);
			ps.setInt(1, leerflaeche.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(leerflaeche);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<Leerflaeche> getAllLeerflaechen() throws DAOException {
		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<Leerflaeche> dummyProjektList = new ArrayList<Leerflaeche>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALLLEERFLAECHE);

			while (rs.next()) {
				Leerflaeche leerflaeche = new Leerflaeche();
				leerflaeche.setId(rs.getInt("id"));
				leerflaeche.setName(rs.getString("name"));
				leerflaeche.setPanelFormatId(rs.getInt("panelFormatId"));
				leerflaeche.setTimestampSql(rs.getTimestamp("timestamp"));
				leerflaeche.setUser(rs.getString("user"));

				dummyProjektList.add(leerflaeche);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(dummyProjektList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return dummyProjektList;
	}

	@Override
	public Leerflaeche getLeerflaeche(int dummyProjektId) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		Leerflaeche leerflaeche;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_LEERFLAECHE);
			ps.setInt(1, dummyProjektId);
			rs = ps.executeQuery();
			rs.next();

			leerflaeche = new Leerflaeche();
			leerflaeche.setId(new Integer(rs.getInt("id")));
			leerflaeche.setName(rs.getString("name"));
			leerflaeche.setPanelFormatId(rs.getInt("panelFormatId"));
			leerflaeche.setTimestampSql(rs.getTimestamp("timestamp"));
			leerflaeche.setUser(rs.getString("user"));

			if (logger.isInfoEnabled()) {
				// logger.info(leerflaeche.getName());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return leerflaeche;
	}

	@Override
	public Leerflaeche getLeerflaeche(Leerflaeche leerflaeche) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_LEERFLAECHE);
			ps.setInt(1, leerflaeche.getId());
			rs = ps.executeQuery();
			rs.next();

			leerflaeche.setId(new Integer(rs.getInt("id")));
			leerflaeche.setName(rs.getString("name"));
			leerflaeche.setPanelFormatId(rs.getInt("panelFormatId"));
			leerflaeche.setTimestampSql(rs.getTimestamp("timestamp"));
			leerflaeche.setUser(rs.getString("user"));

			if (logger.isInfoEnabled()) {
				// logger.info(leerflaeche.getName());
			}

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return leerflaeche;
	}

	@Override
	public void insertLeerflaeche(Leerflaeche leerflaeche) throws DAOException {
		PreparedStatement ps;
		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_LEERFLAECHE);

			ps.setString(1, leerflaeche.getName());
			leerflaeche.setTimestampSql(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
			ps.setTimestamp(2, leerflaeche.getTimestampSql());
			ps.setString(3, System.getProperty("user.name"));
			ps.setInt(4, leerflaeche.getPanelFormatId());
			ps.executeUpdate();

			leerflaeche.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(leerflaeche);
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
	public void updateLeerflaeche(Leerflaeche leerflaeche) throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {
			Leerflaeche projektDb = getLeerflaeche(leerflaeche.getId());

			if (projektDb.getTimestampSql().equals(leerflaeche.getTimestampSql())) {
				PreparedStatement ps = ConnectionManager.getInstance().getConnection()
						.prepareStatement(UPDATE_LEERFLAECHE);

				ps.setString(1, leerflaeche.getName());
				ps.setInt(2, leerflaeche.getPanelFormatId());
				ps.setTimestamp(3, timestamp);
				ps.setString(4, System.getProperty("user.name"));
				ps.setInt(5, leerflaeche.getId());
				ps.executeUpdate();

				leerflaeche.setTimestampSql(timestamp);

				if (logger.isInfoEnabled()) {
					logger.info(leerflaeche);

				} else
					throw new DAOException(TIMESTAMP_ERROR);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

}