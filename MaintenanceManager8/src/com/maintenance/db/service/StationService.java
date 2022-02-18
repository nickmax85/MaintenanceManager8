package com.maintenance.db.service;

import java.util.List;

import com.maintenance.db.dao.StationHibernateDAO;
import com.maintenance.db.dao.StationHibernateJDBCDAO;
import com.maintenance.db.util.DAOException;
import com.maintenance.model.Station;

public class StationService {

	private static StationHibernateJDBCDAO stationDAO;

	public StationService() {

		stationDAO = new StationHibernateJDBCDAO();

	}

	public void insert(Station entity) {
		stationDAO.openCurrentSession();
		try {
			stationDAO.insert(entity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stationDAO.closeCurrentSession();
		}
	}

	public void update(Station entity) {
		stationDAO.openCurrentSession();
		try {
			stationDAO.update(entity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stationDAO.closeCurrentSession();
		}
	}

	public Station findById(int id) {
		stationDAO.openCurrentSession();
		Station data = null;
		try {
			data = stationDAO.get(id);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stationDAO.closeCurrentSession();
		}
		return data;
	}

	public void delete(Station data) {
		stationDAO.openCurrentSession();
		try {
			stationDAO.delete(data);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stationDAO.closeCurrentSession();
		}

	}

	public List<Station> findAll() {
		stationDAO.openCurrentSession();
		List<Station> data = null;
		try {
			data = stationDAO.getAll();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			stationDAO.closeCurrentSession();
		}
		return data;
	}

	public StationHibernateDAO stationDAO() {
		return stationDAO;
	}

}
