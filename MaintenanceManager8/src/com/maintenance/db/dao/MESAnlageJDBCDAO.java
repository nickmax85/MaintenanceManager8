package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class MESAnlageJDBCDAO implements MESAnlageDAO {

	private static final Logger logger = Logger.getLogger(MESAnlageJDBCDAO.class);

	private final static String GET_MES_ANLAGEN = "SELECT * FROM mesAnlage ORDER BY id ASC";
	private final static String GET_MES_ANLAGE = "SELECT * FROM mesAnlage where id = ?";
	private final static String GET_MES_ANLAGEN_ANLAGE = "SELECT * FROM mesAnlage where anlageId = ? OR anlage2Id = ?";
	private final static String INSERT_MES_ANLAGE = "INSERT INTO mesAnlage(id, name, prodStueck) VALUES (?, ?, ?)";
	private final static String UPDATE_MES_ANLAGE = "UPDATE mesAnlage SET name = ?, prodStueck = ?, anlageId = ?, anlage2Id = ? WHERE id = ?";
	private final static String DELETE_MES_ANLAGE = "DELETE FROM mesAnlage WHERE id= ?";

	@Override
	public void deleteMESAnlage(MESAnlage mesAnlage) throws DAOException {

		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_MES_ANLAGE);
			ps.setInt(1, mesAnlage.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(mesAnlage);
			}

		} catch (SQLException e) {

			if (e.getErrorCode() == 547 || e.getErrorCode() == 1451)
				throw new DAOException("Entfernen nicht erlaubt, da die Daten verwendet werden.");
			else
				throw new DAOException(e);

		}

	}

	@Override
	public MESAnlage getMESAnlage(int anlageId) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		MESAnlage mesAnlage;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_MES_ANLAGE);
			ps.setInt(1, anlageId);
			rs = ps.executeQuery();
			rs.next();

			mesAnlage = new MESAnlage();
			mesAnlage.setId(new Integer(rs.getInt("id")));
			mesAnlage.setName(rs.getString("name"));
			mesAnlage.setProdStueck(rs.getInt("prodStueck"));
			mesAnlage.setTimestampSql(rs.getTimestamp("timestamp"));
			mesAnlage.setTimestamp(mesAnlage.getTimestampSql().toString());

			mesAnlage.setAnlageId(rs.getInt("anlageId"));
			mesAnlage.setAnlage2Id(rs.getInt("anlage2Id"));

			if (logger.isInfoEnabled()) {
				// logger.info(mesAnlage);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return mesAnlage;
	}

	@Override
	public List<MESAnlage> getMESAnlagen(Anlage anlage) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		List<MESAnlage> mesAnlagenList = new ArrayList<MESAnlage>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_MES_ANLAGEN_ANLAGE);
			ps.setInt(1, anlage.getId());
			ps.setInt(2, anlage.getId());
			rs = ps.executeQuery();

			while (rs.next()) {
				MESAnlage mesAnlage = new MESAnlage();
				mesAnlage.setId(new Integer(rs.getInt("id")));
				mesAnlage.setName(rs.getString("name"));
				mesAnlage.setProdStueck(rs.getInt("prodStueck"));
				mesAnlage.setTimestampSql(rs.getTimestamp("timestamp"));
				mesAnlage.setTimestamp(mesAnlage.getTimestampSql().toString());

				mesAnlage.setAnlageId(rs.getInt("anlageId"));
				mesAnlage.setAnlage2Id(rs.getInt("anlage2Id"));
				mesAnlagenList.add(mesAnlage);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(mesAnlagenList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return mesAnlagenList;
	}

	@Override
	public List<MESAnlage> getMESAnlagen() throws DAOException {

		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<MESAnlage> mesAnlagen = new ArrayList<MESAnlage>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_MES_ANLAGEN);

			while (rs.next()) {
				MESAnlage mesAnlage = new MESAnlage();
				mesAnlage.setId(new Integer(rs.getInt("id")));
				mesAnlage.setName(rs.getString("name"));
				mesAnlage.setProdStueck(rs.getInt("prodStueck"));
				mesAnlage.setTimestampSql(rs.getTimestamp("timestamp"));
				mesAnlage.setTimestamp(mesAnlage.getTimestampSql().toString());

				mesAnlage.setAnlageId(rs.getInt("anlageId"));
				mesAnlage.setAnlage2Id(rs.getInt("anlage2Id"));

				mesAnlagen.add(mesAnlage);
			}

			if (logger.isInfoEnabled()) {
				//logger.info(mesAnlagen);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return mesAnlagen;
	}

	@Override
	public void insertMESAnlage(MESAnlage mesAnlage) throws DAOException {

		PreparedStatement ps;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_MES_ANLAGE);

			ps.setInt(1, mesAnlage.getId());
			ps.setString(2, mesAnlage.getName());
			ps.setInt(3, mesAnlage.getProdStueck());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(mesAnlage);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);

		}

	}

	@Override
	public void updateMESAnlage(MESAnlage mesAnlage) throws DAOException {

		try {

			PreparedStatement ps = ConnectionManager.getInstance().getConnection().prepareStatement(UPDATE_MES_ANLAGE);

			ps.setString(1, mesAnlage.getName());
			ps.setInt(2, mesAnlage.getProdStueck());

			if (mesAnlage.getAnlageId() != 0)
				ps.setInt(3, mesAnlage.getAnlageId());
			else
				ps.setNull(3, mesAnlage.getAnlageId());

			if (mesAnlage.getAnlage2Id() != 0)
				ps.setInt(4, mesAnlage.getAnlage2Id());
			else
				ps.setNull(4, mesAnlage.getAnlage2Id());

			ps.setInt(5, mesAnlage.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				// logger.info(mesAnlage);
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

	}

}
