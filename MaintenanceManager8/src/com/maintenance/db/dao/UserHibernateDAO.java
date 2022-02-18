package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.util.DAOException;
import com.maintenance.model.User;

public interface UserHibernateDAO {

	public void delete(User data) throws DAOException;

	public User get(int id) throws DAOException;

	public List<User> getAll() throws DAOException;

	public void insert(User data) throws DAOException;

	public void update(User data) throws DAOException;

}