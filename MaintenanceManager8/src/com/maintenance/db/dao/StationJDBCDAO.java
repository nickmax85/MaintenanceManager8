package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class StationJDBCDAO implements StationDAO {

	private static final Logger logger = Logger.getLogger(StationJDBCDAO.class);

	private final static String TIMESTAMP_ERROR = "Ein Benutzer hat die Daten gerade verändert.\nBitte öffnen Sie das Fenster erneut oder drücken sie die F5 Taste.";

	private final static String GET_ALLSTATIONEN_FROM_ANLAGE = "SELECT * FROM station where anlageId = ? ORDER BY name ASC";
	private final static String GET_STATION = "SELECT * FROM station WHERE id = ?";
	private final static String UPDATE_STATION = "UPDATE station SET name = ?, equipment = ?, auftragNr = ?, wartungStueckIntervall = ?, wartungDateIntervall = ?, intervallDateUnit = ?, lastWartungStueck = ?, lastWartungDate = ?, wartungStueckWarnung = ?, wartungStueckFehler = ?, wartungDateWarnung = ?, warnungDateUnit = ?, auswertung = ?, wartungArt = ?, wartungsPlanLink = ?, createDate = ?, timestamp = ?, user = ?, status = ?, tpm = ?, robot = ?, mailSent = ? WHERE id = ?";
	private final static String UPDATE_STATIONSTATUS = "UPDATE station SET status = ? WHERE id = ?";
	private final static String INSERT_STATION = "INSERT INTO station(name, equipment, auftragNr, wartungStueckIntervall, wartungDateIntervall, intervallDateUnit, lastWartungStueck, lastWartungDate, wartungStueckWarnung, wartungStueckFehler, wartungDateWarnung, warnungDateUnit, auswertung, wartungArt, wartungsPlanLink, createDate, timestamp, user, status, tpm, robot, mailSent, anlageId, panelFormatId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	private final static String DELETE_STATION = "DELETE FROM station WHERE id= ?";

	private final static String GET_WARTUNGENSTATION_DATE = "SELECT * FROM wartung WHERE (faellig BETWEEN ? AND ?) AND (station_id = ?) ";

	@Override
	public void deleteStation(Station station) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_STATION);
			ps.setInt(1, station.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(station);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	private void setSingleResult(ResultSet rs, Station station) throws SQLException {

		station.setId(rs.getInt("id"));
		station.setName(rs.getString("name"));
		station.setEquipment(rs.getString("equipment"));
		station.setAuftrag(rs.getString("auftragNr"));
		station.setAuswertung(rs.getBoolean("auswertung"));
		station.setStatus(rs.getBoolean("status"));
		station.setTpm(rs.getBoolean("tpm"));
		station.setRobot(rs.getBoolean("robot"));
		station.setMailSent(rs.getBoolean("mailSent"));

		station.setWartungArt(rs.getInt("wartungArt"));
		station.setWartungsplanLink(rs.getString("wartungsPlanLink"));

		station.setWartungStueckIntervall(rs.getInt("wartungStueckIntervall"));

		station.setWartungDateIntervall(rs.getInt("wartungDateIntervall"));
		station.setIntervallDateUnit(rs.getInt("intervallDateUnit"));

		station.setLastWartungStueckzahl(rs.getInt("lastWartungStueck"));
		station.setLastWartungDate(rs.getDate("lastWartungDate"));

		station.setWartungStueckWarnung(rs.getInt("wartungStueckWarnung"));
		station.setWartungStueckFehler(rs.getInt("wartungStueckFehler"));
		station.setWartungDateWarnung(rs.getInt("wartungDateWarnung"));
		station.setWarnungDateUnit(rs.getInt("warnungDateUnit"));

		station.setCreateDate(rs.getDate("createDate"));
		station.setTimestampSql(rs.getTimestamp("timestamp"));
		station.setUser(rs.getString("user"));
		station.setPanelFormatId(rs.getInt("panelFormatId"));
		station.setAnlageId(rs.getInt("anlageId"));

	}

	@Override
	public List<Station> getAllStationenFromAnlage(Anlage anlage) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		List<Station> stationenList = new ArrayList<Station>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_ALLSTATIONEN_FROM_ANLAGE);
			ps.setInt(1, anlage.getId());
			rs = ps.executeQuery();

			while (rs.next()) {
				Station station = new Station();
				setSingleResult(rs, station);
				stationenList.add(station);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(stationenList);
			}

			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return stationenList;
	}

	@Override
	public Station getStation(int projektId) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		Station station;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_STATION);
			ps.setInt(1, projektId);
			rs = ps.executeQuery();
			rs.next();

			station = new Station();
			setSingleResult(rs, station);

			if (logger.isInfoEnabled()) {
				logger.info(station.getName());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return station;
	}

	@Override
	public List<Wartung> getWartungenStationDate(Station station, Date start, Date end) throws DAOException {
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<Wartung> wartungList = new ArrayList<Wartung>();

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(GET_WARTUNGENSTATION_DATE);

			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2, new java.sql.Date(end.getTime()));
			ps.setInt(3, station.getId());

			rs = ps.executeQuery();

			while (rs.next()) {
				Wartung wartung = new Wartung();
				wartung.setId(rs.getInt("id"));
				wartung.setAuftrag(rs.getString("auftragNr"));
				wartung.setDate(rs.getDate("faellig"));
				wartung.setProzent(rs.getInt("prozent"));
				wartung.setMitarbeiter(rs.getString("mitarbeiter"));
				wartung.setStatus(rs.getInt("status"));
				wartung.setInfo(rs.getString("info"));
				wartung.setTimestampSql(rs.getTimestamp("timestamp"));
				wartung.setAnlageId(rs.getInt("anlage_id"));
				wartung.setStationId(rs.getInt("station_id"));

				station = getStation(wartung.getStationId());
				wartung.setStation(station);

				if (station.isAuswertung())
					wartungList.add(wartung);

			}
			if (logger.isInfoEnabled()) {
				// logger.info(wartungList);
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return wartungList;
	}

	@Override
	public void insertStation(Station station) throws DAOException {
		PreparedStatement ps;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_STATION);

			ps.setString(1, station.getName());
			ps.setString(2, station.getEquipment());
			ps.setString(3, station.getAuftrag());

			if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

				ps.setInt(4, station.getWartungStueckIntervall());

				ps.setNull(5, station.getWartungDateIntervall());
				ps.setNull(6, station.getIntervallDateUnit());

				ps.setInt(7, station.getLastWartungStueckzahl());
				ps.setNull(8, 0);

				ps.setInt(9, station.getWartungStueckWarnung());
				ps.setInt(10, station.getWartungStueckFehler());

				ps.setNull(11, station.getWartungDateWarnung());
				ps.setNull(12, station.getWarnungDateUnit());

			}
			if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

				ps.setNull(4, station.getWartungStueckIntervall());

				ps.setInt(5, station.getWartungDateIntervall());
				ps.setInt(6, station.getIntervallDateUnit());

				ps.setNull(7, station.getLastWartungStueckzahl());
				if (station.getLastWartungDate() != null) {
					java.sql.Date date = new java.sql.Date(station.getLastWartungDate().getTime());
					ps.setDate(8, date);
				} else
					ps.setDate(8, null);

				ps.setNull(9, station.getWartungStueckWarnung());
				ps.setNull(10, station.getWartungStueckFehler());

				ps.setInt(11, station.getWartungDateWarnung());
				ps.setInt(12, station.getWarnungDateUnit());

			}

			ps.setBoolean(13, station.isAuswertung());
			ps.setInt(14, station.getWartungArt());
			ps.setString(15, station.getWartungsplanLink());

			ps.setDate(16, new java.sql.Date(station.getCreateDate().getTime()));
			station.setTimestampSql(new Timestamp(System.currentTimeMillis() / 1000 * 1000));
			ps.setTimestamp(17, station.getTimestampSql());
			ps.setString(18, System.getProperty("user.name"));
			ps.setBoolean(19, station.isStatus());
			ps.setBoolean(20, station.isTpm());
			ps.setBoolean(21, station.isRobot());
			ps.setBoolean(22, station.isMailSent());

			ps.setInt(23, station.getAnlageId());
			ps.setInt(24, station.getPanelFormatId());

			ps.executeUpdate();

			station.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(station);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		}
	}

	@Override
	public void updateStation(Station station) throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {
			Station projektDb = getStation(station.getId());

			if (projektDb.getTimestampSql().equals(station.getTimestampSql())) {
				PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_STATION);

				ps.setString(1, station.getName());
				ps.setString(2, station.getEquipment());
				ps.setString(3, station.getAuftrag());
				if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

					ps.setInt(4, station.getWartungStueckIntervall());
					ps.setNull(5, station.getWartungDateIntervall());
					ps.setNull(6, station.getIntervallDateUnit());

					ps.setInt(7, station.getLastWartungStueckzahl());
					if (station.getLastWartungDate() != null) {
						java.sql.Date date = new java.sql.Date(station.getLastWartungDate().getTime());
						ps.setDate(8, date);
					} else
						ps.setDate(8, null);

					ps.setInt(9, station.getWartungStueckWarnung());
					ps.setInt(10, station.getWartungStueckFehler());

					ps.setNull(11, station.getWartungDateWarnung());
					ps.setNull(12, station.getWarnungDateUnit());

				}
				if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					ps.setNull(4, station.getWartungStueckIntervall());
					ps.setInt(5, station.getWartungDateIntervall());

					ps.setInt(6, station.getIntervallDateUnit());

					ps.setNull(7, station.getLastWartungStueckzahl());

					if (station.getLastWartungDate() != null) {
						java.sql.Date date = new java.sql.Date(station.getLastWartungDate().getTime());
						ps.setDate(8, date);
					} else
						ps.setDate(8, null);

					ps.setNull(9, station.getWartungStueckWarnung());
					ps.setNull(10, station.getWartungStueckFehler());
					ps.setInt(11, station.getWartungDateWarnung());
					ps.setInt(12, station.getWarnungDateUnit());

				}

				ps.setBoolean(13, station.isAuswertung());
				ps.setInt(14, station.getWartungArt());
				ps.setString(15, station.getWartungsplanLink());

				if (station.getCreateDate() != null) {
					ps.setDate(16, new java.sql.Date(station.getCreateDate().getTime()));
				} else
					ps.setDate(16, null);
				ps.setTimestamp(17, timestamp);
				ps.setString(18, System.getProperty("user.name"));
				ps.setBoolean(19, station.isStatus());
				ps.setBoolean(20, station.isTpm());
				ps.setBoolean(21, station.isRobot());
				ps.setBoolean(22, station.isMailSent());

				ps.setInt(23, station.getId());

				ps.executeUpdate();

				// Zeitstempel soll erst beschrieben werden, wenn der Befehl
				// erfolgreich ausgeführt wurde
				station.setTimestampSql(timestamp);

				if (logger.isInfoEnabled()) {
					logger.info(station.getName());
				}
			} else
				throw new DAOException(TIMESTAMP_ERROR);
		} catch (SQLException e) {
			throw new DAOException(e);
		}
	}

	@Override
	public void updateStationStatus(Station station) throws DAOException {
		try {
			PreparedStatement ps = ConnectionManager.getInstance().getConnection()
					.prepareStatement(UPDATE_STATIONSTATUS);

			ps.setBoolean(1, station.isStatus());
			ps.setInt(2, station.getId());

			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(station);
			}

		} catch (SQLException e) {
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

}