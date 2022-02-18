package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.util.DAOException;
import com.maintenance.model.Station;

public interface StationHibernateDAO {

	public void delete(Station data) throws DAOException;

	public Station get(int id) throws DAOException;

	public List<Station> getAll() throws DAOException;

	public void insert(Station data) throws DAOException;

	public void update(Station data) throws DAOException;

}
