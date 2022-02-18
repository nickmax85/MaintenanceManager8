package com.maintenance.db.dao;

import java.util.List;

import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.util.DAOException;

public interface PanelFormatDAO {

	public void deletPanelFormat(PanelFormat panelFormat) throws DAOException;

	public List<PanelFormat> getAllPanelFormat() throws DAOException;

	public PanelFormat getPanelFormat(int panelFormatId) throws DAOException;

	public void insertPanelFormat(PanelFormat panelFormat) throws DAOException;

	public void updatePanelFormat(PanelFormat panelFormat) throws DAOException;

}