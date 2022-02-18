package com.maintenance.db.dao;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.db.dto.Anhang;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class AnhangJDBCDAO implements AnhangDAO {

	private static final Logger logger = Logger.getLogger(AnhangJDBCDAO.class);

	private final static String GET_ALL_ATTACHMENT = "SELECT * FROM anhang where wartung_id is null and station_id is null and anlage_id is null ORDER BY name ASC";
	private final static String GET_ALL_ATTACHMENT_FROM_WARTUNG = "SELECT * FROM anhang where wartung_id = ? ORDER BY name ASC";
	private final static String GET_ALL_ATTACHMENT_FROM_STATION = "SELECT * FROM anhang where station_id = ? ORDER BY name ASC";
	private final static String GET_ALL_ATTACHMENT_FROM_ANLAGE = "SELECT * FROM anhang where anlage_id = ? ORDER BY name ASC";
	private final static String INSERT_ATTACHMENT = "INSERT INTO anhang(name, file, timestamp, user, wartung_Id, station_Id, anlage_Id) VALUES (?, ?, ?, ?, ?, ?, ?)";
	private final static String DELETE_ATTACHMENT = "DELETE FROM anhang where id = ?";

	private final static String ANZAHL_ATTACHMENT_WARTUNG = "SELECT id from anhang where wartung_id = ?";
	private final static String ANZAHL_ATTACHMENT_STATION = "SELECT id from anhang where station_id = ?";
	private final static String ANZAHL_ATTACHMENT_ANLAGE = "SELECT id from anhang where anlage_id = ?";

	@Override
	public void deleteAnhang(Anhang anhang) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_ATTACHMENT);
			ps.setInt(1, anhang.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(anhang);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<Anhang> getAnhangList(Wartung wartung) throws DAOException {

		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;

		List<Anhang> attachmentList = new ArrayList<Anhang>();

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "MaintenanceManager", "anhaenge");
			dir.mkdirs();

			dir.deleteOnExit();
			con = ConnectionManager.getInstance().getConnection();
			ps = con.prepareStatement(GET_ALL_ATTACHMENT_FROM_WARTUNG);

			ps.setInt(1, wartung.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				Anhang anhang = new Anhang();
				anhang.setId(rs.getInt("id"));
				anhang.setName(rs.getString("name"));
				// ===================================================================================
				File file = new File(dir.getAbsolutePath() + File.separator + anhang.getName());
				file.createNewFile();

				FileOutputStream fos = new FileOutputStream(file);
				byte[] buffer = new byte[1];

				InputStream is = rs.getBinaryStream("file");
				while (is.read(buffer) > 0) {
					fos.write(buffer);
				}
				file.deleteOnExit();
				fos.close();
				anhang.setFile(file);
				// ===================================================================================
				anhang.setTimestamp(rs.getTimestamp("timestamp"));
				anhang.setUser(rs.getString("user"));
				anhang.setWartungId(wartung.getId());
				attachmentList.add(anhang);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Anhaenge: " + attachmentList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		} catch (IOException e) {

			e.printStackTrace();
		}

		return attachmentList;
	}

	@Override
	public void insertAnhang(Anhang anhang) throws DAOException {
		PreparedStatement ps;

		try {
			FileInputStream fin = new FileInputStream(anhang.getFile());
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_ATTACHMENT);

			ps.setString(1, anhang.getName());
			ps.setBinaryStream(2, fin, (int) anhang.getFile().length());
			anhang.setTimestamp(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
			ps.setTimestamp(3, anhang.getTimestamp());
			ps.setString(4, System.getProperty("user.name"));

			/*
			 * if (anhang.getWartungId() == 0 && anhang.getStationId() == 0) { ps.setNull(5,
			 * anhang.getWartungId()); ps.setNull(6, anhang.getStationId()); } else if
			 * (anhang.getWartungId() == 0) { ps.setNull(5, anhang.getWartungId());
			 * ps.setInt(6, anhang.getStationId()); } else if (anhang.getStationId() == 0) {
			 * ps.setInt(5, anhang.getWartungId()); ps.setNull(6, anhang.getStationId()); }
			 * else if (anhang.getAnlageId() == 0) { ps.setInt(5, anhang.getWartungId());
			 * ps.setNull(6, anhang.getStationId()); }
			 */

			if (anhang.getWartungId() == 0 && anhang.getStationId() == 0 && anhang.getAnlageId() == 0) {
				ps.setNull(5, anhang.getWartungId());
				ps.setNull(6, anhang.getStationId());
				ps.setNull(7, anhang.getAnlageId());
			} else if (anhang.getWartungId() != 0) {
				ps.setInt(5, anhang.getWartungId());
				ps.setNull(6, anhang.getStationId());
				ps.setNull(7, anhang.getAnlageId());
			} else if (anhang.getStationId() != 0) {
				ps.setNull(5, anhang.getWartungId());
				ps.setInt(6, anhang.getStationId());
				ps.setNull(7, anhang.getAnlageId());
			} else if (anhang.getAnlageId() != 0) {
				ps.setNull(5, anhang.getWartungId());
				ps.setNull(6, anhang.getStationId());
				ps.setInt(7, anhang.getAnlageId());
			}

			ps.executeUpdate();

			anhang.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(anhang);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		} catch (FileNotFoundException e) {

			e.printStackTrace();
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
	public boolean getAnhangAnzahl(Wartung wartung) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(ANZAHL_ATTACHMENT_WARTUNG);
			ps.setInt(1, wartung.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return false;
	}

	@Override
	public boolean getAnhangAnzahl(Station station) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(ANZAHL_ATTACHMENT_STATION);
			ps.setInt(1, station.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return false;
	}

	@Override
	public boolean getAnhangAnzahl(Anlage anlage) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(ANZAHL_ATTACHMENT_ANLAGE);
			ps.setInt(1, anlage.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				return true;
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return false;
	}

	@Override
	public List<Anhang> getAnhangList(int stationId) throws DAOException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;

		List<Anhang> attachmentList = new ArrayList<Anhang>();

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "MaintenanceManager", "anhaenge");
			dir.mkdirs();

			dir.deleteOnExit();
			con = ConnectionManager.getInstance().getConnection();
			ps = con.prepareStatement(GET_ALL_ATTACHMENT_FROM_STATION);

			ps.setInt(1, stationId);
			rs = ps.executeQuery();

			while (rs.next()) {

				Anhang anhang = new Anhang();
				anhang.setId(rs.getInt("id"));
				anhang.setName(rs.getString("name"));
				// ===================================================================================
				File file = new File(dir.getAbsolutePath() + File.separator + anhang.getName());
				if (!file.exists()) {
					file.createNewFile();

					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1];

					InputStream is = rs.getBinaryStream("file");
					while (is.read(buffer) > 0) {
						fos.write(buffer);
					}
					file.deleteOnExit();
					fos.close();
				}
				anhang.setFile(file);
				// ===================================================================================
				anhang.setTimestamp(rs.getTimestamp("timestamp"));
				anhang.setUser(rs.getString("user"));
				anhang.setStationId(stationId);
				attachmentList.add(anhang);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Anhaenge: " + attachmentList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

		return attachmentList;
	}

	@Override
	public List<Anhang> getAnhangList(Anlage anlage) throws DAOException {
		ResultSet rs = null;
		PreparedStatement ps = null;
		Connection con = null;

		List<Anhang> attachmentList = new ArrayList<Anhang>();

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "MaintenanceManager", "anhaenge");
			dir.mkdirs();

			dir.deleteOnExit();
			con = ConnectionManager.getInstance().getConnection();
			ps = con.prepareStatement(GET_ALL_ATTACHMENT_FROM_ANLAGE);

			ps.setInt(1, anlage.getId());
			rs = ps.executeQuery();

			while (rs.next()) {

				Anhang anhang = new Anhang();
				anhang.setId(rs.getInt("id"));
				anhang.setName(rs.getString("name"));
				// ===================================================================================
				File file = new File(dir.getAbsolutePath() + File.separator + anhang.getName());
				if (!file.exists()) {
					file.createNewFile();

					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1];

					InputStream is = rs.getBinaryStream("file");
					while (is.read(buffer) > 0) {
						fos.write(buffer);
					}
					file.deleteOnExit();
					fos.close();
				}
				anhang.setFile(file);
				// ===================================================================================
				anhang.setTimestamp(rs.getTimestamp("timestamp"));
				anhang.setUser(rs.getString("user"));
				anhang.setAnlageId(anlage.getId());
				attachmentList.add(anhang);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Anhaenge: " + attachmentList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

		return attachmentList;
	}

	@Override
	public List<Anhang> getAnhangList() throws DAOException {
		ResultSet rs = null;
		Connection con = null;
		Statement statement = null;

		List<Anhang> attachmentList = new ArrayList<Anhang>();

		try {

			File dir = new File(System.getProperty("user.home") + File.separator + "MaintenanceManager", "anhaenge");
			dir.mkdirs();

			dir.deleteOnExit();
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALL_ATTACHMENT);

			while (rs.next()) {

				Anhang anhang = new Anhang();
				anhang.setId(rs.getInt("id"));
				anhang.setName(rs.getString("name"));
				// ===================================================================================
				File file = new File(dir.getAbsolutePath() + File.separator + anhang.getName());
				if (!file.exists()) {
					file.createNewFile();

					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1];

					InputStream is = rs.getBinaryStream("file");
					while (is.read(buffer) > 0) {
						fos.write(buffer);
					}
					file.deleteOnExit();
					fos.close();
				}
				anhang.setFile(file);
				// ===================================================================================
				anhang.setTimestamp(rs.getTimestamp("timestamp"));
				anhang.setUser(rs.getString("user"));
				attachmentList.add(anhang);
			}

			if (logger.isInfoEnabled()) {
				logger.info("Anhaenge: " + attachmentList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		} catch (IOException e) {
			e.printStackTrace();
			throw new DAOException(e.getMessage());
		}

		return attachmentList;
	}

}