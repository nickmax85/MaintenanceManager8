package com.maintenance.db.dao;

public class DAOFactory {

	private SystemDAO systemDAO;
	private AnlageDAO anlageDAO;
	private WartungDAO wartungDAO;
	private PanelFormatDAO panelFormatDAO;
	private LeerflaecheDAO leerflaecheDAO;
	private AnhangDAO anhangDAO;
	private AbteilungDAO abteilungDAO;
	private StationDAO stationDAO;
	private UserDAO userDAO;
	private AnlageUserDAO anlageUserDAO;
	private MESAnlageDAO mesAnlageDAO;
	private CalendarWartungDAO calendarWartungDAO;

	public DAOFactory(EDAOType eDAOType) {

		if (eDAOType == EDAOType.JDBC) {
			systemDAO = new SystemJDBCDAO();
			anlageDAO = new AnlageJDBCDAO();
			wartungDAO = new WartungJDBCDAO();
			panelFormatDAO = new PanelFormatJDBCDAO();
			leerflaecheDAO = new LeerflaecheJDBCDAO();
			anhangDAO = new AnhangJDBCDAO();
			abteilungDAO = new AbteilungJDBCDAO();
			stationDAO = new StationJDBCDAO();
			userDAO = new UserJDBCDAO();
			anlageUserDAO = new AnlageUserJDBCDAO();
			mesAnlageDAO = new MESAnlageJDBCDAO();
			calendarWartungDAO = new CalendarWartungJDBCDAO();
		}

		if (eDAOType == EDAOType.HIBERNATE) {

		}

		if (eDAOType == EDAOType.MEMORY) {

		}

	}

	public AbteilungDAO getAbteilungDAO() {
		return abteilungDAO;
	}

	public AnhangDAO getAnhangDAO() {
		return anhangDAO;
	}

	public AnlageDAO getAnlageDAO() {

		return anlageDAO;
	}

	public LeerflaecheDAO getLeerflaecheDAO() {
		return leerflaecheDAO;
	}

	public PanelFormatDAO getPanelFormatDAO() {
		return panelFormatDAO;
	}

	public StationDAO getStationDAO() {
		return stationDAO;
	}

	public SystemDAO getSystemDAO() {
		return systemDAO;
	}

	public WartungDAO getWartungDAO() {
		return wartungDAO;
	}

	public AnlageUserDAO getAnlageUserDAO() {
		return anlageUserDAO;
	}

	public UserDAO getUserDAO() {
		return userDAO;
	}

	public MESAnlageDAO getMesAnlageDAO() {
		return mesAnlageDAO;
	}

	public CalendarWartungDAO getCalendarWartungDAO() {
		return calendarWartungDAO;
	}

}
