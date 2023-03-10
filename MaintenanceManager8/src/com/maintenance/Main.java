package com.maintenance;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.service.Service;
import com.maintenance.db.util.HibernateUtil;
import com.maintenance.util.ApplicationProperties;
import com.maintenance.util.Constants;
import com.maintenance.util.DragResizeMod;
import com.maintenance.view.abteilung.AbteilungenOverviewController;
import com.maintenance.view.anhang.AnhangOverviewController;
import com.maintenance.view.anlage.AnlageEditController;
import com.maintenance.view.anlage.AnlagePanelController;
import com.maintenance.view.anlage.AnlagenOverviewController;
import com.maintenance.view.anlageuser.AnlageUserOverviewController;
import com.maintenance.view.chart.NextWartungenBarChartController;
import com.maintenance.view.leerflaeche.LeerflaecheEditController;
import com.maintenance.view.leerflaeche.LeerflaechePanelController;
import com.maintenance.view.leerflaeche.LeerflaechenOverviewController;
import com.maintenance.view.mesanlage.MESAnlagenOverviewController;
import com.maintenance.view.report.WartungReportController;
import com.maintenance.view.root.LayoutController;
import com.maintenance.view.root.LoginDialog;
import com.maintenance.view.root.SettingsController;
import com.maintenance.view.station.StationEditController;
import com.maintenance.view.station.StationOverviewController;
import com.maintenance.view.tree.TreeViewController;
import com.maintenance.view.user.UserOverviewController;
import com.maintenance.view.wartung.WartungEditController;
import com.maintenance.view.wartung.WartungenOverviewController;

import javafx.animation.FadeTransition;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.concurrent.Task;
import javafx.concurrent.Worker;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.geometry.Rectangle2D;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.paint.Color;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.stage.WindowEvent;
import javafx.util.Duration;

public class Main extends Application {

	public final static String VERSION_HAUPT = "1";
	public final static String VERSION_NEBEN = "0";
	public final static String REVISION = "32";

	// Java Entwicklungsversion
	public final static String JDK = "1.8.0_152";

	private static final Logger logger = Logger.getLogger(Main.class);
	private ResourceBundle resources = ResourceBundle.getBundle("language");

	// Splash
	private Pane splashLayout;
	private ProgressBar loadProgress;
	private Label progressText;
	private Label appInfo;
	private Label developerInfo;
	public static final String SPLASH_IMAGE = Constants.SPLASHSCREEN_IMAGE_PROCESSMANAGER;
	private static int threadSplashSleepTime = Constants.THREAD_SPLASH_SLEEP_TIME;
	private static double fadeTransitionsTime = Constants.FADE_TRANSITIONS_TIME;
	private static boolean showSplashScreen = Constants.SHOW_SPLASH_SCREEN;

	public final static String APP_ICON = Constants.APP_ICON;
	// public final static String APP_ICON = Constants.APP_ICON2;

	private static String ip;

	private Stage primaryStage;

	private BorderPane rootLayout;

	private List<AnlagePanelController> anlagePanelControllerList;
	private List<LeerflaechePanelController> leerflaechePanelControllerList;

	private Thread uiUpdateThread;
	private Thread loadDatabaseThread;

	public static void main(final String[] args) {

		ip = null;
		if (args.length == 1) {
			ip = args[0];

			threadSplashSleepTime = 0;
			fadeTransitionsTime = 0;
			showSplashScreen = false;

		}

		launch(args);

	}

	/*
	 * @Override public void start(Stage primaryStage) throws Exception {
	 * 
	 * this.primaryStage = primaryStage;
	 * 
	 * String userHome = System.getProperty("user.home");
	 * 
	 * PropertyConfigurator.configure(getClass().getClassLoader().getResource(
	 * "log4j.properties"));
	 * ApplicationProperties.configure("application.properties", userHome +
	 * File.separator + resources.getString("appname"), "application.properties");
	 * ApplicationProperties.getInstance().setup();
	 * 
	 * if (ip != null) { ApplicationProperties.getInstance().edit("db_host", ip);
	 * LoginDialog.loggedIn = true;
	 * 
	 * }
	 * 
	 * HibernateUtil.getSessionFactory();
	 * 
	 * this.primaryStage.setTitle(resources.getString("appname") + " " +
	 * BUILD.replace("$", " ") + "@" +
	 * ApplicationProperties.getInstance().getProperty("db_host"));
	 * this.primaryStage.setMaximized(true); this.primaryStage.getIcons() .add(new
	 * Image(getClass().getClassLoader().getResourceAsStream(Constants.APP_ICON)));
	 * this.primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
	 * 
	 * @Override public void handle(WindowEvent event) {
	 * 
	 * Platform.exit(); System.exit(0);
	 * 
	 * } });
	 * 
	 * initRootLayout();
	 * 
	 * this.primaryStage.show();
	 * 
	 * loadDatabaseThread = new Thread(new LoadDatabaseThread());
	 * loadDatabaseThread.start();
	 * 
	 * uiUpdateThread = new Thread(new UIUpdateThread()); uiUpdateThread.start();
	 * 
	 * }
	 */

