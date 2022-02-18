package com.maintenance.db.service;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.log4j.Logger;

import com.maintenance.db.dao.AbteilungDAO;
import com.maintenance.db.dao.AnhangDAO;
import com.maintenance.db.dao.AnlageDAO;
import com.maintenance.db.dao.AnlageUserDAO;
import com.maintenance.db.dao.CalendarWartungDAO;
import com.maintenance.db.dao.DAOFactory;
import com.maintenance.db.dao.EDAOType;
import com.maintenance.db.dao.LeerflaecheDAO;
import com.maintenance.db.dao.MESAnlageDAO;
import com.maintenance.db.dao.PanelFormatDAO;
import com.maintenance.db.dao.StationDAO;
import com.maintenance.db.dao.SystemDAO;
import com.maintenance.db.dao.UserDAO;
import com.maintenance.db.dao.WartungDAO;
import com.maintenance.db.dto.Abteilung;
import com.maintenance.db.dto.Anhang;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.AnlageUser;
import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.util.DAOException;
import com.maintenance.model.User;
import com.maintenance.util.Constants;

import javafx.application.Platform;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Stage;

public class Service {

	private static Service instance;

	private static final Logger logger = Logger.getLogger(Service.class);

	private final static EDAOType SOURCE = EDAOType.JDBC;

	private boolean errorStatus = false;

	private AnlageService anlageService;
	private StationService stationService;
	private UserService userService;

	private SystemDAO systemDAO;
	private AnlageDAO anlageDAO;
	private WartungDAO wartungDAO;
	private PanelFormatDAO panelFormatDAO;
	private LeerflaecheDAO leerflaecheDAO;
	private AnhangDAO anhangDAO;
	private AbteilungDAO abteilungDAO;
	private StationDAO stationDAO;
	private UserDAO userDAO;
	private MESAnlageDAO mesAnlageDAO;
	private CalendarWartungDAO calendarWartungDAO;

	private Anlage anlage;
	private List<Anlage> anlageList;

	private Wartung wartung;
	private List<Wartung> wartungList;

	private List<PanelFormat> panelFormatList;
	private PanelFormat panelFormat;

	private Leerflaeche leerflaeche;
	private List<Leerflaeche> leerflaecheList;

	private Anhang anhang;
	private List<Anhang> anhangList;

	private List<Abteilung> abteilungList;
	private Abteilung abteilung;

	private List<CalendarWartung> calendarWartungList;
	private CalendarWartung calendarWartung;

	private List<Station> stationList;
	private Station station;

	private AnlageUserDAO anlageUserDAO;
	private List<AnlageUser> anlageUserList;

	private AnlageUser anlageUser;
	private List<User> userList;

	private Service() {

		anlageService = new AnlageService();
		stationService = new StationService();
		userService = new UserService();

		DAOFactory daoFactory;
		daoFactory = new DAOFactory(SOURCE);

		systemDAO = daoFactory.getSystemDAO();
		anlageDAO = daoFactory.getAnlageDAO();
		wartungDAO = daoFactory.getWartungDAO();
		panelFormatDAO = daoFactory.getPanelFormatDAO();
		leerflaecheDAO = daoFactory.getLeerflaecheDAO();
		anhangDAO = daoFactory.getAnhangDAO();
		abteilungDAO = daoFactory.getAbteilungDAO();
		stationDAO = daoFactory.getStationDAO();
		calendarWartungDAO = daoFactory.getCalendarWartungDAO();

		userDAO = daoFactory.getUserDAO();
		anlageUserDAO = daoFactory.getAnlageUserDAO();
		mesAnlageDAO = daoFactory.getMesAnlageDAO();

		insertLoggedInUser();

	}

	public synchronized static Service getInstance() {

		if (instance == null) {
			instance = new Service();
		}

		return instance;

	}

