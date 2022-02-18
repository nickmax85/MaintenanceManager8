package com.maintenance.db.dao;

import java.util.Date;
import java.util.List;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.util.DAOException;

public interface CalendarWartungDAO {

	public void deleteCalendarWartung(CalendarWartung data) throws DAOException;

	public CalendarWartung getCalendarWartung(int data) throws DAOException;

	public List<CalendarWartung> getAllCalendarWartung() throws DAOException;

	public CalendarWartung getNextCalendarWartungFromAnlage(int data, Date lastWartungDate) throws DAOException;

	public void insertCalendarWartung(CalendarWartung data) throws DAOException;

	public void updateCalendarWartung(CalendarWartung data) throws DAOException;

	public List<CalendarWartung> getCalendarWartungenFromAnlage(Anlage anlage) throws DAOException;

}