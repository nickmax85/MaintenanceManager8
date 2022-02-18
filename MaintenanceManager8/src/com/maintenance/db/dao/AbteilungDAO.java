package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.Abteilung;
import com.maintenance.db.util.DAOException;

public interface AbteilungDAO {

	public void deleteAbteilung(Abteilung abteilung) throws DAOException;

	public Abteilung getAbteilung(int abteilungId) throws DAOException;

	public List<Abteilung> getAllAbteilung() throws DAOException;

	public void insertAbteilung(Abteilung abteilung) throws DAOException;

	public void updateAbteilung(Abteilung abteilung) throws DAOException;

}