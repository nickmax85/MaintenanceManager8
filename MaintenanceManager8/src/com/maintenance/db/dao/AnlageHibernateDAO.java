package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.util.DAOException;
import com.maintenance.model.Anlage;

public interface AnlageHibernateDAO {

	public void delete(Anlage data) throws DAOException;

	public Anlage get(int dataId) throws DAOException;

	public List<Anlage> getAll() throws DAOException;

	public void insert(Anlage data) throws DAOException;

	public void update(Anlage data) throws DAOException;

}
