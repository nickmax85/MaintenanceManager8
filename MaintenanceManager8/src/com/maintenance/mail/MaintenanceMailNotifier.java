package com.maintenance.mail;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Properties;
import java.util.ResourceBundle;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import com.maintenance.db.dto.Anhang;
import com.maintenance.db.dto.AnlageUser;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;
import com.maintenance.db.util.HibernateUtil;
import com.maintenance.model.Anlage;
import com.maintenance.model.Station;
import com.maintenance.model.User;
import com.maintenance.util.ApplicationProperties;
import com.maintenance.util.Constants;
import com.maintenance.util.ProzentCalc;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.scene.image.Image;
import javafx.scene.layout.StackPane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class MaintenanceMailNotifier extends Application {

	private ResourceBundle resources = ResourceBundle.getBundle("language");

	private long sleep = 3600000 * 1; // Stunden in Millisekungen umrechnen
	private static String ip;

	private Stage primaryStage;

	private List<Station> stationenForMail;
	private List<com.maintenance.db.dto.Station> stationenForMail2;
	private ListView<String> listView;

	private Thread thread;

	@Override
	public void start(Stage primaryStage) {

		this.primaryStage = primaryStage;

		initProperties();

		initRootLayout();

		generateThread();

		generateSystemThread();
	}

	public static void main(String[] args) {

		ip = null;
		if (args.length == 1) {
			ip = args[0];
		}

		launch(args);
	}

	private void initProperties() {

		String userHome = System.getProperty("user.home");

		ApplicationProperties.configure("application.properties",
				userHome + File.separator + resources.getString("appname"), "application.properties");
		ApplicationProperties.getInstance().setup();

		ApplicationProperties.getInstance().edit("db_host", "10.176.45.25");

		if (ip != null) {
			ApplicationProperties.getInstance().edit("db_host", ip);

		}

		HibernateUtil.getSessionFactory();
	}

	private void initRootLayout() {

		primaryStage.setTitle(
				"MaintenanceMailNotifier" + " " + "@" + ApplicationProperties.getInstance().getProperty("db_host") + "/"
						+ ApplicationProperties.getInstance().getProperty("db_model"));

		primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(Constants.APP_ICON)));
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {

				Platform.exit();
				System.exit(0);

			}
		});

		listView = new ListView<>();

		StackPane root = new StackPane();
		root.getChildren().add(listView);
		primaryStage.setScene(new Scene(root, 800, 600));
		primaryStage.show();

	}

	private void generateThread() {

		thread = new Thread(new Runnable() {

			@Override
			public void run() {

				while (!Thread.currentThread().isInterrupted()) {

					try {

						addListElement(
								getCurrentTimeStamp() + " Thread is running: ID=" + Thread.currentThread().getId());

						for (Station station : getStationenForMail()) {
							try {

								if (!station.isMailSent()) {

									addListElement(getCurrentTimeStamp() + " " + station.getAnlage().getName() + ": "
											+ station.getName());

									requestMailDate(station, ProzentCalc.calcProzent(station), "");

									String userText = "";
									for (User user : station.getAnlage().getUsers()) {
										userText += user.getMail() + ", ";

									}

									station.setMailSent(true);
									Service.getInstance().getStationService().update(station);
									addListElement(getCurrentTimeStamp() + " " + userText);

								}

							} catch (Exception e) {

								addListElement(getCurrentTimeStamp() + " Nachrichten konnten nicht versendet werden");
								addListElement(getCurrentTimeStamp() + " Exception Message: " + e.getMessage());
								e.printStackTrace();
							}
						}

						for (com.maintenance.db.dto.Station station : getStationenForMail2()) {
							try {

								if (!station.isMailSent()) {

									addListElement(getCurrentTimeStamp() + " " + station.getAnlage().getName() + ": "
											+ station.getName());

									requestMailStueckzahl(station, ProzentCalc.calcProzent(station), "");

									String userText = "";
									for (User user : station.getAnlage().getUsers()) {
										userText += user.getMail() + ", ";

									}

									station.setMailSent(true);
									Service.getInstance().updateStation(station);
									addListElement(getCurrentTimeStamp() + " " + userText);

								}

							} catch (Exception e) {

								addListElement(getCurrentTimeStamp() + " Nachrichten konnten nicht versendet werden");
								addListElement(getCurrentTimeStamp() + " Exception Message: " + e.getMessage());
								e.printStackTrace();
							}
						}

						Thread.sleep(sleep);

					} catch (InterruptedException e) {

						Thread.currentThread().interrupt();
						e.printStackTrace();
					}

				}

			}
		});

		thread.start();

	}

	private void generateSystemThread() {

		Thread thread = new Thread(new Runnable() {

			@Override
			public void run() {

				while (!Thread.currentThread().isInterrupted()) {

					try {

						addListElement(getCurrentTimeStamp() + " System Mail Thread is running: ID="
								+ Thread.currentThread().getId());

						requestSystemMail("");

						// alle 24 Stunden
						Thread.sleep(86400000);

					} catch (InterruptedException e) {

						Thread.currentThread().interrupt();
						e.printStackTrace();
					} catch (Exception e) {
						addListElement(getCurrentTimeStamp() + " Nachrichten konnten nicht versendet werden");
						addListElement(getCurrentTimeStamp() + " Exception Message: " + e.getMessage());
						e.printStackTrace();
					}

				}

			}
		});

		thread.start();

	}

	private void addListElement(String text) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {

				listView.getItems().add(0, text);

			}
		});

	}

	private void requestMailDate(Station station, float prozent, String remark) throws Exception {

		String smtpHostServer = "smtp-auth.magna.global";
		String from = "svc_LIAT_Maintenance@magna.com";

		String to = "";
		// String to = "markus.thaler@magna.com,markus.thaler@gmx.at";

		List<File> files = new ArrayList<>();

		for (Anhang anhang : Service.getInstance().getAnhangListStation(station)) {

			files.add(anhang.getFile());

		}

		for (Anhang anhang : Service.getInstance().getAnhangList()) {

			files.add(anhang.getFile());

		}

		for (User user : station.getAnlage().getUsers()) {
			to += user.getMail() + ",";

		}

		String betreff = "MaintenanceManager: Autonomes TPM -  Wartungsanforderung für " + station.getAnlage().getName()
				+ "; " + station.getName();

		String text = "";
		text += "Anlage: " + station.getAnlage().getEquipment() + " - " + station.getAnlage().getName() + "\n";
		text += "Komponente: " + station.getName() + "\n";

		Date nextWartungDate;
		if (station.getLastWartungDate() != null)
			nextWartungDate = ProzentCalc.calcNextWartungDate(station.getLastWartungDate(),
					station.getIntervallDateUnit(), station.getWartungDateIntervall());
		else
			nextWartungDate = ProzentCalc.calcNextWartungDate(station.getCreateDate(), station.getIntervallDateUnit(),
					station.getWartungDateIntervall());

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		text += "Wartung ist fällig am : " + df.format(nextWartungDate);
		text += "\n\n";
		// text += "Software für Wartungsrückmeldung: " +
		// "http://10.176.45.4/software/TPMTool.jar";
		text += "Software für Wartungsrückmeldung: " + "http://ilzmsih01.prodln01.net/tpm.html";

		text += "\n\n";
		text += "Diese Nachricht wurde an folgende Adressen versendet: " + to.replaceAll(",", "; ");
		text += "\n\n\n\n\n";

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHostServer);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		//create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, "@VdvCxkoauXdhhz1");
					}
				};

		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		EmailUtil.sendEmail(session, from, to, null, betreff, text, files);

		deleteFiles(files);

	}

	private void deleteFiles(List<File> files) {

		for (File f : files) {

			f.delete();

		}

	}

	private void requestMailStueckzahl(com.maintenance.db.dto.Station station, float prozent, String remark)
			throws Exception {

		String smtpHostServer = "smtp-auth.magna.global";
		String from = "svc_LIAT_Maintenance@magna.com";

		String to = "";
		// String to = "markus.thaler@magna.com,markus.thaler@gmx.at";

		List<File> files = new ArrayList<>();

		for (Anhang anhang : Service.getInstance().getAnhangList(station)) {

			files.add(anhang.getFile());

		}

		for (Anhang anhang : Service.getInstance().getAnhangList()) {

			files.add(anhang.getFile());

		}

		for (User user : station.getAnlage().getUsers()) {
			to += user.getMail() + ",";

		}

		String betreff = "MaintenanceManager: Autonomes TPM - Wartungsanforderung für " + station.getAnlage().getName()
				+ "; " + station.getName();

		String text = "";
		text += "Anlage: " + station.getAnlage().getName() + "\n";
		text += "Komponente: " + station.getName() + "\n";

		text += "Wartung ist fällig in " + ProzentCalc.calcNextWartungStueck(station) + " Stück";

		text += "\n\n";
		text += "Software für Wartungsrückmeldung: " + "http://ilzmsih01.prodln01.net/tpm.html";

		text += "\n\n";
		text += "Diese Nachricht wurde an folgende Adressen versendet: " + to.replaceAll(",", "; ");
		text += "\n\n\n\n\n";

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHostServer);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		//create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, "@VdvCxkoauXdhhz1");
					}
				};

		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		EmailUtil.sendEmail(session, from, to, null, betreff, text, files);

		deleteFiles(files);

	}

	public String getCurrentTimeStamp() {

		return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS").format(new Date());

	}

	private boolean checkStationElapsed(Station station) {

		String remark = null;
		float prozent = 0;
		boolean maintenanceElapsed = false;

		if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

			if (station.getCreateDate() != null || station.getLastWartungDate() != null) {

				Date nextWarnungDate = null;
				Date nextWartungDate;

				if (station.getLastWartungDate() != null) {
					nextWartungDate = ProzentCalc.calcNextWartungDate(station.getLastWartungDate(),
							station.getIntervallDateUnit(), station.getWartungDateIntervall());
					nextWarnungDate = ProzentCalc.calcNextWarnungDate(station.getWarnungDateUnit(),
							station.getLastWartungDate(), nextWartungDate, station.getWartungDateWarnung());
					prozent = ProzentCalc.calcProzent(station.getLastWartungDate().getTime(),
							nextWartungDate.getTime());
				} else {
					nextWartungDate = ProzentCalc.calcNextWartungDate(station.getCreateDate(),
							station.getIntervallDateUnit(), station.getWartungDateIntervall());
					nextWarnungDate = ProzentCalc.calcNextWarnungDate(station.getWarnungDateUnit(),
							station.getCreateDate(), nextWartungDate, station.getWartungDateWarnung());
					prozent = ProzentCalc.calcProzent(station.getCreateDate().getTime(), nextWartungDate.getTime());
				}

				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				remark = df.format(nextWartungDate);

				if (Calendar.getInstance().getTime().after(nextWarnungDate)
						&& Calendar.getInstance().getTime().before(nextWartungDate))
					maintenanceElapsed = true;

				if (Calendar.getInstance().getTime().after(nextWartungDate))
					maintenanceElapsed = true;

			}

		}

		return maintenanceElapsed;

	}

	private boolean checkStationElapsed(com.maintenance.db.dto.Station station) {

		String remark = null;
		float prozent = 0;
		boolean maintenanceElapsed = false;

		if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

			prozent = ProzentCalc.calcProzent(station);

			if (prozent >= station.getWartungStueckWarnung() && prozent < station.getWartungStueckFehler())
				maintenanceElapsed = true;

			else if (prozent >= station.getWartungStueckFehler())
				maintenanceElapsed = true;

		}

		return maintenanceElapsed;

	}

	public List<Station> getStationenForMail() {

		stationenForMail = new ArrayList<>();

		for (Anlage anlage : Service.getInstance().getAnlageService().findAll()) {

			if (anlage.isStatus())
				for (Station station : anlage.getStationen()) {

					if (station.isStatus() && station.isTpm()) {

						if (checkStationElapsed(station))
							stationenForMail.add(station);

					}

				}
		}

		return stationenForMail;
	}

	private List<com.maintenance.db.dto.Station> getStationenForMail2() {

		stationenForMail2 = new ArrayList<>();

		for (com.maintenance.db.dto.Anlage anlage : Service.getInstance()
				.getAllAnlageLeerflaecheAbteilungPanelFormat()) {

			if (anlage.isStatus())
				for (com.maintenance.db.dto.Station station : Service.getInstance().getStationenFromAnlage(anlage)) {

					station.getAnlage().setAktuelleStueck(anlage.getAktuelleStueck());

					if (station.isStatus() && station.isTpm()) {

						if (checkStationElapsed(station)) {

							stationenForMail2.add(station);

							List<User> users = Service.getInstance().getAnlagenUser(anlage.getId());
							station.getAnlage().setUsers(users);
						}
					}
				}

		}
		return stationenForMail2;
	}

	private void requestSystemMail(String remark) throws Exception {

		String smtpHostServer = "smtp-auth.magna.global";
		String from = "svc_LIAT_Maintenance@magna.com";
		String text = "";
		// String to = "";
		String to = "werner.janisch@magna.com,markus.thaler@magna.com";

		SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");

		text += "MaintenanceMailNotifier is running ...";
		// text += "Software für Wartungsrückmeldung: " +
		// "http://10.176.45.4/software/TPMTool.jar";

		text += "\n\n";
		text += "Diese Nachricht wurde an folgende Adressen versendet: " + to.replaceAll(",", "; ");
		text += "\n\n\n\n\n";

		String betreff = "MaintenanceManager";

		Properties props = System.getProperties();
		props.put("mail.smtp.host", smtpHostServer);
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.port", "587");

		//create Authenticator object to pass in Session.getInstance argument
				Authenticator auth = new Authenticator() {
					//override the getPasswordAuthentication method
					protected PasswordAuthentication getPasswordAuthentication() {
						return new PasswordAuthentication(from, "@VdvCxkoauXdhhz1");
					}
				};

		Session session = Session.getInstance(props, auth);
		session.setDebug(true);

		EmailUtil.sendEmail(session, from, to, null, betreff, text, null);

	}

}
