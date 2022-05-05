package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class AnlageJDBCDAO implements AnlageDAO {

	private static final Logger logger = Logger.getLogger(AnlageJDBCDAO.class);

	private final static String TIMESTAMP_ERROR = "Ein Benutzer hat die Daten gerade verändert.\nBitte öffnen Sie das Fenster erneut oder drücken sie die F5 Taste.";

	private final static String GET_ALL_ANLAGEN = "SELECT * FROM anlage ORDER BY name ASC";
	private final static String GET_ANLAGE = "SELECT * FROM anlage WHERE id = ?";

	private final static String UPDATE_ANLAGE = "UPDATE anlage SET name = ?, equipment = ?, auftragNr = ?, jahresStueck = ?, aktuelleStueck = ?, wartungStueckIntervall = ?, wartungDateIntervall = ?, intervallDateUnit = ?, lastWartungStueck = ?, lastWartungDate = ?, wartungStueckWarnung = ?, wartungStueckFehler = ?, wartungDateWarnung = ?, warnungDateUnit = ?, wartungUeberfaellig = ?, auswertung = ?, subMenu = ?, wartungArt = ?, wartungsPlanLink = ?,  tpmStep = ?, createDate = ?, timestamp = ?, user = ?, status = ?, produkte = ?, panelFormatId = ?, abteilungId = ? WHERE id = ?";
	private final static String UPDATE_ANLAGESTATUS = "UPDATE anlage SET status = ? WHERE id = ?";

	private final static String INSERT_ANLAGE = "INSERT INTO anlage(name, equipment, auftragNr, jahresStueck, aktuelleStueck, wartungStueckIntervall, wartungDateIntervall, intervallDateUnit, lastWartungStueck, lastWartungDate, wartungStueckWarnung, wartungStueckFehler, wartungDateWarnung, warnungDateUnit, wartungUeberfaellig, auswertung, subMenu, wartungArt, wartungsPlanLink, tpmStep, createDate, timestamp, user, status, produkte, panelFormatId, abteilungId) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

	private final static String DELETE_ANLAGE = "DELETE FROM anlage WHERE id= ?";

	private final static String GET_WARTUNG_DATE = "SELECT * FROM wartung WHERE faellig BETWEEN ? AND ? AND (anlage_id IS NOT NULL)";
	private final static String GET_WARTUNGENANLAGE_DATE = "SELECT * FROM wartung WHERE (faellig BETWEEN ? AND ?) AND (anlage_id = ?)";

	@Override
	public void deleteAnlage(Anlage anlage) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_ANLAGE);
			ps.setInt(1, anlage.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(anlage);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<Anlage> getAllAnlagen() throws DAOException {

		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<Anlage> anlagenList = new ArrayList<Anlage>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALL_ANLAGEN);

			while (rs.next()) {
				Anlage anlage = new Anlage();
				setSingleResult(rs, anlage);

				anlagenList.add(anlage);

			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return anlagenList;
	}

	private void setSingleResult(ResultSet rs, Anlage anlage) throws SQLException {

		anlage.setId(rs.getInt("id"));
		anlage.setName(rs.getString("name"));
		anlage.setEquipment(rs.getString("equipment"));
		anlage.setAuftrag(rs.getString("auftragNr"));
		anlage.setJahresStueck(rs.getInt("jahresStueck"));

		anlage.setAktuelleStueck(rs.getInt("aktuelleStueck"));
		anlage.setWartungStueckIntervall(rs.getInt("wartungStueckIntervall"));

		anlage.setWartungDateIntervall(rs.getInt("wartungDateIntervall"));
		anlage.setIntervallDateUnit(rs.getInt("intervallDateUnit"));

		anlage.setLastWartungStueckzahl(rs.getInt("lastWartungStueck"));
		anlage.setLastWartungDate(rs.getDate("lastWartungDate"));

		anlage.setWartungStueckWarnung(rs.getInt("wartungStueckWarnung"));
		anlage.setWartungStueckFehler(rs.getInt("wartungStueckFehler"));
		anlage.setWartungDateWarnung(rs.getInt("wartungDateWarnung"));
		anlage.setWarnungDateUnit(rs.getInt("warnungDateUnit"));
		anlage.setWartungUeberfaellig(rs.getInt("wartungUeberfaellig"));

		anlage.setAuswertung(rs.getBoolean("auswertung"));
		anlage.setSubMenu(rs.getBoolean("subMenu"));
		anlage.setWartungArt(rs.getInt("wartungArt"));
		anlage.setStatus(rs.getBoolean("status"));
		anlage.setProdukte(rs.getString("produkte"));

		anlage.setWartungsplanLink(rs.getString("wartungsPlanLink"));
		anlage.setTpmStep(rs.getInt("tpmStep"));

		anlage.setCreateDate(rs.getDate("createDate"));
		anlage.setTimestampSql(rs.getTimestamp("timestamp"));
		anlage.setUser(rs.getString("user"));

		anlage.setAbteilungId(rs.getInt("abteilungId"));
		anlage.setPanelFormatId(rs.getInt("panelFormatId"));

	}

	@Override
	public Anlage getAnlage(Anlage anlage) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_ANLAGE);

			ps.setInt(1, anlage.getId());
			rs = ps.executeQuery();

			if (rs.next()) {
				setSingleResult(rs, anlage);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(anlage.getName());
			}
			con.close();

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return anlage;
	}

	@Override
	public Anlage getAnlage(int anlageId) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		Anlage anlage = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_ANLAGE);

			ps.setInt(1, anlageId);
			rs = ps.executeQuery();

			if (rs.next()) {
				anlage = new Anlage();
				setSingleResult(rs, anlage);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(anlage.getName());
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return anlage;
	}

	@Override
	public List<Wartung> getWartungenAnlageDate(Anlage anlage, Date start, Date end) throws DAOException {
		ResultSet rs = null;
		PreparedStatement ps = null;

		List<Wartung> wartungList = new ArrayList<Wartung>();

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(GET_WARTUNGENANLAGE_DATE);

			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2, new java.sql.Date(end.getTime()));
			ps.setInt(3, anlage.getId());

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
				wartung.setTimestampSql(rs.getTimestamp("timestamp"));
				wartung.setAnlageId(rs.getInt("anlage_Id"));

				anlage = getAnlage(wartung.getAnlageId());
				wartung.setAnlage(anlage);

				if (anlage.isAuswertung())
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
	public List<Wartung> getWartungenDate(Date start, Date end) throws DAOException {

		ResultSet rs = null;
		PreparedStatement ps = null;

		List<Wartung> wartungList = new ArrayList<Wartung>();

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(GET_WARTUNG_DATE);

			ps.setDate(1, new java.sql.Date(start.getTime()));
			ps.setDate(2, new java.sql.Date(end.getTime()));

			rs = ps.executeQuery();

			while (rs.next()) {
				Wartung wartung = new Wartung();
				wartung.setId(rs.getInt("id"));
				wartung.setAuftrag(rs.getString("auftragNr"));
				wartung.setDate(rs.getDate("faellig"));
				wartung.setProzent(rs.getInt("prozent"));
				wartung.setMitarbeiter(rs.getString("mitarbeiter"));
				wartung.setInfo(rs.getString("info"));
				wartung.setTimestampSql(rs.getTimestamp("timestamp"));
				wartung.setAnlageId(rs.getInt("anlage_Id"));

				Anlage anlage = new Anlage();
				anlage = getAnlage(wartung.getAnlageId());
				wartung.setAnlage(anlage);

				if (anlage.isStatus() && anlage.isAuswertung())
					wartungList.add(wartung);

			}
			if (logger.isInfoEnabled()) {
				logger.info(wartungList);
			}

		} catch (SQLException e) {

			throw new DAOException(e);
		}

		return wartungList;
	}

	public Integer selectLastID() throws DAOException {

		PreparedStatement ps = null;
		Integer lastId = null;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement("select last_insert_id()");
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

	private boolean compareTimestamp(Timestamp ts1, Timestamp ts2) {

		if (ts1.equals(ts2))
			return true;
		else
			return false;

	}

	@Override
	public void insertAnlage(Anlage anlage) throws DAOException {

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		PreparedStatement ps = null;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_ANLAGE);

			ps.setString(1, anlage.getName());
			ps.setString(2, anlage.getEquipment());
			ps.setString(3, anlage.getAuftrag());

			if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
				ps.setInt(4, anlage.getJahresStueck());
				ps.setInt(5, anlage.getAktuelleStueck());

				ps.setInt(6, anlage.getWartungStueckIntervall());
				ps.setNull(7, anlage.getWartungDateIntervall());
				ps.setNull(8, anlage.getIntervallDateUnit());

				ps.setInt(9, anlage.getLastWartungStueckzahl());
				ps.setNull(10, 0);

				ps.setInt(11, anlage.getWartungStueckWarnung());
				ps.setInt(12, anlage.getWartungStueckFehler());
				ps.setNull(13, anlage.getWartungDateWarnung());
				ps.setNull(14, anlage.getWarnungDateUnit());

			}

			if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
				ps.setNull(4, anlage.getJahresStueck());
				ps.setNull(5, anlage.getAktuelleStueck());

				ps.setNull(6, anlage.getWartungStueckIntervall());
				ps.setInt(7, anlage.getWartungDateIntervall());
				ps.setInt(8, anlage.getIntervallDateUnit());

				ps.setNull(9, anlage.getLastWartungStueckzahl());
				if (anlage.getLastWartungDate() != null) {
					java.sql.Date date = new java.sql.Date(anlage.getLastWartungDate().getTime());
					ps.setDate(10, date);
				} else
					ps.setDate(10, null);

				ps.setNull(11, anlage.getWartungStueckWarnung());
				ps.setNull(12, anlage.getWartungStueckFehler());
				ps.setInt(13, anlage.getWartungDateWarnung());
				ps.setInt(14, anlage.getWarnungDateUnit());

			}
			ps.setInt(15, anlage.getWartungUeberfaellig());
			ps.setBoolean(16, anlage.isAuswertung());
			ps.setBoolean(17, anlage.isSubMenu());
			ps.setInt(18, anlage.getWartungArt());
			ps.setString(19, anlage.getWartungsplanLink());

			ps.setDate(20, new java.sql.Date(anlage.getCreateDate().getTime()));
			anlage.setTimestampSql(timestamp);
			ps.setTimestamp(21, anlage.getTimestampSql());

			ps.setString(22, System.getProperty("user.name"));
			ps.setBoolean(23, anlage.isStatus());
			ps.setString(24, anlage.getProdukte());

			ps.setInt(25, anlage.getPanelFormatId());
			ps.setInt(26, anlage.getAbteilungId());

			ps.executeUpdate();

			anlage.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				logger.info(anlage);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		}

	}

	@Override
	public void updateAnlage(Anlage anlage) throws DAOException {

		PreparedStatement ps = null;

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {

			Anlage anlageInDB = getAnlage(anlage.getId());

			if (compareTimestamp(anlageInDB.getTimestampSql(), anlage.getTimestampSql())) {

				ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_ANLAGE);

				ps.setString(1, anlage.getName());
				ps.setString(2, anlage.getEquipment());
				ps.setString(3, anlage.getAuftrag());

				if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
					// die Stückzahlen dürfen nicht überschrieben werden, ausser es wird vom 2017+2018 importiert
					ps.setInt(4, anlage.getJahresStueck());
					//ps.setInt(5, anlage.getAktuelleStueck());
					ps.setInt(5, getAnlage(anlage.getId()).getAktuelleStueck());

					ps.setInt(6, anlage.getWartungStueckIntervall());
					ps.setNull(7, anlage.getWartungDateIntervall());
					ps.setNull(8, anlage.getIntervallDateUnit());

					ps.setInt(9, anlage.getLastWartungStueckzahl());

					if (anlage.getLastWartungDate() != null) {
						java.sql.Date date = new java.sql.Date(anlage.getLastWartungDate().getTime());
						ps.setDate(10, date);
					} else
						ps.setDate(10, null);

					ps.setInt(11, anlage.getWartungStueckWarnung());
					ps.setInt(12, anlage.getWartungStueckFehler());

					ps.setNull(13, anlage.getWartungDateWarnung());
					ps.setNull(14, anlage.getWarnungDateUnit());
				}

				if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					ps.setNull(4, anlage.getJahresStueck());				
					// die Stückzahlen dürfen nicht überschrieben werden, ausser es wird vom 2017+2018 importiert
					//ps.setNull(5, anlage.getAktuelleStueck());
					ps.setNull(5, getAnlage(anlage.getId()).getAktuelleStueck());

					ps.setNull(6, anlage.getWartungStueckIntervall());
					ps.setInt(7, anlage.getWartungDateIntervall());
					ps.setInt(8, anlage.getIntervallDateUnit());

					ps.setNull(9, anlage.getLastWartungStueckzahl());

					if (anlage.getLastWartungDate() != null) {
						java.sql.Date date = new java.sql.Date(anlage.getLastWartungDate().getTime());
						ps.setDate(10, date);
					} else
						ps.setDate(10, null);

					ps.setNull(11, anlage.getWartungStueckWarnung());
					ps.setNull(12, anlage.getWartungStueckFehler());
					ps.setInt(13, anlage.getWartungDateWarnung());
					ps.setInt(14, anlage.getWarnungDateUnit());
				}

				ps.setInt(15, anlage.getWartungUeberfaellig());
				ps.setBoolean(16, anlage.isAuswertung());
				ps.setBoolean(17, anlage.isSubMenu());
				ps.setInt(18, anlage.getWartungArt());
				ps.setString(19, anlage.getWartungsplanLink());
				ps.setInt(20, anlage.getTpmStep());

				if (anlage.getCreateDate() != null) {
					ps.setDate(21, new java.sql.Date(anlage.getCreateDate().getTime()));
				} else
					ps.setDate(21, null);

				anlage.setTimestampSql(timestamp);
				ps.setTimestamp(22, anlage.getTimestampSql());

				ps.setString(23, System.getProperty("user.name"));
				ps.setBoolean(24, anlage.isStatus());
				ps.setString(25, anlage.getProdukte());

				ps.setInt(26, anlage.getPanelFormatId());
				ps.setInt(27, anlage.getAbteilungId());

				ps.setInt(28, anlage.getId());

				ps.executeUpdate();
				// Zeitstempel erst beschreiben, wenn der Befehl erfolgreich
				// ausgeführt wurde
				anlage.setTimestampSql(timestamp);

				if (logger.isInfoEnabled()) {
					logger.info(anlage.getName());
				}
			} else
				throw new DAOException(TIMESTAMP_ERROR);
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}
	
	@Override
	public void updateAnlageStueckzahl(Anlage anlage) throws DAOException {

		PreparedStatement ps = null;

		Timestamp timestamp = new Timestamp(System.currentTimeMillis() / 1000 * 1000);

		try {

			Anlage anlageInDB = getAnlage(anlage.getId());

			if (compareTimestamp(anlageInDB.getTimestampSql(), anlage.getTimestampSql())) {

				ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_ANLAGE);

				ps.setString(1, anlage.getName());
				ps.setString(2, anlage.getEquipment());
				ps.setString(3, anlage.getAuftrag());

				if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {				
					// die Stückzahlen dürfen nicht überschrieben werden, ausser es wird vom 2017+2018 importiert
					ps.setInt(4, anlage.getJahresStueck());
					ps.setInt(5, anlage.getAktuelleStueck());
					//ps.setInt(5, getAnlage(anlage.getId()).getAktuelleStueck());

					ps.setInt(6, anlage.getWartungStueckIntervall());
					ps.setNull(7, anlage.getWartungDateIntervall());
					ps.setNull(8, anlage.getIntervallDateUnit());

					ps.setInt(9, anlage.getLastWartungStueckzahl());

					if (anlage.getLastWartungDate() != null) {
						java.sql.Date date = new java.sql.Date(anlage.getLastWartungDate().getTime());
						ps.setDate(10, date);
					} else
						ps.setDate(10, null);

					ps.setInt(11, anlage.getWartungStueckWarnung());
					ps.setInt(12, anlage.getWartungStueckFehler());

					ps.setNull(13, anlage.getWartungDateWarnung());
					ps.setNull(14, anlage.getWarnungDateUnit());
				}

				if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					ps.setNull(4, anlage.getJahresStueck());					
					// die Stückzahlen dürfen nicht überschrieben werden, ausser es wird vom 2017+2018 importiert
					ps.setNull(5, anlage.getAktuelleStueck());
					//ps.setNull(5, getAnlage(anlage.getId()).getAktuelleStueck());

					ps.setNull(6, anlage.getWartungStueckIntervall());
					ps.setInt(7, anlage.getWartungDateIntervall());
					ps.setInt(8, anlage.getIntervallDateUnit());

					ps.setNull(9, anlage.getLastWartungStueckzahl());

					if (anlage.getLastWartungDate() != null) {
						java.sql.Date date = new java.sql.Date(anlage.getLastWartungDate().getTime());
						ps.setDate(10, date);
					} else
						ps.setDate(10, null);

					ps.setNull(11, anlage.getWartungStueckWarnung());
					ps.setNull(12, anlage.getWartungStueckFehler());
					ps.setInt(13, anlage.getWartungDateWarnung());
					ps.setInt(14, anlage.getWarnungDateUnit());
				}

				ps.setInt(15, anlage.getWartungUeberfaellig());
				ps.setBoolean(16, anlage.isAuswertung());
				ps.setBoolean(17, anlage.isSubMenu());
				ps.setInt(18, anlage.getWartungArt());
				ps.setString(19, anlage.getWartungsplanLink());
				ps.setInt(20, anlage.getTpmStep());

				if (anlage.getCreateDate() != null) {
					ps.setDate(21, new java.sql.Date(anlage.getCreateDate().getTime()));
				} else
					ps.setDate(21, null);

				anlage.setTimestampSql(timestamp);
				ps.setTimestamp(22, anlage.getTimestampSql());

				ps.setString(23, System.getProperty("user.name"));
				ps.setBoolean(24, anlage.isStatus());
				ps.setString(25, anlage.getProdukte());

				ps.setInt(26, anlage.getPanelFormatId());
				ps.setInt(27, anlage.getAbteilungId());

				ps.setInt(28, anlage.getId());

				ps.executeUpdate();
				// Zeitstempel erst beschreiben, wenn der Befehl erfolgreich
				// ausgeführt wurde
				anlage.setTimestampSql(timestamp);

				if (logger.isInfoEnabled()) {
					logger.info(anlage.getName());
				}
			} else
				throw new DAOException(TIMESTAMP_ERROR);
		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

	@Override
	public void updateAnlagenStatus(Anlage anlage) throws DAOException {

		PreparedStatement ps = null;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_ANLAGESTATUS);

			ps.setBoolean(1, anlage.isStatus());
			ps.setInt(2, anlage.getId());

			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(anlage);
			}

		} catch (SQLException e) {
			throw new DAOException(e);
		}

	}

}