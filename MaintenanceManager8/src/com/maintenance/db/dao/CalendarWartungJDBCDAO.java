package com.maintenance.db.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.util.ConnectionManager;
import com.maintenance.db.util.DAOException;

public class CalendarWartungJDBCDAO implements CalendarWartungDAO {

	private static final Logger logger = Logger.getLogger(CalendarWartungJDBCDAO.class);

	private final static String GET_ALL_CALENDAR_WARTUNGEN_FROM_ANLAGE = "SELECT * FROM CalendarWartung WHERE anlage_id = ? ORDER BY date ASC";
	private final static String GET_ALL_CALENDAR_WARTUNG = "SELECT * FROM CalendarWartung ORDER BY date ASC";
	private final static String GET_CALENDAR_WARTUNG = "SELECT * FROM CalendarWartung where id = ?";
	private final static String GET_NEXT_CALENDAR_WARTUNG = "SELECT * FROM CalendarWartung WHERE anlage_id = ? and date >= ? ORDER BY date ASC limit 1";
	private final static String INSERT_CALENDAR_WARTUNG = "INSERT INTO CalendarWartung(date, remark, anlage_id) VALUES (?, ?, ?)";
	private final static String UPDATE_CALENDAR_WARTUNG = "UPDATE CalendarWartung SET date = ?, remark = ? WHERE id = ?";
	private final static String DELETE_CALENDAR_WARTUNG = "DELETE FROM CalendarWartung WHERE id= ?";

	@Override
	public void deleteCalendarWartung(CalendarWartung data) throws DAOException {
		Connection con = null;
		PreparedStatement ps = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(DELETE_CALENDAR_WARTUNG);
			ps.setInt(1, data.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(data);
			}

		} catch (SQLException e) {
			throw new DAOException(e);

		}

	}

	@Override
	public List<CalendarWartung> getCalendarWartungenFromAnlage(Anlage anlage) throws DAOException {

		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		List<CalendarWartung> calendarWartungenList = new ArrayList<CalendarWartung>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_ALL_CALENDAR_WARTUNGEN_FROM_ANLAGE);
			ps.setInt(1, anlage.getId());
			rs = ps.executeQuery();

			while (rs.next()) {
				CalendarWartung data = new CalendarWartung();
				data.setId(rs.getInt("id"));
				data.setDate(rs.getDate("date"));
				data.setRemark(rs.getString("remark"));

				data.setAnlage(anlage);
				calendarWartungenList.add(data);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(stationenList);
			}
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return calendarWartungenList;
	}

	@Override
	public CalendarWartung getNextCalendarWartungFromAnlage(int id, Date lastWartungDate) throws DAOException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		CalendarWartung calendarWartung = null;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_NEXT_CALENDAR_WARTUNG);
			ps.setInt(1, id);
			if (lastWartungDate != null)
				ps.setDate(2, new java.sql.Date(lastWartungDate.getTime()));
			else
				ps.setNull(2, 0);
			rs = ps.executeQuery();

			while (rs.next()) {
				calendarWartung = new CalendarWartung();
				calendarWartung.setId(rs.getInt("id"));
				calendarWartung.setDate(rs.getDate("date"));
				calendarWartung.setRemark(rs.getString("remark"));

			}

			if (logger.isInfoEnabled()) {
				// logger.info(stationenList);
			}

			con.close();
		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return calendarWartung;
	}

	@Override
	public CalendarWartung getCalendarWartung(int wartungId) throws DAOException {
		ResultSet rs = null;
		Connection con = null;
		PreparedStatement ps = null;

		CalendarWartung data;

		try {
			con = ConnectionManager.getInstance().getConnection();

			ps = con.prepareStatement(GET_CALENDAR_WARTUNG);
			ps.setInt(1, wartungId);
			rs = ps.executeQuery();
			rs.next();

			data = new CalendarWartung();
			data.setId(new Integer(rs.getInt("id")));
			data.setDate(rs.getDate("date"));
			data.setRemark(rs.getString("name"));

			if (logger.isInfoEnabled()) {
				logger.info(data);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return data;
	}

	@Override
	public List<CalendarWartung> getAllCalendarWartung() throws DAOException {
		Statement statement = null;
		ResultSet rs = null;
		Connection con = null;

		List<CalendarWartung> dataList = new ArrayList<CalendarWartung>();

		try {
			con = ConnectionManager.getInstance().getConnection();

			statement = con.createStatement();
			rs = statement.executeQuery(GET_ALL_CALENDAR_WARTUNG);

			while (rs.next()) {
				CalendarWartung data = new CalendarWartung();
				data.setId(new Integer(rs.getInt("id")));
				data.setRemark(rs.getString("remark"));

				dataList.add(data);
			}

			if (logger.isInfoEnabled()) {
				// logger.info(dataList);
			}

		} catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

		return dataList;
	}

	@Override
	public void insertCalendarWartung(CalendarWartung data) throws DAOException {

		PreparedStatement ps;

		try {
			ps = ConnectionManager.getInstance().getConnection().prepareStatement(INSERT_CALENDAR_WARTUNG);

			java.sql.Date date = new java.sql.Date(data.getDate().getTime());
			ps.setDate(1, date);
			ps.setString(2, data.getRemark());
			ps.setInt(3, data.getAnlage().getId());

			ps.executeUpdate();

			data.setId(selectLastID());

			if (logger.isInfoEnabled()) {
				// logger.info(data);
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
				// logger.info(lastId);
			}
		} catch (SQLException e) {
			throw new DAOException(e);
		}

		return lastId;
	}

	@Override
	public void updateCalendarWartung(CalendarWartung data) throws DAOException {

		try {

			PreparedStatement ps = ConnectionManager.getInstance().getConnection()
					.prepareStatement(UPDATE_CALENDAR_WARTUNG);

			java.sql.Date date = new java.sql.Date(data.getDate().getTime());
			ps.setDate(1, date);
			ps.setString(2, data.getRemark());
			ps.setInt(3, data.getId());
			ps.executeUpdate();

			if (logger.isInfoEnabled()) {
				logger.info(data);
			}

		}

		catch (SQLException e) {
			e.printStackTrace();
			throw new DAOException(e);
		}

	}

}
