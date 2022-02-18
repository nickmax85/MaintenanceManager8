package com.maintenance.db.dao;

import com.maintenance.db.util.DAOException;

public interface SystemDAO {

	public String getServerInfo() throws DAOException;

	public void insertLoggedInUser() throws DAOException;

}