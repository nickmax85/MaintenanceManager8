package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.AnlageUser;
import com.maintenance.db.util.DAOException;

public interface AnlageUserDAO {

	public void deleteAnlageUser(AnlageUser anlageUser) throws DAOException;

	public void insertAnlageUser(AnlageUser anlageUser) throws DAOException;

	public List<AnlageUser> selectAnlagenUser(int anlageId) throws DAOException;

	public AnlageUser selectAnlageUser(int anlageId, int userId) throws DAOException;

	public void updateAnlageUser(AnlageUser anlageUser) throws DAOException;

}