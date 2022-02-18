package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.util.DAOException;
import com.maintenance.model.User;

public interface UserDAO {

	public void deleteUser(User user) throws DAOException;

	public List<User> getAllUser() throws DAOException;

	public void insertUser(User user) throws DAOException;

	public User selectUser(int userId) throws DAOException;

	public void updateUser(User user) throws DAOException;

}