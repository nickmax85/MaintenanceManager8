package com.maintenance.db.dao;

import java.util.Date;
import java.util.List;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.DAOException;

public interface AnlageDAO {

	public void deleteAnlage(Anlage anlage) throws DAOException;

	public List<Anlage> getAllAnlagen() throws DAOException;

	public Anlage getAnlage(int anlageId) throws DAOException;

	public Anlage getAnlage(Anlage anlage) throws DAOException;

	public List<Wartung> getWartungenAnlageDate(Anlage anlage, Date start, Date end) throws DAOException;

	public List<Wartung> getWartungenDate(Date start, Date end) throws DAOException;

	public void insertAnlage(Anlage anlage) throws DAOException;

	public void updateAnlage(Anlage anlage) throws DAOException;
	
	public void updateAnlageStueckzahl(Anlage anlage) throws DAOException;

	public void updateAnlagenStatus(Anlage anlage) throws DAOException;

}