	public int calcProzent(Anlage anlage) {

		float prozent = 0;
		float prozentUeberproduktion = 0;
		int produziert;
		int ueberproduktion;

		produziert = anlage.getAktuelleStueck() - anlage.getLastWartungStueckzahl();

		prozent = (float) 100 * produziert / anlage.getWartungStueckIntervall();

		ueberproduktion = produziert - anlage.getWartungStueckIntervall();
		prozentUeberproduktion = (float) 100 * ueberproduktion / anlage.getWartungStueckIntervall();

		if (prozent >= 100) {
			prozent = 100 + prozentUeberproduktion;
		}

		if (prozent >= 200) {
			prozent = 200 + prozentUeberproduktion;
		}

		return (int) prozent;
	}

	public int calcProzent(Station station) {

		float prozent = 0;
		float prozentUeberproduktion = 0;
		int produziert;
		int ueberproduktion;

		// if (station.getLastWartungStueckzahl() != 0) {

		produziert = station.getAnlage().getAktuelleStueck() - station.getLastWartungStueckzahl();

		prozent = (float) 100 * produziert / station.getWartungStueckIntervall();

		ueberproduktion = produziert - station.getWartungStueckIntervall();
		prozentUeberproduktion = (float) 100 * ueberproduktion / station.getWartungStueckIntervall();

		if (prozent >= 100) {
			prozent = 100 + prozentUeberproduktion;
		}

		if (prozent >= 200) {
			prozent = 200 + prozentUeberproduktion;
		}

		return (int) prozent;
	}

