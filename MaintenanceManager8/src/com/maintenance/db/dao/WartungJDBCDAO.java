package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class WartungJDBCDAO implements WartungDAO {

	private static final Logger logger = Logger.getLogger(WartungJDBCDAO.class);

	private final static String TIMESTAMP_ERROR = "Ein Benutzer hat die Daten gerade verändert.\nBitte öffnen Sie das Fenster erneut, um die Daten neu zu laden.";

	private final static String GET_ALL_WARTUNGEN_FROM_ANLAGE = "SELECT * FROM wartung WHERE anlage_id = ? ORDER BY faellig DESC";
	private final static String GET_ALL_WARTUNGEN_FROM_STATION = "SELECT * FROM wartung WHERE station_id = ? ORDER BY faellig DESC";
	private final static String GET_WARTUNG = "SELECT * FROM wartung where id = ?";
	private final static String GET_ALL_WARTUNGEN = "SELECT * FROM wartung";
	private final static String INSERT_WARTUNG = "INSERT INTO wartung(auftragNr, faellig, prozent, mitarbeiter, info, status, anlage_id, station_id, timestamp, user, tpm) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String UPDATE_WARTUNG = "UPDATE wartung SET auftragNr = ?, faellig = ?, prozent = ?, mitarbeiter = ?, info = ?, status = ?, timestamp = ?, user = ?, tpm = ? WHERE id = ?";
	private final static String DELETE_WARTUNG = "DELETE FROM wartung WHERE id= ?";

	@Override
	public void deleteWartung(Wartung wartung) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_WARTUNG);
			ps.setInt(1, wartung.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(wartung);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<Wartung> getAllWartungen() throws DAOException {
		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<Wartung> wartungList = new ArrayList<Wartung>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALL_WARTUNGEN);

			while (rs.next()) {
				Wartung wartung = new Wartung();
				wartung.setId(new Integer(rs.getInt("id")));
				wartung.setAuftrag(rs.getString("auftragNr"));
				wartung.setDate(rs.getDate("faellig"));
				wartung.setProzent(rs.getInt("prozent"));
				wartung.setMitarbeiter(rs.getString("mitarbeiter"));
				wartung.setInfo(rs.getString("info"));
				wartung.setStatus(rs.getInt("status"));
				wartung.setTpm(rs.getBoolean("tpm"));
				wartung.setAnlageId(rs.getInt("anlage_Id"));
				wartung.setStationId(rs.getInt("station_Id"));
				wartung.setTimestampSql(rs.getTimestamp("timestamp"));
				wartungList.add(wartung);
			}

			if (logger.isInfoEnabled()) {
				logger.info(wartungList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return wartungList;
	}

	@Override
	public List<Wartung> getAllWartungenFromAnlage(int projektId) throws DAOException {

		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Wartung> wartungList = new ArrayList<Wartung>();

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(GET_ALL_WARTUNGEN_FROM_ANLAGE);
			ps.setInt(1, projektId);

			rs = ps.executeQuery();
			while (rs.next()) {
				Wartung wartung = new Wartung();
				wartung.setId(rs.getInt("id"));
				wartung.setAuftrag(rs.getString("auftragNr"));
				wartung.setDate(rs.getDate("faellig"));

				wartung.setProzent(rs.getInt("prozent"));
				wartung.setMitarbeiter(rs.getString("mitarbeiter"));
				wartung.setInfo(rs.getString("info"));
				wartung.setStatus(rs.getInt("status"));
				wartung.setTpm(rs.getBoolean("tpm"));
				wartung.setAnlageId(rs.getInt("anlage_Id"));
				wartung.setStationId(rs.getInt("station_Id"));
				wartung.setTimestampSql(rs.getTimestamp("timestamp"));

				wartungList.add(wartung);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(wartungList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return wartungList;
	}

	@Override
	public List<Wartung> getAllWartungenFromStation(int stationId) throws DAOException {
		PreparedStatement ps = null;
		ResultSet rs = null;
		List<Wartung> wartungList = new ArrayList<Wartung>();

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(GET_ALL_WARTUNGEN_FROM_STATION);
			ps.setInt(1, stationId);

			rs = ps.executeQuery();
			while (rs.next()) {
				Wartung wartung = new Wartung();
				wartung.setId(rs.getInt("id"));
				wartung.setAuftrag(rs.getString("auftragNr"));
				wartung.setDate(rs.getDate("faellig"));
				wartung.setProzent(rs.getInt("prozent"));
				wartung.setMitarbeiter(rs.getString("mitarbeiter"));
				wartung.setInfo(rs.getString("info"));
				wartung.setStatus(rs.getInt("status"));
				wartung.setTpm(rs.getBoolean("tpm"));
				wartung.setAnlageId(rs.getInt("anlage_id"));
				wartung.setStationId(rs.getInt("station_id"));
				wartung.setTimestampSql(rs.getTimestamp("timestamp"));

				wartungList.add(wartung);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(wartungList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return wartungList;
	}

	@Override
	public Wartung getWartung(int wartungId) throws DAOException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		Wartung wartung;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_WARTUNG);
			ps.setInt(1, wartungId);
			rs = ps.executeQuery();
			rs.next();

			wartung = new Wartung();

			wartung.setId(rs.getInt("id"));
			wartung.setAuftrag(rs.getString("auftragNr"));
			wartung.setDate(rs.getDate("faellig"));
			wartung.setProzent(rs.getInt("prozent"));
			wartung.setMitarbeiter(rs.getString("mitarbeiter"));
			wartung.setInfo(rs.getString("info"));
			wartung.setStatus(rs.getInt("status"));
			wartung.setTpm(rs.getBoolean("tpm"));
			wartung.setAnlageId(rs.getInt("anlage_id"));
			wartung.setStationId(rs.getInt("station_id"));
			wartung.setTimestampSql(rs.getTimestamp("timestamp"));

			if (logger.isInfoEnabled()) {
				// logger.info(wartung.getProzent());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return wartung;
	}

	@Override
	public void insertWartung(Wartung wartung) throws DAOException {
		PreparedStatement ps;
		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_WARTUNG);

			ps.setString(1, wartung.getAuftrag());
			java.sql.Date date = new Date(wartung.getDate().getTime());
			ps.setDate(2, date);
			ps.setInt(3, wartung.getProzent());
			ps.setString(4, wartung.getMitarbeiter());
			ps.setString(5, wartung.getInfo());
			ps.setInt(6, wartung.getStatus());
			if (wartung.getAnlageId() == 0) {
				ps.setNull(7, wartung.getAnlageId());
				ps.setInt(8, wartung.getStationId());

				ps.setBoolean(11, wartung.getStation().isTpm());
			}
			if (wartung.getStationId() == 0) {
				ps.setInt(7, wartung.getAnlageId());
				ps.setNull(8, wartung.getStationId());

				ps.setNull(11, 0);
			}
			wartung.setTimestampSql(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
			ps.setTimestamp(9, wartung.getTimestampSql());
			ps.setString(10, System.getProperty("user.name"));

			ps.executeUpdate();

			wartung.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(wartung);
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
	public void updateWartung(Wartung wartung) throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {

			Wartung wartungDb = getWartung(wartung.getId());

			if (wartungDb.getTimestampSql().equals(wartung.getTimestampSql())) {
				PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_WARTUNG);

				ps.setString(1, wartung.getAuftrag());
				java.sql.Date date = new Date(wartung.getDate().getTime());
				ps.setDate(2, date);
				ps.setInt(3, wartung.getProzent());
				ps.setString(4, wartung.getMitarbeiter());
				ps.setString(5, wartung.getInfo());
				ps.setInt(6, wartung.getStatus());
				ps.setTimestamp(7, timestamp);
				ps.setString(8, System.getProperty("user.name"));

				if (wartung.getAnlageId() == 0) {
					ps.setBoolean(9, wartung.getStation().isTpm());
				}

				if (wartung.getStationId() == 0) {
					ps.setNull(9, 0);
				}

				ps.setInt(10, wartung.getId());
				ps.executeUpdate();

				// Zeitstempel soll erst beschrieben werden, wenn der Befehl
				// erfolgreich ausgeführt wurde
				wartung.setTimestampSql(timestamp);

				if (logger.isInfoEnabled()) {
					logger.info(wartung.isTpm());
				}

			} else {
				throw new DAOException(TIMESTAMP_ERROR);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

}
