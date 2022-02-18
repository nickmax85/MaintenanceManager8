package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.DAOException;

public interface WartungDAO {

	public void deleteWartung(Wartung wartung) throws DAOException;

	public List<Wartung> getAllWartungen() throws DAOException;

	public List<Wartung> getAllWartungenFromAnlage(int anlageId) throws DAOException;

	public List<Wartung> getAllWartungenFromStation(int stationId) throws DAOException;

	public Wartung getWartung(int wartungId) throws DAOException;

	public void insertWartung(Wartung wartung) throws DAOException;

	public void updateWartung(Wartung wartung) throws DAOException;

}