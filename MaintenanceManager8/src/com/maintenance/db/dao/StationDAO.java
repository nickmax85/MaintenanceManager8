package com.maintenance.db.dao;

import java.util.Date;
import java.util.List;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.DAOException;

public interface StationDAO {

	public void deleteStation(Station station) throws DAOException;

	public List<Station> getAllStationenFromAnlage(Anlage anlage) throws DAOException;

	public Station getStation(int stationId) throws DAOException;

	public List<Wartung> getWartungenStationDate(Station station, Date start, Date end) throws DAOException;

	public void insertStation(Station station) throws DAOException;

	public void updateStation(Station station) throws DAOException;

	public void updateStationStatus(Station station) throws DAOException;

}