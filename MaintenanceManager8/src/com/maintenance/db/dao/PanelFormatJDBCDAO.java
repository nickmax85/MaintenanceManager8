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

import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class PanelFormatJDBCDAO implements PanelFormatDAO {

	private static final Logger logger = Logger.getLogger(PanelFormatJDBCDAO.class);

	private final static String TIMESTAMP_ERROR = "Ein Benutzer hat die Daten gerade verändert.\nBitte öffnen Sie das Fenster erneut oder drücken sie die F5 Taste.";

	private final static String GET_PANELFORMAT = "SELECT * FROM panelFormat where id = ?";
	private final static String GET_ALLPANELFORMAT = "SELECT * FROM panelFormat";
	private final static String INSERT_PANELFORMAT = "INSERT INTO panelFormat(x, y, width, heigth, timestamp, user) VALUES (?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_PANELFORMAT = "UPDATE panelFormat SET x = ?, y = ?, width = ?, heigth = ?, timestamp = ?, user = ? where id = ?";
	private final static String DELETE_PANELFORMAT = "DELETE FROM panelFormat WHERE id= ?";

	@Override
	public void deletPanelFormat(PanelFormat panelFormat) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_PANELFORMAT);
			ps.setInt(1, panelFormat.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(panelFormat);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<PanelFormat> getAllPanelFormat() throws DAOException {

		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<PanelFormat> panelFormatList = new ArrayList<PanelFormat>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALLPANELFORMAT);

			while (rs.next()) {
				PanelFormat panelFormat = new PanelFormat();
				panelFormat.setId(new Integer(rs.getInt("id")));
				panelFormat.setX(rs.getInt("x"));
				panelFormat.setY(rs.getInt("y"));
				panelFormat.setWidth(rs.getInt("width"));
				panelFormat.setHeigth(rs.getInt("heigth"));
				panelFormat.setTimestamp(rs.getTimestamp("timestamp"));
				panelFormat.setUser(rs.getString("user"));

				panelFormatList.add(panelFormat);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(panelFormatList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return panelFormatList;
	}

	@Override
	public PanelFormat getPanelFormat(int panelFormatId) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		PanelFormat panelFormat;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_PANELFORMAT);
			ps.setInt(1, panelFormatId);
			rs = ps.executeQuery();
			rs.next();

			panelFormat = new PanelFormat();
			panelFormat.setId(new Integer(rs.getInt("id")));
			panelFormat.setX(rs.getInt("x"));
			panelFormat.setY(rs.getInt("y"));
			panelFormat.setWidth(rs.getInt("width"));
			panelFormat.setHeigth(rs.getInt("heigth"));
			panelFormat.setTimestamp(rs.getTimestamp("timestamp"));
			panelFormat.setUser(rs.getString("user"));

			if (logger.isInfoEnabled()) {
				// logger.info(panelFormat);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return panelFormat;
	}

	@Override
	public void insertPanelFormat(PanelFormat panelFormat) throws DAOException {

		PreparedStatement ps;
		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_PANELFORMAT);

			ps.setInt(1, panelFormat.getX());
			ps.setInt(2, panelFormat.getY());
			ps.setInt(3, panelFormat.getWidth());
			ps.setInt(4, panelFormat.getHeigth());
			panelFormat.setTimestamp(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
			ps.setTimestamp(5, panelFormat.getTimestamp());
			ps.setString(6, System.getProperty("user.name"));
			ps.executeUpdate();

			panelFormat.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(panelFormat);
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
	public void updatePanelFormat(PanelFormat panelFormat) throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {

			PanelFormat panelFormatDb = getPanelFormat(panelFormat.getId());

			if (panelFormatDb.getTimestamp().equals(panelFormat.getTimestamp())) {

				PreparedStatement ps = ConnectionManager.getInstance().getConnection()
						.prepareStatement(UPDATE_PANELFORMAT);

				ps.setInt(1, panelFormat.getX());
				ps.setInt(2, panelFormat.getY());
				ps.setInt(3, panelFormat.getWidth());
				ps.setInt(4, panelFormat.getHeigth());
				ps.setTimestamp(5, timestamp);
				ps.setString(6, System.getProperty("user.name"));
				ps.setInt(7, panelFormat.getId());
				ps.executeUpdate();

				// Zeitstempel soll erst beschrieben werden, wenn der Befehl
				// erfolgreich ausgeführt wurde
				panelFormat.setTimestamp(timestamp);

				if (logger.isInfoEnabled()) {
					// logger.info(panelFormat.getX());
				}
			} else {
				throw new DAOException(TIMESTAMP_ERROR);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

}
