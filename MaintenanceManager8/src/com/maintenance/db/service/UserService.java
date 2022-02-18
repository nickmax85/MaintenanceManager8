package com.maintenance.db.service;

import java.util.List;

import com.maintenance.db.dao.UserHibernateDAO;
import com.maintenance.db.dao.UserHibernateJDBCDAO;
import com.maintenance.db.util.DAOException;
import com.maintenance.model.User;

public class UserService {

	private static UserHibernateJDBCDAO userDAO;

	public UserService() {

		userDAO = new UserHibernateJDBCDAO();

	}

	public void insert(User entity) {
		userDAO.openCurrentSession();
		try {
			userDAO.insert(entity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			userDAO.closeCurrentSession();
		}
	}

	public void update(User entity) {
		userDAO.openCurrentSession();
		try {
			userDAO.update(entity);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			userDAO.closeCurrentSession();
		}
	}

	public User findById(int id) {
		userDAO.openCurrentSession();
		User data = null;
		try {
			data = userDAO.get(id);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			userDAO.closeCurrentSession();
		}
		return data;
	}

	public void delete(User data) {
		userDAO.openCurrentSession();
		try {
			userDAO.delete(data);
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			userDAO.closeCurrentSession();
		}

	}

	public List<User> findAll() {
		userDAO.openCurrentSession();
		List<User> data = null;
		try {
			data = userDAO.getAll();
		} catch (DAOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			userDAO.closeCurrentSession();
		}
		return data;
	}

	public UserHibernateDAO userDAO() {
		return userDAO;
	}

}