	@Override
	public void start(Stage initStage) {

		this.primaryStage = new Stage();

		PropertyConfigurator.configure(getClass().getClassLoader().getResource("log4j.properties"));

		final Task<Integer> modulTask = new Task<Integer>() {
			@Override
			protected Integer call() throws InterruptedException {

				int actProgress = 1;
				int maxProgress = 2;

				updateProgress(0, maxProgress);
				updateMessage("Programm wird gestartet. . .");
				Thread.sleep(threadSplashSleepTime * 2);

				if (actProgress == 1) {
					updateProgress(actProgress, maxProgress);
					updateMessage(actProgress + " von " + maxProgress + ": "
							+ "Initialisiere Einstellungen, Datenbank . . .");

					initProperties();

					Thread.sleep(threadSplashSleepTime);
					actProgress++;
				}

				if (actProgress == 2) {
					updateProgress(actProgress, maxProgress);
					updateMessage(
							actProgress + " von " + maxProgress + ": " + "Initialisiere Benutzeroberfläche . . .");

					initGraphics();

					Thread.sleep(threadSplashSleepTime);
					actProgress++;
				}

				return actProgress;
			}
		};

		showSplash(initStage, modulTask, () -> initRootLayout());
		new Thread(modulTask).start();

	}

	private void initProperties() {

		String userHome = System.getProperty("user.home");

		ApplicationProperties.configure("application.properties",
				userHome + File.separator + resources.getString("appname"), "application.properties");
		ApplicationProperties.getInstance().setup();

		ApplicationProperties.getInstance().edit("db_host", "ilzmsih01.prodln01.net");

		if (ip != null) {
			ApplicationProperties.getInstance().edit("db_host", ip);
			LoginDialog.loggedIn = true;
		}

		HibernateUtil.getSessionFactory();

	}

