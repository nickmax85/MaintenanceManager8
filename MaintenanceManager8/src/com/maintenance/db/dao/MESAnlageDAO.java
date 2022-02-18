package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.util.DAOException;

public interface MESAnlageDAO {

	public void deleteMESAnlage(MESAnlage data) throws DAOException;

	public MESAnlage getMESAnlage(int id) throws DAOException;

	public List<MESAnlage> getMESAnlagen() throws DAOException;

	public List<MESAnlage> getMESAnlagen(Anlage anlage) throws DAOException;

	public void insertMESAnlage(MESAnlage data) throws DAOException;

	public void updateMESAnlage(MESAnlage data) throws DAOException;

}