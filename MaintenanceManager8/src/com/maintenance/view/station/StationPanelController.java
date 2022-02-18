package com.maintenance.view.station;

import java.io.IOException;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.ZoneOffset;
import java.util.Calendar;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.util.ProzentCalc;
import com.maintenance.view.alert.InfoAlert;
import com.maintenance.view.anhang.AnhangOverviewController;
import com.maintenance.view.anlage.AnlageDataController;
import com.maintenance.view.root.LoginDialog;

import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
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

public class StationPanelController {

	private static final Logger logger = Logger.getLogger(StationPanelController.class);

	private Stage dialogStage;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label nameLabel;
	@FXML
	private Label equipmentLabel;
	@FXML
	private Label auftragLabel;
	@FXML
	private Label aktuelleStueckLabel;
	@FXML
	private ImageView anhangImage;
	@FXML
	private ImageView linkImage;

	@FXML
	private ProgressBar prozentProgressBar;
	private float prozent;

	@FXML
	private AnchorPane rootPane;

	private ContextMenu contextMenu = new ContextMenu();

	private MenuItem newWartung = new MenuItem();
	private MenuItem wartungen = new MenuItem();
	private MenuItem anhaenge = new MenuItem();
	private MenuItem info = new MenuItem();
	private MenuItem settings = new MenuItem();
	private MenuItem link = new MenuItem();
	private MenuItem duplicate = new MenuItem();

	private Main main;
	private Station station;

	@FXML

	public void initialize() {

		initContextMenu();

	}

	@FXML
	private void handleMenu(MouseEvent e) {

		if (e.isSecondaryButtonDown()) {

			if (logger.isInfoEnabled()) {
				logger.info(station.getName());
			}

		}

	}

	private void initContextMenu() {

		EventHandler<MouseEvent> ev = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getButton() == MouseButton.SECONDARY)
					contextMenu.show(rootPane, event.getScreenX(), event.getScreenY());

