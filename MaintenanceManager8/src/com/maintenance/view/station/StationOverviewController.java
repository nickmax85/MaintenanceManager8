package com.maintenance.view.station;

import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.service.Service;
import com.maintenance.util.ApplicationProperties;
import com.maintenance.util.Constants;
import com.maintenance.util.DragResizeMod;
import com.maintenance.view.root.LoginDialog;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Tab;
import javafx.scene.control.TabPane;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StationOverviewController {

	private static final Logger logger = Logger.getLogger(StationOverviewController.class);

	public static boolean dragResize;

	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private BorderPane pane;
	@FXML
	private TabPane tabPane;
	@FXML
	private BorderPane maintenancePane;
	@FXML
	private BorderPane tpmPane;
	@FXML
	private BorderPane robotPane;
	@FXML
	private CheckMenuItem dragResizeCheckMenuItem;
	
	@FXML
	private Tab tpmTab;
	@FXML
	private Tab robotTab;

	private Main main;

	private Stage dialogStage;

	private Anlage anlage;

	private List<StationPanelController> stationPanelControllerList;

	@FXML
	public void initialize() {
		
		dragResizeCheckMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (LoginDialog.isLoggedIn(main.getPrimaryStage())) {

					if (dragResizeCheckMenuItem.isSelected())
						dragResize = true;
					else
						dragResize = false;

				}

			}
		});

	}

	public void setMain(Main main) {

		this.main = main;

	}

	public void setData(Anlage anlage) {

		this.anlage = anlage;

		// ScrollPane sp = getAnlageOverviewPane(anlage);
		// pane.setCenter(sp);

		// maintenancePane.getChildren().add(getAnlageOverviewPane(anlage));
		// tpmPane.getChildren().add(getAnlageOverviewTPMPane(anlage));
		
		
		if (ApplicationProperties.getInstance().getProperty("db_model").equalsIgnoreCase("InspectionManager")) {
			tabPane.getTabs().remove(tpmTab);
			tabPane.getTabs().remove(robotTab);		
		}

		ScrollPane sp = new ScrollPane();
		sp.setContent(getAnlageOverviewPane(anlage));
		maintenancePane.setCenter(sp);

		ScrollPane sp1 = new ScrollPane();
		sp1.setContent(getAnlageOverviewTPMPane(anlage));
		tpmPane.setCenter(sp1);

		ScrollPane sp2 = new ScrollPane();
		sp2.setContent(getAnlageOverviewRobotPane(anlage));
		robotPane.setCenter(sp2);

		// maintenancePane.setCenter(getAnlageOverviewPane(anlage));
		// tpmPane.setCenter(getAnlageOverviewTPMPane(anlage));

		

	}

	@FXML
	private void handleStationenOverviewDialog() {

		if (!LoginDialog.isLoggedIn(main.getPrimaryStage()))
			return;

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/station/StationenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Übersicht: Stationen");
			dialogStage.initOwner(this.dialogStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			StationenOverviewController controller = loader.getController();

			controller.setDialogStage(dialogStage);
			controller.setData(anlage);

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	private AnchorPane getAnlageOverviewPane(Anlage anlage) {

		// ScrollPane sp = new ScrollPane();
		AnchorPane overviewPane = new AnchorPane();

		stationPanelControllerList = new ArrayList<>();

		try {
			for (Station station : Service.getInstance().getAnlageStationenPanelFormat(anlage)) {

				if (!station.isTpm() & !station.isRobot())
					if (station.isStatus()) {

						FXMLLoader loader = new FXMLLoader();
						loader.setResources(resources);
						loader.setLocation(Main.class.getResource("view/station/StationPanel.fxml"));

						AnchorPane pane = (AnchorPane) loader.load();
						pane.setUserData(station);
						overviewPane.getChildren().add(pane);

						StationPanelController controller = loader.getController();
						controller.setMain(main);
						controller.setData(station);
						controller.setDialogStage(dialogStage);

						stationPanelControllerList.add(controller);

						DragResizeMod.makeResizable(pane);

					}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// sp.setContent(overviewPane);
		return overviewPane;
	}

	private AnchorPane getAnlageOverviewTPMPane(Anlage anlage) {

		// ScrollPane sp = new ScrollPane();
		AnchorPane overviewPane = new AnchorPane();

		stationPanelControllerList = new ArrayList<>();

		try {
			for (Station station : Service.getInstance().getAnlageStationenPanelFormat(anlage)) {

				if (station.isTpm())
					if (station.isStatus()) {

						FXMLLoader loader = new FXMLLoader();
						loader.setResources(resources);
						loader.setLocation(Main.class.getResource("view/station/StationPanel.fxml"));

						AnchorPane pane = (AnchorPane) loader.load();
						pane.setUserData(station);
						overviewPane.getChildren().add(pane);

						StationPanelController controller = loader.getController();
						controller.setMain(main);
						controller.setData(station);
						controller.setDialogStage(dialogStage);

						stationPanelControllerList.add(controller);

						DragResizeMod.makeResizable(pane);

					}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// sp.setContent(overviewPane);
		return overviewPane;
	}

	private AnchorPane getAnlageOverviewRobotPane(Anlage anlage) {

		// ScrollPane sp = new ScrollPane();
		AnchorPane overviewPane = new AnchorPane();

		stationPanelControllerList = new ArrayList<>();

		try {
			for (Station station : Service.getInstance().getAnlageStationenPanelFormat(anlage)) {

				if (station.isRobot())
					if (station.isStatus()) {

						FXMLLoader loader = new FXMLLoader();
						loader.setResources(resources);
						loader.setLocation(Main.class.getResource("view/station/StationPanel.fxml"));

						AnchorPane pane = (AnchorPane) loader.load();
						pane.setUserData(station);
						overviewPane.getChildren().add(pane);

						StationPanelController controller = loader.getController();
						controller.setMain(main);
						controller.setData(station);
						controller.setDialogStage(dialogStage);

						stationPanelControllerList.add(controller);

						DragResizeMod.makeResizable(pane);

					}
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		// sp.setContent(overviewPane);
		return overviewPane;
	}

	@FXML
	private void handleUpdate(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			logger.info(event);
			setData(anlage);

		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
