package com.maintenance.view.anlage;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;
import com.maintenance.util.ApplicationProperties;
import com.maintenance.util.Constants;
import com.maintenance.util.ProzentCalc;
import com.maintenance.view.alert.InfoAlert;
import com.maintenance.view.anhang.AnhangOverviewController;
import com.maintenance.view.root.LoginDialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.Tooltip;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Font;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AnlagePanelController implements Initializable {

	private static final Logger logger = Logger.getLogger(AnlagePanelController.class);

	private Stage dialogStage;
	@FXML
	private ResourceBundle resources;
	@FXML
	private URL location;

	@FXML
	private AnchorPane outerPane;
	@FXML
	private AnchorPane innerPane;

	@FXML
	private Label nameLabel;
	@FXML
	private Label auftragLabel;
	@FXML
	private Label aktuelleStueckLabel;
	@FXML
	private ProgressBar prozentProgressBar;
	private float prozent;
	@FXML
	private ImageView tpmImage;
	@FXML
	private ImageView robotImage;
	@FXML
	private ImageView anhangImage;
	@FXML
	private ImageView linkImage;
	@FXML
	private ImageView tpmExistsImage;

	private ContextMenu contextMenu = new ContextMenu();
	private MenuItem wartungen = new MenuItem();
	private MenuItem newWartung = new MenuItem();
	private MenuItem anhaenge = new MenuItem();
	private MenuItem info = new MenuItem();
	private MenuItem settings = new MenuItem();
	private MenuItem link = new MenuItem();

	private Main main;

	private Anlage anlage;

	private List<Station> stationen;
	private Station stationStueck;
	private Station stationDate;
	private CalendarWartung cal;

	private EPanel visiblePanel;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		initContextMenu();

	}

	private void initContextMenu() {

		tpmImage.setVisible(false);
		robotImage.setVisible(false);
		EventHandler<MouseEvent> ev = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getButton() == MouseButton.SECONDARY)
					contextMenu.show(outerPane, event.getScreenX(), event.getScreenY());

				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (event.getClickCount() == 2) {
						main.showStationOverviewDialog(anlage);
					}
				}

			}
		};
		outerPane.addEventHandler(MouseEvent.MOUSE_CLICKED, ev);

		ImageView wartungenImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/table48.png")));
		wartungenImage.setFitWidth(24);
		wartungenImage.setFitHeight(24);
		wartungen.setGraphic(wartungenImage);
		wartungen.setText("Wartungen");
		wartungen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				main.showWartungenOverviewDialog(anlage, null);

			}
		});

		ImageView anhangImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/anhang48.png")));
		anhangImage.setFitWidth(24);
		anhangImage.setFitHeight(24);
		anhaenge.setGraphic(anhangImage);
		anhaenge.setText("Anhaenge");
		anhaenge.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				logger.info(event);

				handleAnhaenge();

			}
		});

		ImageView newWartungImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/maintenance48.png")));
		newWartungImage.setFitWidth(24);
		newWartungImage.setFitHeight(24);
		newWartung.setGraphic(newWartungImage);
		newWartung.setText("Wartung erstellen");
		newWartung.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				Wartung wartung = new Wartung();

				main.showWartungEditDialog(anlage, null, wartung);

			}
		});

		ImageView infoImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/info48.png")));
		infoImage.setFitWidth(24);
		infoImage.setFitHeight(24);
		info.setGraphic(infoImage);
		info.setText("Info");
		info.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				String message = "Id: " + anlage.getId() + "\nName: " + anlage.getName() + "\nAuftragsnummer: "
						+ anlage.getAuftrag() + "\nAktuelle Stückzahl: " + anlage.getAktuelleStueck()
						+ "\nAbteilungId: " + anlage.getAbteilungId() + "\n\nModifiedBy: " + anlage.getUser()
						+ "\nTimestamp: " + anlage.getTimestampSql();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Info");
				alert.setContentText(message);

				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.initOwner(dialogStage);
				stage.getIcons().add(new Image(Constants.APP_ICON));

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().addAll(Main.class.getResource(Constants.STYLESHEET).toExternalForm());
				alert.showAndWait();

			}
		});

		ImageView settingsImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/config24.png")));
		settingsImage.setFitWidth(24);
		settingsImage.setFitHeight(24);

		settings.setGraphic(settingsImage);
		settings.setText("Konfiguration");
		settings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (LoginDialog.isLoggedIn(dialogStage))
					main.showAnlageEditDialog(anlage);

			}
		});

		ImageView linkImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/link16.png")));
		linkImage.setFitWidth(24);
		linkImage.setFitHeight(24);

		link.setGraphic(linkImage);
		link.setText("Wartungsplan");
		link.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				if (anlage.getWartungsplanLink().isEmpty()) {
					new InfoAlert(null).showAndWait();
				}

				else {
					try {

					

						if (anlage.getWartungsplanLink().contains("http"))
							try {

								String link = anlage.getWartungsplanLink().replace("{", "%7B");
								link = link.replace("}", "%7D");
								AnlageDataController.openWebpage(new URL(link).toURI());
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
							Runtime.getRuntime().exec("explorer " + anlage.getWartungsplanLink());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}
		});

		contextMenu.getItems().addAll(newWartung, wartungen, new SeparatorMenuItem(), anhaenge, link,
				new SeparatorMenuItem(), settings, new SeparatorMenuItem(), info);

	}

	@FXML
	private void handleMenu(MouseEvent e) {

		if (e.isSecondaryButtonDown()) {
			if (logger.isInfoEnabled()) {
				logger.info(anlage.getName());
			}
		}
	}

	@FXML
	private boolean handleAnhaenge() {

		logger.info("Methode: handleAnhaenge() Start");

		try {
			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/anhang/AnhangOverview.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();

			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());
			dialogStage.setTitle("Anhänge (1 Anhang max. 10MB)");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AnhangOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(anlage);

			dialogStage.showAndWait();

			logger.info("Methode: handleAnhaenge() Ende");

			return controller.isOkClicked();

		} catch (IOException e) {

			e.printStackTrace();

			logger.error(e.getMessage());

			return false;
		}

	}

	public void initData(Anlage anlage) {

		this.anlage = anlage;

		getDataFromDatabase();
		setGraphicComponents();

	}

	public void setGraphicComponents() {

		// if (anlage.getName().equalsIgnoreCase("atc 350"))
		// logger.error(anlage.getName() + ": " + anlage.hashCode());

		if (this.anlage.getWartungsplanLink() != null)
			if (this.anlage.getWartungsplanLink().isEmpty()) {
				linkImage.setVisible(false);
				link.setDisable(true);
			} else {
				linkImage.setVisible(true);
				link.setDisable(false);
			}
		else {
			linkImage.setVisible(false);
			link.setDisable(true);

		}

		nameLabel.setText(anlage.getName());
		auftragLabel.setText(anlage.getAuftrag());

		if (!anlage.isSubMenu()) {

			newWartung.setDisable(false);
			wartungen.setDisable(false);

			styleInnerPane();
			styleOuterPane();

			if (ProzentCalc.isTPMStationFehler(stationen))
				tpmImage.setVisible(true);
			else
				tpmImage.setVisible(false);

			if (ProzentCalc.isRobotStationFehler(stationen))
				robotImage.setVisible(true);
			else
				robotImage.setVisible(false);

			tpmExistsImage.setVisible(false);
			for (Station s : stationen) {
				if (s.isTpm() && s.isStatus())
					tpmExistsImage.setVisible(true);

			}

		}

		if (anlage.isSubMenu()) {

			newWartung.setDisable(true);
			wartungen.setDisable(true);

			if (stationen != null) {
				stationStueck = ProzentCalc.getMaxIntervallStationStueck(stationen);
				stationDate = ProzentCalc.getMaxIntervallStationDate(stationen);
			}

			if (visiblePanel == null)
				visiblePanel = EPanel.STUECK;

			if (visiblePanel == EPanel.STUECK && stationStueck == null)
				visiblePanel = EPanel.DATE;

			if (visiblePanel == EPanel.DATE && stationDate == null)
				visiblePanel = EPanel.STUECK;

			if (visiblePanel == EPanel.STUECK && stationStueck != null)
				if (stationStueck.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

					styleInnerPane(stationStueck);

				}

			if (visiblePanel == EPanel.DATE && stationDate != null)
				if (stationDate.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					styleInnerPane(stationDate);

				}

			// if (anlage.getName().equalsIgnoreCase("tlc"))
			// logger.error("Panel: " + visiblePanel);

			if (ProzentCalc.isTPMStationFehler(stationen))
				tpmImage.setVisible(true);
			else
				tpmImage.setVisible(false);

			if (ProzentCalc.isRobotStationFehler(stationen))
				robotImage.setVisible(true);
			else
				robotImage.setVisible(false);

			tpmExistsImage.setVisible(false);
			for (Station s : stationen) {
				if (s.isTpm() && s.isStatus())
					tpmExistsImage.setVisible(true);

			}

			outerPane.setId(innerPane.getId());
		}

		Tooltip tooltip = new Tooltip(prozent + "%");
		tooltip.setFont(new Font("Arial", 16));
		prozentProgressBar.setTooltip(tooltip);

		outerPane.setLayoutX(anlage.getPanelFormat().getX());
		outerPane.setLayoutY(anlage.getPanelFormat().getY());
		outerPane.setPrefSize(anlage.getPanelFormat().getWidth(), anlage.getPanelFormat().getHeigth());

		if (visiblePanel == EPanel.STUECK)
			visiblePanel = EPanel.DATE;
		else
			visiblePanel = EPanel.STUECK;

	}

	private void styleInnerPane() {

		Date nextWarnungDate = null;
		Date nextWartungDate = null;

		// Kalender Wartung
		if (cal != null && anlage.getWartungArt() != EWartungArt.TIME_INTERVALL.ordinal()) {
			nextWartungDate = cal.getDate();
			
			if (anlage.getLastWartungDate() != null)
				prozent = ProzentCalc.calcProzent(anlage.getLastWartungDate().getTime(), nextWartungDate.getTime());
			else
				prozent = ProzentCalc.calcProzent(anlage.getCreateDate().getTime(), nextWartungDate.getTime());

			prozentProgressBar.setProgress(prozent / 100.0f);

			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			aktuelleStueckLabel.setText(df.format(nextWartungDate));

			if (prozent >= 0 && prozent < anlage.getWartungStueckWarnung())
				innerPane.setId("pane-green");

			else if (prozent >= anlage.getWartungStueckWarnung() && prozent < anlage.getWartungStueckFehler())
				innerPane.setId("pane-yellow");

			else if (prozent >= anlage.getWartungStueckFehler())
				innerPane.setId("pane-orange");

		}

		// Stückzahl Wartung
		if (cal == null)
			if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

				aktuelleStueckLabel.setText("#" + String.valueOf(anlage.getAktuelleStueck()));

				prozent = ProzentCalc.calcProzent(anlage);
				prozentProgressBar.setProgress(prozent / 100.0f);

				if (prozent >= 0 && prozent < anlage.getWartungStueckWarnung())
					innerPane.setId("pane-green");

				else if (prozent >= anlage.getWartungStueckWarnung() && prozent < anlage.getWartungStueckFehler())
					innerPane.setId("pane-yellow");

				else if (prozent >= anlage.getWartungStueckFehler())
					innerPane.setId("pane-orange");

			}

		// Zeitintervall Wartung
		if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

			nextWarnungDate = null;
			Date lastWartungDate;

			if (anlage.getLastWartungDate() != null)
				lastWartungDate = anlage.getLastWartungDate();
			else
				lastWartungDate = anlage.getCreateDate();

			nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate, anlage.getIntervallDateUnit(),
					anlage.getWartungDateIntervall());
			nextWarnungDate = ProzentCalc.calcNextWarnungDate(anlage.getWarnungDateUnit(), anlage.getLastWartungDate(),
					nextWartungDate, anlage.getWartungDateWarnung());
			prozent = ProzentCalc.calcProzent(lastWartungDate.getTime(), nextWartungDate.getTime());

			prozentProgressBar.setProgress(prozent / 100.0f);

			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			aktuelleStueckLabel.setText(df.format(nextWartungDate));

			if (Calendar.getInstance().getTime().before(nextWarnungDate))
				innerPane.setId("pane-green");

			if (Calendar.getInstance().getTime().after(nextWarnungDate)
					&& Calendar.getInstance().getTime().before(nextWartungDate))
				innerPane.setId("pane-yellow");

			if (Calendar.getInstance().getTime().after(nextWartungDate))
				innerPane.setId("pane-orange");

		}

		innerPane.getStyleClass().add("panel");

	}

	private void styleInnerPane(Station station) {

		// Stückzahl Wartung
		if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

			aktuelleStueckLabel.setText("#" + String.valueOf(anlage.getAktuelleStueck()));

			prozent = ProzentCalc.calcProzent(station);
			prozentProgressBar.setProgress(prozent / 100.0f);

			if (prozent >= 0 && prozent < station.getWartungStueckWarnung())
				innerPane.setId("pane-green");

			else if (prozent >= station.getWartungStueckWarnung() && prozent < station.getWartungStueckFehler())
				innerPane.setId("pane-yellow");

			else if (prozent >= station.getWartungStueckFehler())
				innerPane.setId("pane-orange");
		}

		// Zeitintervall Wartung
		if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

			Date nextWarnungDate = null;
			Date nextWartungDate;

			Date lastWartungDate;

			if (station.getLastWartungDate() != null)
				lastWartungDate = station.getLastWartungDate();
			else
				lastWartungDate = station.getCreateDate();

			nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate, station.getIntervallDateUnit(),
					station.getWartungDateIntervall());

			prozent = ProzentCalc.calcProzent(lastWartungDate.getTime(), nextWartungDate.getTime());
			prozentProgressBar.setProgress(prozent / 100.0f);

			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			aktuelleStueckLabel.setText(df.format(nextWartungDate));

			nextWarnungDate = ProzentCalc.calcNextWarnungDate(station.getWarnungDateUnit(), lastWartungDate,
					nextWartungDate, station.getWartungDateWarnung());

			if (Calendar.getInstance().getTime().before(nextWarnungDate))
				innerPane.setId("pane-green");

			if (Calendar.getInstance().getTime().after(nextWarnungDate)
					&& Calendar.getInstance().getTime().before(nextWartungDate))
				innerPane.setId("pane-yellow");

			if (Calendar.getInstance().getTime().after(nextWartungDate))
				innerPane.setId("pane-orange");

		}

	}

	private boolean styleOuterPane() {

		boolean isStyled = false;
		outerPane.setId("pane-green");

		if (stationen != null) {
			if (ProzentCalc.isStationWarning(stationen)) {

				outerPane.setId("pane-yellow");
				isStyled = true;

			}
			if (ProzentCalc.isStationFehler(stationen)) {
				outerPane.setId("pane-orange");
				isStyled = true;

			}

		}

		return isStyled;
	}

	public void getDataFromDatabase() {

		if (!Service.getInstance().isErrorStatus()) {
			anlage = Service.getInstance().getAnlage(anlage);
			if (Service.getInstance().getAnhangAnzahlFromAnlage(anlage))
				anhangImage.setVisible(true);
			else
				anhangImage.setVisible(false);

			if (anlage.getLastWartungDate() != null)
				cal = Service.getInstance().getNextCalendarWartung(anlage.getId(), anlage.getLastWartungDate());
			else
				cal = Service.getInstance().getNextCalendarWartung(anlage.getId(), anlage.getCreateDate());

			stationen = Service.getInstance().getStationenFromAnlage(anlage);

		}
	}

	public void setMain(Main main) {
		this.main = main;

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public enum EPanel {

		STUECK, DATE;

	}

}
