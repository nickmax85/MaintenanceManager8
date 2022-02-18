package com.maintenance.db.service;

import java.util.List;

import com.maintenance.db.dao.AnlageHibernateDAO;
import com.maintenance.db.dao.AnlageHibernateJDBCDAO;
import com.maintenance.db.util.DAOException;
import com.maintenance.model.Anlage;

public class AnlageService {

	private static AnlageHibernateJDBCDAO anlageDAO;

	public AnlageService() {

		anlageDAO = new AnlageHibernateJDBCDAO();

	}

	public void insert(Anlage entity) {
		anlageDAO.openCurrentSession();
		try {
			anlageDAO.insert(entity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			anlageDAO.closeCurrentSession();
		}
	}

	public void update(Anlage entity) {
		anlageDAO.openCurrentSession();
		try {
			anlageDAO.update(entity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			anlageDAO.closeCurrentSession();
		}
	}

	public Anlage findById(int id) {
		anlageDAO.openCurrentSession();
		Anlage data = null;
		try {
			data = anlageDAO.get(id);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			anlageDAO.closeCurrentSession();
		}
		return data;
	}

	public void delete(Anlage data) {
		anlageDAO.openCurrentSession();
		try {
			anlageDAO.delete(data);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			anlageDAO.closeCurrentSession();
		}

	}

	public List<Anlage> findAll() {
		anlageDAO.openCurrentSession();
		List<Anlage> data = null;
		try {
			data = anlageDAO.getAll();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			anlageDAO.closeCurrentSession();
		}
		return data;
	}

	public AnlageHibernateDAO anlageDAO() {
		return anlageDAO;
	}

}