				if (event.getButton().equals(MouseButton.PRIMARY)) {
					if (event.getClickCount() == 2) {

					}
				}

			}
		};
		rootPane.addEventHandler(MouseEvent.MOUSE_CLICKED, ev);

		ImageView wartungenImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/table48.png")));
		wartungenImage.setFitWidth(24);
		wartungenImage.setFitHeight(24);
		wartungen.setGraphic(wartungenImage);
		wartungen.setText("Wartungen");
		wartungen.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				main.showWartungenOverviewDialog(null, station);

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

				main.showWartungEditDialog(null, station, wartung);

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
				if (station.getWartungsplanLink().isEmpty()) {
					new InfoAlert(null).showAndWait();
				}

				else {
					try {

						System.out.println(station.getWartungsplanLink());

						if (station.getWartungsplanLink().contains("http"))
							try {

								String link = station.getWartungsplanLink().replace("{", "%7B");
								link = link.replace("}", "%7D");
								AnlageDataController.openWebpage(new URL(link).toURI());
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
							Runtime.getRuntime().exec("explorer " + station.getWartungsplanLink());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
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

				String message = "Id: " + station.getId() + "\nName: " + station.getName() + "\nAuftragsnummer: "
						+ station.getAuftrag() + "\n\nModifiedBy: " + station.getUser() + "\nTimestamp: "
						+ station.getTimestampSql();

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

		ImageView duplicateImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/duplicate48.png")));
		duplicateImage.setFitWidth(24);
		duplicateImage.setFitHeight(24);
		duplicate.setGraphic(duplicateImage);
		duplicate.setText("Duplizieren");
		duplicate.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				if (LoginDialog.isLoggedIn(main.getPrimaryStage())) {

					Alert alert = new Alert(AlertType.CONFIRMATION);
					alert.getDialogPane().getStyleableParent();
					alert.setTitle("MaintenanceManager");
					alert.setHeaderText("Duplizieren");
					alert.setContentText("Wollen Sie die Station wirklich duplizieren?");

					Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
					stage.setAlwaysOnTop(true);
					stage.toFront();

					ButtonType buttonTypeOk = new ButtonType("Ja");
					ButtonType buttonTypeCancel = new ButtonType("Nein");
					alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
					Optional<ButtonType> result = alert.showAndWait();

					if (result.get() == buttonTypeOk) {

						Station s = new Station();
						s.setAnlageId(station.getAnlageId());
						s.setAnlage(station.getAnlage());
						s.setAuftrag(station.getAuftrag());
						s.setWartungArt(station.getWartungArt());
						s.setEquipment(station.getEquipment());
						s.setIntervallDateUnit(station.getIntervallDateUnit());
						s.setWartungDateIntervall(station.getWartungDateIntervall());	
						s.setWarnungDateUnit(station.getWarnungDateUnit());					
						s.setWartungDateWarnung(station.getWartungDateWarnung());
						s.setWartungStueckWarnung(station.getWartungStueckWarnung());
						s.setWartungStueckFehler(station.getWartungStueckFehler());

						s.setStatus(station.isStatus());
						s.setTpm(station.isTpm());
						s.setRobot(station.isRobot());
						s.setAuswertung(station.isAuswertung());
						s.setName("Duplikat von: " + station.getName());
						s.setCreateDate(Calendar.getInstance().getTime());

						PanelFormat panelFormat = new PanelFormat();
						panelFormat.setX(station.getPanelFormat().getX() + 20);
						panelFormat.setY(station.getPanelFormat().getY() + 20);
						panelFormat.setWidth(station.getPanelFormat().getWidth());
						panelFormat.setHeigth(station.getPanelFormat().getHeigth());

						Service.getInstance().insertPanelFormat(panelFormat);

						if (!Service.getInstance().isErrorStatus()) {
							s.setPanelFormat(panelFormat);
							s.setPanelFormatId(panelFormat.getId());
							Service.getInstance().insertStation(s);
						}
					}
				}
			}
		});

		rootPane.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {
				if (event.isSecondaryButtonDown()) {
					contextMenu.show(rootPane, event.getScreenX(), event.getScreenY());
				}
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
				if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
					main.showStationEditDialog(station, station.getAnlage());
			}
		});

		contextMenu.getItems().addAll(newWartung, wartungen, new SeparatorMenuItem(), link, anhaenge,
				new SeparatorMenuItem(), settings, duplicate, info);

	}

	public void setData(Station station) {

		this.station = station;

		nameLabel.setText(station.getName());
		equipmentLabel.setText(station.getEquipment());
		auftragLabel.setText(station.getAuftrag());

		stylePane();

		if (Service.getInstance().getAnhangAnzahlFromStation(station))
			anhangImage.setVisible(true);
		else
			anhangImage.setVisible(false);

		if (this.station.getWartungsplanLink() != null)
			if (this.station.getWartungsplanLink().isEmpty()) {
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

		// if (station.getAnlage().getName().contains("CTC50 Wellenzelle"))
		// logger.info(station.getName() + ": " + prozent);

		Tooltip tooltip = new Tooltip(prozent + "%");
		tooltip.setFont(new Font("Arial", 16));
		prozentProgressBar.setTooltip(tooltip);

		rootPane.setLayoutX(station.getPanelFormat().getX());
		rootPane.setLayoutY(station.getPanelFormat().getY());
		rootPane.setPrefSize(station.getPanelFormat().getWidth(), station.getPanelFormat().getHeigth());

	}

	public void setMain(Main main) {
		this.main = main;

	}

	private void stylePane() {

		if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

			aktuelleStueckLabel.setText("#" + String.valueOf(station.getAnlage().getAktuelleStueck()));
			prozent = ProzentCalc.calcProzent(station);
			prozentProgressBar.setProgress(prozent / 100.0f);

			if (prozent >= 0 && prozent < station.getWartungStueckWarnung())
				rootPane.setId("pane-green");

			else if (prozent >= station.getWartungStueckWarnung() && prozent < station.getWartungStueckFehler())
				rootPane.setId("pane-yellow");

			else if (prozent >= station.getWartungStueckFehler())
				rootPane.setId("pane-orange");
		}

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

				prozentProgressBar.setProgress(prozent / 100.0f);

				SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
				aktuelleStueckLabel.setText(df.format(nextWartungDate));

				if (Calendar.getInstance().getTime().before(nextWarnungDate))
					rootPane.setId("pane-green");

				if (Calendar.getInstance().getTime().after(nextWarnungDate)
						&& Calendar.getInstance().getTime().before(nextWartungDate))
					rootPane.setId("pane-yellow");

				if (Calendar.getInstance().getTime().after(nextWartungDate))
					rootPane.setId("pane-orange");

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
			controller.setData(station);

			dialogStage.showAndWait();

			logger.info("Methode: handleAnhaenge() Ende");

			return controller.isOkClicked();

		} catch (IOException e) {

			e.printStackTrace();

			logger.error(e.getMessage());

			return false;
		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
