package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.util.DAOException;

public interface LeerflaecheDAO {

	public List<Leerflaeche> getAllLeerflaechen() throws DAOException;

	public Leerflaeche getLeerflaeche(int leerflaecheId) throws DAOException;

	public Leerflaeche getLeerflaeche(Leerflaeche leerflaeche) throws DAOException;

	public void insertLeerflaeche(Leerflaeche leerflaeche) throws DAOException;

	public void updateLeerflaeche(Leerflaeche leerflaeche) throws DAOException;

	public void deleteLeerflaeche(Leerflaeche leerflaeche) throws DAOException;

}