	private void initGraphics() {

		primaryStage.setTitle(resources.getString("appname") + " " + Main.VERSION_HAUPT + "." + Main.VERSION_NEBEN + "."
				+ Main.REVISION + "@" + ApplicationProperties.getInstance().getProperty("db_host") + "/"
				+ ApplicationProperties.getInstance().getProperty("db_model"));
		primaryStage.setMaximized(true);
		primaryStage.getIcons().add(new Image(getClass().getClassLoader().getResourceAsStream(Main.APP_ICON)));
		primaryStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {

				Platform.exit();
				System.exit(0);

			}
		});

	}

	public void updateRootLayout() {

		rootLayout.setCenter(initHalleOverviewPane());

	}

	private void initRootLayout() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/root/Layout.fxml"));
			loader.setResources(resources);

			rootLayout = (BorderPane) loader.load();
			ScrollPane halleOverviewPane = initHalleOverviewPane();
			rootLayout.setCenter(halleOverviewPane);

			LayoutController controller = loader.getController();
			controller.setMain(this);

			Scene scene = new Scene(rootLayout);
			scene.getStylesheets().add(Constants.STYLESHEET);
			primaryStage.setScene(scene);

			loadDatabaseThread = new Thread(new LoadDatabaseThread());
			loadDatabaseThread.start();

			uiUpdateThread = new Thread(new UIUpdateThread());
			uiUpdateThread.start();

			primaryStage.show();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private ScrollPane initHalleOverviewPane() {

		ScrollPane sp = new ScrollPane();
		AnchorPane overviewPane = new AnchorPane();

		anlagePanelControllerList = new ArrayList<>();
		leerflaechePanelControllerList = new ArrayList<>();

		try {

			for (Anlage anlage : Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat()) {

				if (anlage.isStatus()) {
					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Main.class.getResource("view/anlage/AnlagePanel.fxml"));
					loader.setResources(resources);

					AnchorPane pane = (AnchorPane) loader.load();
					pane.setUserData(anlage);

					overviewPane.getChildren().add(pane);

					AnlagePanelController controller = loader.getController();
					controller.setMain(this);
					controller.setDialogStage(primaryStage);
					controller.initData(anlage);

					anlagePanelControllerList.add(controller);

					DragResizeMod.makeResizable(pane);

				}
			}

			for (Leerflaeche leerflaeche : Service.getInstance().getAllLeerflaechePanelFormat()) {

				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("view/leerflaeche/LeerflaechePanel.fxml"));
				loader.setResources(resources);

				AnchorPane pane = (AnchorPane) loader.load();
				pane.setUserData(leerflaeche);

				overviewPane.getChildren().add(pane);

				LeerflaechePanelController controller = loader.getController();
				controller.setMain(this);
				controller.setDialogStage(primaryStage);
				controller.initData(leerflaeche);

				leerflaechePanelControllerList.add(controller);

				DragResizeMod.makeResizable(pane);
			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		sp.setContent(overviewPane);
		return sp;

	}

	public void showAbteilungenOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/abteilung/AbteilungenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Abteilungen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AbteilungenOverviewController controller = loader.getController();
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showReportWartungenDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/report/WartungReport.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Report: Wartungen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			WartungReportController controller = loader.getController();
			controller.setDialogStage(primaryStage);

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showMESAnlagenOverview() {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/mesanlage/MESAnlagenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Import Stückzahlen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			MESAnlagenOverviewController controller = loader.getController();
			controller.setData();
			controller.setDialogStage(primaryStage);

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showLeerflaechenOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/leerflaeche/LeerflaechenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Leerflaechen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			LeerflaechenOverviewController controller = loader.getController();
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showTreeViewOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/tree/TreeView.fxml"));
			loader.setResources(resources);
			BorderPane page = (BorderPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht: Wartungen");
			dialogStage.initOwner(primaryStage);
			dialogStage.setMaximized(true);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			TreeViewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setMain(this);

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showAnlagenOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/anlage/AnlagenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht: Anlagen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AnlagenOverviewController controller = loader.getController();
			controller.setDialogStage(primaryStage);
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showAnlageUserOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/anlageuser/AnlageUserOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			// dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht: Anlagen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AnlageUserOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public boolean showAllgemeineAnhaenge() {

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/anhang/AnhangOverview.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();

			dialogStage.centerOnScreen();
			dialogStage.initOwner(primaryStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.setTitle("Anhänge (1 Anhang max. 10MB)");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AnhangOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(null);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void showUserOverviewDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/user/UserOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			// dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht: User");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			UserOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public boolean showAnlageEditDialog(Anlage data) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/anlage/AnlageEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.primaryStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.primaryStage.getIcons());

			dialogStage.setTitle("Anlage: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AnlageEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(Service.getInstance().getAnlage(data.getId()));

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showLeerflaecheEditDialog(Leerflaeche data) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/leerflaeche/LeerflaecheEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.primaryStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.primaryStage.getIcons());

			dialogStage.setTitle("Leerfläche: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			LeerflaecheEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(data);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void showNextWartungenBarChartDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/chart/NextWartungenBarChart.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht: Anlagen");
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			NextWartungenBarChartController controller = loader.getController();
			controller.setDialogStage(primaryStage);
			controller.setData();

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void showWartungenOverviewDialog(Anlage anlage, Station station) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/wartung/WartungenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);

			if (anlage != null)
				dialogStage.setTitle("Übersicht Wartungen: " + anlage.getName());
			if (station != null)
				dialogStage.setTitle("Übersicht Wartungen: " + station.getName());

			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			WartungenOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			if (anlage != null)
				controller.setData(anlage);

			if (station != null)
				controller.setData(station);

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public void showStationOverviewDialog(Anlage anlage) {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/station/StationOverview.fxml"));
			loader.setResources(resources);

			BorderPane pane = (BorderPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.setMaximized(true);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht Stationen: " + anlage.getName());
			dialogStage.initOwner(primaryStage);

			Scene scene = new Scene(pane);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			StationOverviewController controller = loader.getController();
			controller.setMain(this);
			controller.setDialogStage(dialogStage);
			controller.setData(anlage);

			dialogStage.showAndWait();
			StationOverviewController.dragResize = false;

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	public boolean showStationEditDialog(Station station, Anlage anlage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/station/StationEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.primaryStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.primaryStage.getIcons());

			dialogStage.setTitle("Station: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			StationEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(station, anlage);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showWartungEditDialog(Anlage anlage, Station station, Wartung data) {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/wartung/WartungEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(primaryStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.setAlwaysOnTop(true);
			dialogStage.setTitle("Wartung: Erstellen");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);
			dialogStage.setAlwaysOnTop(true);

			WartungEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			if (anlage != null)
				controller.setData(anlage);

			if (station != null)
				controller.setData(station);

			controller.setData(data);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean showSettingsDialog() {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/root/Settings.fxml"));
			loader.setResources(resources);
			AnchorPane pane = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(primaryStage.getIcons());
			dialogStage.setTitle(resources.getString("settings"));
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.initOwner(primaryStage);
			Scene scene = new Scene(pane);
			scene.getStylesheets().add(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			dialogStage.setScene(scene);

			SettingsController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData();

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}

	}

	class UIUpdateThread implements Runnable {

		@Override
		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				// logger.info(getClass().getSimpleName() + "; " +
				// Thread.currentThread().getName() + "; Status: "
				// + Thread.currentThread().getState() + ";");

				Platform.runLater(new Runnable() {
					@Override
					public void run() {

						for (AnlagePanelController controller : anlagePanelControllerList)
							controller.setGraphicComponents();

						for (LeerflaechePanelController controller : leerflaechePanelControllerList)
							controller.setGraphicComponents();

					}
				});

				try {

					Thread.sleep(TimeUnit.SECONDS.toMillis(5));

				} catch (InterruptedException e) {

					e.printStackTrace();
					logger.info(e.getLocalizedMessage());
					Thread.currentThread().interrupt();
				}
			}

		}

	}

	class LoadDatabaseThread implements Runnable {

		@Override
		public void run() {

			while (!Thread.currentThread().isInterrupted()) {

				logger.info(LoadDatabaseThread.class.getSimpleName() + "; " + Thread.currentThread().getName()
						+ "; Status: " + Thread.currentThread().getState());

				for (AnlagePanelController controller : anlagePanelControllerList) {
					controller.getDataFromDatabase();
				}

				for (LeerflaechePanelController controller : leerflaechePanelControllerList) {
					controller.getDataFromDatabase();
				}

				try {

					Thread.sleep(TimeUnit.MINUTES.toMillis(10));

				} catch (InterruptedException e) {

					e.printStackTrace();
					logger.info(e.getLocalizedMessage());
					Thread.currentThread().interrupt();
				}

			}

		}

	}

	public Stage getPrimaryStage() {
		return primaryStage;
	}

	@Override
	public void init() {

		ImageView splash = new ImageView(new Image(SPLASH_IMAGE));

		loadProgress = new ProgressBar();
		loadProgress.setPrefWidth(Constants.SPLASH_WIDTH - 0);

		progressText = new Label("");
		progressText.setAlignment(Pos.CENTER);

		StringBuilder sb = new StringBuilder();
		appInfo = new Label("");

		sb.append(resources.getString("appname"));
		sb.append(" (Version " + VERSION_HAUPT + "." + VERSION_NEBEN + "." + REVISION + ")");

		appInfo.setFont(Font.font("System", FontWeight.BOLD, 15));
		appInfo.setTextFill(Color.DARKGREY);
		appInfo.setText(sb.toString().replace("$", ""));

		developerInfo = new Label("");
		developerInfo.setFont(Font.font("System", FontWeight.BOLD, 20));
		developerInfo.setTextFill(Color.DARKGREY);
		developerInfo.setText("\nEntwicklung: " + resources.getString("programer"));

		splashLayout = new VBox();
		splashLayout.getChildren().addAll(splash, loadProgress, progressText, developerInfo, appInfo);
		splashLayout.setStyle(
				"-fx-padding: 5; " + "-fx-background-color: #DAE6F3; " + "-fx-border-width:5; " + "-fx-border-color: "
						+ "linear-gradient(" + "to bottom, " + "#7ebcea, " + "derive(#7ebcea, 50%)" + ");");
		splashLayout.setEffect(new DropShadow());
	}

	private void showSplash(final Stage initStage, Task<?> task, InitCompletionHandler initCompletionHandler) {
		progressText.textProperty().bind(task.messageProperty());
		loadProgress.progressProperty().bind(task.progressProperty());
		task.stateProperty().addListener((observableValue, oldState, newState) -> {
			if (newState == Worker.State.SUCCEEDED) {
				loadProgress.progressProperty().unbind();
				loadProgress.setProgress(1);
				initStage.toFront();
				FadeTransition fadeSplash = new FadeTransition(Duration.seconds(fadeTransitionsTime), splashLayout);
				fadeSplash.setFromValue(1.0);
				fadeSplash.setToValue(0.0);
				fadeSplash.setOnFinished(actionEvent -> initStage.close());
				fadeSplash.play();

				initCompletionHandler.complete();
			} // todo add code to gracefully handle other task states.
		});

		Scene splashScene = new Scene(splashLayout, Color.TRANSPARENT);
		final Rectangle2D bounds = Screen.getPrimary().getVisualBounds();
		initStage.setScene(splashScene);
		initStage.setX(bounds.getMinX() + bounds.getWidth() / 2 - Constants.SPLASH_WIDTH / 2);
		initStage.setY(bounds.getMinY() + bounds.getHeight() / 2 - Constants.SPLASH_HEIGHT / 2);
		initStage.getIcons().add(new Image(Constants.APP_ICON));
		initStage.setTitle(resources.getString("appname"));
		initStage.initStyle(StageStyle.TRANSPARENT);
		initStage.setResizable(false);

		initStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
			@Override
			public void handle(WindowEvent event) {
				logger.info("Programm beenden");

				Platform.exit();
				System.exit(0);

			}
		});

		initStage.setAlwaysOnTop(true);

		if (showSplashScreen)
			initStage.show();
	}

	public interface InitCompletionHandler {
		void complete();
	}

}
