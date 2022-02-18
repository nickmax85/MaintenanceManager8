package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.Anhang;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.DAOException;

public interface AnhangDAO {

	public void deleteAnhang(Anhang anhang) throws DAOException;

	public List<Anhang> getAnhangList() throws DAOException;

	public List<Anhang> getAnhangList(Wartung wartung) throws DAOException;

	public List<Anhang> getAnhangList(int stationId) throws DAOException;
	
	public List<Anhang> getAnhangList(Anlage anlage) throws DAOException;

	public void insertAnhang(Anhang anhang) throws DAOException;

	public boolean getAnhangAnzahl(Wartung wartung) throws DAOException;

	public boolean getAnhangAnzahl(Station station) throws DAOException;

	public boolean getAnhangAnzahl(Anlage anlage) throws DAOException;

}