	public void deleteAbteilung(Abteilung abteilung) {

		try {
			abteilungDAO.deleteAbteilung(abteilung);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteCalendarWartung(CalendarWartung data) {

		try {
			calendarWartungDAO.deleteCalendarWartung(data);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteStation(Station station) {

		try {
			stationDAO.deleteStation(station);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteAnhang(Anhang anhang) {

		try {
			anhangDAO.deleteAnhang(anhang);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteAnlage(Anlage anlage) {

		try {
			anlageDAO.deleteAnlage(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteAnlageUser(AnlageUser anlageUser) {

		try {
			anlageUserDAO.deleteAnlageUser(anlageUser);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteUser(User user) {

		try {
			userDAO.deleteUser(user);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteLeerflaeche(Leerflaeche leerflaeche) {

		try {
			leerflaecheDAO.deleteLeerflaeche(leerflaeche);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deletePanelFormat(PanelFormat panelFormat) {

		try {
			panelFormatDAO.deletPanelFormat(panelFormat);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteWartung(Wartung wartung) {

		try {
			wartungDAO.deleteWartung(wartung);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void deleteMESAnlage(MESAnlage mesAnlage) {

		try {
			mesAnlageDAO.deleteMESAnlage(mesAnlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public List<Abteilung> getAllAbteilungen() {

		try {
			abteilungList = abteilungDAO.getAllAbteilung();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return abteilungList;

	}

	public List<CalendarWartung> getAllCalendarWartung() {

		try {
			calendarWartungList = calendarWartungDAO.getAllCalendarWartung();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return calendarWartungList;

	}

	public List<MESAnlage> getMESAnlagen() {

		List<MESAnlage> mesAnlagen = null;

		try {
			mesAnlagen = mesAnlageDAO.getMESAnlagen();

			for (MESAnlage mes : mesAnlagen) {
				for (Anlage anlage : anlageList) {
					if (mes.getAnlageId() == anlage.getId())
						mes.setAnlage(anlage);
				}
				for (Anlage anlage : anlageList) {
					if (mes.getAnlage2Id() == anlage.getId())
						mes.setAnlage2(anlage);
				}
			}
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return mesAnlagen;

	}

	public List<Anlage> getAllAnlageLeerflaecheAbteilungPanelFormat() {

		int stueck = 0;

		try {
			anlageList = anlageDAO.getAllAnlagen();
			abteilungList = abteilungDAO.getAllAbteilung();
			leerflaecheList = leerflaecheDAO.getAllLeerflaechen();
			panelFormatList = panelFormatDAO.getAllPanelFormat();

			doAnlageAbteilungPanelFormatMapping();

			for (Anlage anlage : anlageList) {

				// logger.info("Anlage: " + anlage.getName());
				anlage.setMesAnlagen(mesAnlageDAO.getMESAnlagen(anlage));

				stueck = anlage.getAktuelleStueck();

				for (MESAnlage mes : anlage.getMesAnlagen()) {
					stueck = mes.getProdStueck() + stueck;

				}

				anlage.setAktuelleStueck(stueck);
				// logger.info("Stueck: " + stueck);

			}

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anlageList;

	}

	public Anlage getAnlage(int anlageId) {

		int stueck = 0;

		try {
			anlage = anlageDAO.getAnlage(anlageId);
			anlage.setPanelFormat(panelFormatDAO.getPanelFormat(anlage.getPanelFormatId()));

			anlage.setMesAnlagen(mesAnlageDAO.getMESAnlagen(anlage));

			for (MESAnlage mes : anlage.getMesAnlagen()) {
				stueck = mes.getProdStueck() + stueck;
				anlage.setAktuelleStueck(stueck);
			}

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);

		}

		return anlage;

	}

	public Anlage getAnlage(Anlage anlage) {

		int stueck = 0;

		try {
			anlage = anlageDAO.getAnlage(anlage);
			anlage.setPanelFormat(panelFormatDAO.getPanelFormat(anlage.getPanelFormatId()));

			anlage.setMesAnlagen(mesAnlageDAO.getMESAnlagen(anlage));

			stueck = anlage.getAktuelleStueck();

			for (MESAnlage mes : anlage.getMesAnlagen()) {
				stueck = mes.getProdStueck() + stueck;
				anlage.setAktuelleStueck(stueck);
			}

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);

		}

		return anlage;

	}

	public List<Leerflaeche> getAllLeerflaechePanelFormat() {

		try {
			leerflaecheList = leerflaecheDAO.getAllLeerflaechen();
			panelFormatList = panelFormatDAO.getAllPanelFormat();

			doAnlageAbteilungPanelFormatMapping();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return leerflaecheList;

	}

	public List<Wartung> getAllWartungenFromAbteilungAndDate(Abteilung abteilung, Date start, Date end) {

		List<Wartung> wartungList;
		List<Wartung> wartungList2 = new ArrayList<Wartung>();

		try {

			abteilungList = abteilungDAO.getAllAbteilung();
			wartungList = anlageDAO.getWartungenDate(start, end);

			for (Wartung wartung : wartungList) {
				if (wartung.getAnlage().getAbteilungId() == abteilung.getId()) {
					wartungList2.add(wartung);
				}

			}
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}
		return wartungList2;

	}

	public List<Wartung> getAllWartungenFromAnlage(Anlage anlage) {

		try {
			wartungList = wartungDAO.getAllWartungenFromAnlage(anlage.getId());
			for (Wartung wartung : wartungList) {
				wartung.setAnlage(anlage);
			}
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return wartungList;

	}

	public boolean getAnhangAnzahlFromWartung(Wartung wartung) {

		boolean anhang = false;

		try {
			if (anhangDAO.getAnhangAnzahl(wartung))
				anhang = true;
			else
				anhang = false;

			errorStatus = false;

		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return anhang;

	}

	public boolean getAnhangAnzahlFromStation(Station station) {

		boolean anhang = false;

		try {
			if (anhangDAO.getAnhangAnzahl(station))
				anhang = true;
			else
				anhang = false;

			errorStatus = false;

		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return anhang;

	}

	public boolean getAnhangAnzahlFromAnlage(Anlage anlage) {

		boolean anhang = false;

		try {
			if (anhangDAO.getAnhangAnzahl(anlage))
				anhang = true;
			else
				anhang = false;

			errorStatus = false;

		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return anhang;

	}

	public List<Wartung> getAllWartungenFromStation(Station station) {

		try {
			wartungList = wartungDAO.getAllWartungenFromStation(station.getId());
			for (Wartung wartung : wartungList) {
				wartung.setStation(station);
			}
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return wartungList;

	}

	public List<CalendarWartung> getCalendarWartungenFromAnlage(Anlage anlage) {

		try {
			calendarWartungList = calendarWartungDAO.getCalendarWartungenFromAnlage(anlage);
			for (CalendarWartung data : calendarWartungList) {
				data.setAnlage(anlage);
			}
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return calendarWartungList;

	}

	public List<User> getAnlagenUser(int anlageId) {

		List<AnlageUser> anlagenUser;
		List<User> users;
		List<User> userList = new ArrayList<>();

		List<AnlageUser> mergedAnlagenUser = new ArrayList<AnlageUser>();

		anlagenUser = selectAnlagenUser(anlageId);
		users = getUsers();

		for (AnlageUser anlageUser : anlagenUser) {

			for (User user : users) {
				if (user.getId() == anlageUser.getUserId()) {
					anlageUser.setBenutzer(user);
					userList.add(user);
				}

			}
			mergedAnlagenUser.add(anlageUser);

		}

		return userList;
	}

	public List<User> getUsers() {

		try {
			userList = userDAO.getAllUser();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			errorStatus = true;

		}

		return userList;

	}

	public List<Anhang> getAnhangList(Wartung wartung) {

		try {
			anhangList = anhangDAO.getAnhangList(wartung);
			wartung.setAnhangList(anhangList);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anhangList;

	}

	public List<Anhang> getAnhangList(Anlage anlage) {

		try {
			anhangList = anhangDAO.getAnhangList(anlage);
			anlage.setAnhangList(anhangList);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anhangList;

	}

	public List<Anhang> getAnhangList() {

		try {
			anhangList = anhangDAO.getAnhangList();

			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					showExceptionAlertDialog(e);

				}
			});

		}

		return anhangList;

	}

	public List<Anhang> getAnhangList(Station station) {

		try {
			anhangList = anhangDAO.getAnhangList(station.getId());
			station.setAnhangList(anhangList);
			errorStatus = false;
		} catch (DAOException e) {

			e.printStackTrace();

			Platform.runLater(new Runnable() {

				@Override
				public void run() {
					showExceptionAlertDialog(e);

				}
			});

		}

		return anhangList;

	}

	public List<Anhang> getAnhangListStation(com.maintenance.model.Station station) {

		try {
			anhangList = anhangDAO.getAnhangList(station.getId());

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anhangList;

	}

	public List<Station> getAnlageStationenPanelFormat(Anlage anlage) {

		try {
			stationList = stationDAO.getAllStationenFromAnlage(anlage);
			panelFormatList = panelFormatDAO.getAllPanelFormat();

			doStationPanelFormatMapping(anlage);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return stationList;
	}

	public List<Station> getStationenFromAnlage(Anlage anlage) {

		try {
			stationList = stationDAO.getAllStationenFromAnlage(anlage);

			doStationAnlageMapping(anlage);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return stationList;
	}

	public List<Leerflaeche> getLeerflaecheList() {
		return leerflaecheList;
	}

	public String getServerInfo() {

		String info = null;

		try {
			info = systemDAO.getServerInfo();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}
		return info;

	}

	public List<Station> getStationList() {
		return stationList;
	}

	public Wartung getWartung(int lastWartungId) {

		try {
			wartung = wartungDAO.getWartung(lastWartungId);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return wartung;

	}

	public CalendarWartung getNextCalendarWartung(int anlageId, Date lastWartungDate) {

		CalendarWartung calendarWartung = null;
		try {
			calendarWartung = calendarWartungDAO.getNextCalendarWartungFromAnlage(anlageId, lastWartungDate);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return calendarWartung;

	}

	public MESAnlage getMESAnlage(int id) {

		MESAnlage mesAnlage = null;

		try {
			mesAnlage = mesAnlageDAO.getMESAnlage(id);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return mesAnlage;

	}

	public CalendarWartung getCalendarWartung(int id) {

		CalendarWartung data = null;

		try {
			data = calendarWartungDAO.getCalendarWartung(id);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

		return data;

	}

	public List<Wartung> getWartungenAnlageDate(Anlage anlage, Date start, Date end) {

		try {
			wartungList = anlageDAO.getWartungenAnlageDate(anlage, start, end);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}
		return wartungList;

	}

	public List<Wartung> getWartungenDate(Date start, Date end) {

		try {
			wartungList = anlageDAO.getWartungenDate(start, end);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}
		return wartungList;

	}

	public List<Wartung> getWartungenStationDate(Station station, Date start, Date end) {

		try {
			wartungList = stationDAO.getWartungenStationDate(station, start, end);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}
		return wartungList;

	}

	public void insertAbteilung(Abteilung abteilung) {

		try {
			abteilungDAO.insertAbteilung(abteilung);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertCalendarWartung(CalendarWartung data) {

		try {
			calendarWartungDAO.insertCalendarWartung(data);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertAnhang(Anhang anhang) {

		try {
			anhangDAO.insertAnhang(anhang);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertAnlage(Anlage anlage) {

		try {
			anlageDAO.insertAnlage(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertAnlagePanelFormat(Anlage anlage) {

		try {
			panelFormatDAO.insertPanelFormat(anlage.getPanelFormat());
			anlageDAO.insertAnlage(anlage);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertDummyProjekt(Leerflaeche leerflaeche) {

		try {
			leerflaecheDAO.insertLeerflaeche(leerflaeche);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertLoggedInUser() {

		try {
			systemDAO.insertLoggedInUser();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertPanelFormat(PanelFormat panelFormat) {

		try {
			panelFormatDAO.insertPanelFormat(panelFormat);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertStation(Station station) {

		try {
			stationDAO.insertStation(station);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void insertWartung(Wartung wartung) {

		try {
			wartungDAO.insertWartung(wartung);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

	}

	public void insertMESAnlage(MESAnlage mesAnlage) {

		try {
			mesAnlageDAO.insertMESAnlage(mesAnlage);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

	}

	public void insertAnlageUser(AnlageUser anlageUser) {

		try {
			anlageUserDAO.insertAnlageUser(anlageUser);
			errorStatus = false;
		} catch (DAOException e) {
			errorStatus = true;
			e.printStackTrace();

		}

	}

	public void insertUser(User user) {

		try {
			userDAO.insertUser(user);

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			errorStatus = true;

		}

	}

	public void insertLeerflaeche(Leerflaeche leerflaeche) {

		try {
			leerflaecheDAO.insertLeerflaeche(leerflaeche);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			errorStatus = true;

		}

	}

	public boolean isErrorStatus() {
		return errorStatus;
	}

	public Abteilung reloadAbteilung(int abteilungId) {

		try {
			abteilung = abteilungDAO.getAbteilung(abteilungId);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);

		}

		return abteilung;
	}

	public List<Abteilung> reloadAllAbteilung() {

		try {
			abteilungList = abteilungDAO.getAllAbteilung();
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);

		}

		return abteilungList;
	}

	public List<AnlageUser> selectAnlagenUser(int anlageId) {

		try {

			anlageUserList = anlageUserDAO.selectAnlagenUser(anlageId);

			errorStatus = false;

		} catch (DAOException e) {
			e.printStackTrace();
			errorStatus = true;

		}

		return anlageUserList;

	}

	public AnlageUser selectAnlageUser(int anlageId, int userId) {

		try {

			anlageUser = anlageUserDAO.selectAnlageUser(anlageId, userId);

			errorStatus = false;

		} catch (DAOException e) {
			e.printStackTrace();
			errorStatus = true;

		}

		return anlageUser;

	}

	public Leerflaeche getLeerflaeche(int leerflaecheId) {

		try {
			leerflaeche = leerflaecheDAO.getLeerflaeche(leerflaecheId);
			leerflaeche.setPanelFormat(panelFormatDAO.getPanelFormat(leerflaeche.getPanelFormatId()));
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return leerflaeche;

	}

	public Leerflaeche getLeerflaeche(Leerflaeche leerflaeche) {

		try {
			leerflaeche = leerflaecheDAO.getLeerflaeche(leerflaeche);
			leerflaeche.setPanelFormat(panelFormatDAO.getPanelFormat(leerflaeche.getPanelFormatId()));
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return leerflaeche;

	}

	public Station getStation(int stationtId) {

		try {
			station = stationDAO.getStation(stationtId);
			station.setAnlage(anlageDAO.getAnlage(station.getAnlageId()));
			station.setPanelFormat(panelFormatDAO.getPanelFormat(station.getPanelFormatId()));
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);

		}

		return station;

	}

	public void updateAbteilung(Abteilung abteilung) {

		try {
			abteilungDAO.updateAbteilung(abteilung);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateCalendarWartung(CalendarWartung data) {

		try {
			calendarWartungDAO.updateCalendarWartung(data);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateAnlage(Anlage anlage) {

		try {
			anlageDAO.updateAnlage(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateAnlageStueckzahl(Anlage anlage) {

		try {
			anlageDAO.updateAnlageStueckzahl(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateMESAnlage(MESAnlage mesAnlage) {

		try {
			mesAnlageDAO.updateMESAnlage(mesAnlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateAnlageStatus(Anlage anlage) {

		try {
			anlageDAO.updateAnlagenStatus(anlage);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateLeerflaeche(Leerflaeche leerflaeche) {

		try {
			leerflaecheDAO.updateLeerflaeche(leerflaeche);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updatePanelFormat(PanelFormat panelFormat) {

		try {
			panelFormatDAO.updatePanelFormat(panelFormat);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateStation(Station station) {
		try {
			stationDAO.updateStation(station);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

	}

	public void updateUser(User user) {

		try {
			userDAO.updateUser(user);
			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			errorStatus = true;
		}

	}

	public void updateWartung(Wartung wartung) {

		try {
			wartungDAO.updateWartung(wartung);
			errorStatus = false;
		} catch (DAOException e) {
			showExceptionAlertDialog(e);
			e.printStackTrace();
		}

	}

	// TODO
//	public List<Anlage> updateAnlageStueckzahlen() {
//
//		int stueck = 0;
//
//		try {
//			anlageList = anlageDAO.getAllAnlagen();
//
//			for (Anlage anlage : anlageList) {
//				stueck = 0;
//
//				logger.info("Anlage: " + anlage.getName());
//				anlage.setMesAnlagen(mesAnlageDAO.getMESAnlagen(anlage));
//
//				for (MESAnlage mes : anlage.getMesAnlagen()) {
//					stueck = mes.getProdStueck() + stueck;
//					anlage.setAktuelleStueck(stueck);
//				}
//				
//				if (anlage.getName().equalsIgnoreCase("SEC-VA"))
//					logger.info("stop");
//				
//				if (!anlage.getMesAnlagen().isEmpty())				
//					Service.getInstance().updateAnlageStueckzahl(anlage);
//
//				// logger.info("Importierte Anlagen: " + anlage.getMesAnlagen());
//				logger.info("Stueck: " + stueck);
//
//			}
//
//			errorStatus = false;
//		} catch (DAOException e) {
//			e.printStackTrace();
//			showExceptionAlertDialog(e);
//		}
//
//		return anlageList;
//
//	}

	public List<Anlage> updateAnlageStueckzahlenAdd() {

		int stueck = 0;

		try {
			anlageList = anlageDAO.getAllAnlagen();

			for (Anlage anlage : anlageList) {
				stueck = 0;

				logger.info("Anlage: " + anlage.getName());
				anlage.setMesAnlagen(mesAnlageDAO.getMESAnlagen(anlage));

				for (MESAnlage mes : anlage.getMesAnlagen()) {
					stueck = mes.getProdStueck() + stueck;					
				}

				if (anlage.getName().equalsIgnoreCase("SEC-VA")) {			
					logger.info("aktuelle Stück: " + anlage.getAktuelleStueck() + "; SAP Stück: " + stueck);
				}
							
				anlage.setAktuelleStueck(anlage.getAktuelleStueck() + stueck);
				
				if (!anlage.getMesAnlagen().isEmpty())
					Service.getInstance().updateAnlageStueckzahl(anlage);

				// logger.info("Importierte Anlagen: " + anlage.getMesAnlagen());
				//logger.info("Stueck: " + stueck);

			}

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anlageList;

	}
	
	
	public List<Anlage> updateAnlageStueckzahlenSubtract() {

		int stueck = 0;

		try {
			anlageList = anlageDAO.getAllAnlagen();

			for (Anlage anlage : anlageList) {
				stueck = 0;

				logger.info("Anlage: " + anlage.getName());
				anlage.setMesAnlagen(mesAnlageDAO.getMESAnlagen(anlage));

				for (MESAnlage mes : anlage.getMesAnlagen()) {
					stueck = mes.getProdStueck() + stueck;					
				}

				if (anlage.getName().equalsIgnoreCase("SEC-VA")) {			
					logger.info("aktuelle Stück: " + anlage.getAktuelleStueck() + "; SAP Stück: " + stueck);
				}
							
				anlage.setAktuelleStueck(anlage.getAktuelleStueck() - stueck);
				
				if (!anlage.getMesAnlagen().isEmpty())
					Service.getInstance().updateAnlageStueckzahl(anlage);

				// logger.info("Importierte Anlagen: " + anlage.getMesAnlagen());
				//logger.info("Stueck: " + stueck);

			}

			errorStatus = false;
		} catch (DAOException e) {
			e.printStackTrace();
			showExceptionAlertDialog(e);
		}

		return anlageList;

	}

	private void doAnlageAbteilungPanelFormatMapping() {

		for (Anlage anlage : anlageList) {
			for (PanelFormat panelFormat : panelFormatList) {
				if (panelFormat.getId() == anlage.getPanelFormatId()) {
					anlage.setPanelFormat(panelFormat);
				}
			}

			for (Abteilung abteilung : abteilungList) {
				if (abteilung.getId() == anlage.getAbteilungId()) {
					anlage.setAbteilung(abteilung);
					anlage.setAbteilungId(abteilung.getId());
				}
			}

		}

		for (Leerflaeche leerflaeche : leerflaecheList) {
			for (PanelFormat panelFormat : panelFormatList) {
				if (panelFormat.getId() == leerflaeche.getPanelFormatId()) {
					leerflaeche.setPanelFormat(panelFormat);
				}
			}

		}

	}

	private void doStationPanelFormatMapping(Anlage anlage) {

		for (Station station : stationList) {
			for (PanelFormat panelFormat : panelFormatList) {
				if (panelFormat.getId() == station.getPanelFormatId()) {
					station.setPanelFormat(panelFormat);
					station.setAnlage(anlage);
				}
			}

		}

	}

	public boolean stationMaintenanceRequested(Anlage anlage) {

		for (Station station : getStationenFromAnlage(anlage)) {

			if (station.isStatus() == true)
				if (calcProzent(station) >= station.getWartungStueckWarnung()) {

					return true;

				}

		}

		return false;

	}

	private void doStationAnlageMapping(Anlage anlage) {

		for (Station station : stationList) {

			station.setAnlage(anlage);

		}

	}

	private void showExceptionAlertDialog(Exception e) {

		errorStatus = true;

		Alert alert = new Alert(AlertType.ERROR);
		alert.setTitle("Exception Dialog");
		alert.setContentText(e.getMessage() + "\nException: " + e.getClass().getName());
		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());

		// Create expandable Exception.
		StringWriter sw = new StringWriter();
		PrintWriter pw = new PrintWriter(sw);
		e.printStackTrace(pw);
		String exceptionText = sw.toString();

		Label label = new Label("The exception stacktrace was: ");

		TextArea textArea = new TextArea(exceptionText);
		textArea.setEditable(false);
		textArea.setWrapText(true);

		textArea.setMaxWidth(Double.MAX_VALUE);
		textArea.setMaxHeight(Double.MAX_VALUE);
		GridPane.setVgrow(textArea, Priority.ALWAYS);
		GridPane.setHgrow(textArea, Priority.ALWAYS);

		GridPane expContent = new GridPane();
		expContent.setMaxWidth(Double.MAX_VALUE);
		expContent.add(label, 0, 0);
		expContent.add(textArea, 0, 1);

		// Set expandable Exception into the dialog pane.
		alert.getDialogPane().setExpandableContent(expContent);

		Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
		stage.getIcons().add(new Image(Constants.APP_ICON));

		alert.showAndWait();

	}

	public AnlageService getAnlageService() {

		return anlageService;
	}

	public UserService getUserService() {
		return userService;
	}

	public StationService getStationService() {
		return stationService;
	}

}
