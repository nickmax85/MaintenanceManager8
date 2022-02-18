package com.maintenance.view.station;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.NoSelectionAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn.CellEditEvent;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StationenOverviewController {

	@FXML
	private ResourceBundle resources;
	@FXML
	private TableView<Station> table;
	@FXML
	private TableColumn<Station, String> equipmentColumn;
	@FXML
	private TableColumn<Station, String> nameColumn;
	@FXML
	private TableColumn<Station, String> auftragColumn;
	@FXML
	private TableColumn<Station, Boolean> statusColumn;
	@FXML
	private TableColumn<Station, Boolean> tpmColumn;
	@FXML
	private TableColumn<Station, Boolean> robotColumn;
	@FXML
	private TableColumn<Station, Boolean> anhangColumn;

	@FXML
	private StationDataController stationDataController;

	private ObservableList<Station> stationen;

	private Anlage anlage;

	private Stage dialogStage;

	public StationenOverviewController() {

	}

	@FXML
	private void initialize() {

		stationDataController.setEditable(false);

		table.setEditable(true);

		equipmentColumn.setCellValueFactory(cellData -> cellData.getValue().equipmentProperty());

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		// auftragColumn.setCellValueFactory(cellData ->
		// cellData.getValue().auftragProperty());
		// auftragColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		auftragColumn.setCellValueFactory(new PropertyValueFactory<Station, String>("auftrag"));
		auftragColumn.setCellFactory(TextFieldTableCell.forTableColumn());
		auftragColumn.setOnEditCommit(new EventHandler<CellEditEvent<Station, String>>() {
			@Override
			public void handle(CellEditEvent<Station, String> t) {
				((Station) t.getTableView().getItems().get(t.getTablePosition().getRow())).setAuftrag(t.getNewValue());
				Service.getInstance().updateStation(table.getSelectionModel().getSelectedItem());
			}
		});

		statusColumn.setCellValueFactory(new PropertyValueFactory<Station, Boolean>("status"));
		statusColumn.setCellFactory(CheckBoxTableCell.forTableColumn(statusColumn));
		statusColumn.setEditable(false);

		tpmColumn.setCellValueFactory(new PropertyValueFactory<Station, Boolean>("tpm"));
		tpmColumn.setCellFactory(CheckBoxTableCell.forTableColumn(tpmColumn));
		tpmColumn.setEditable(false);

		robotColumn.setCellValueFactory(new PropertyValueFactory<Station, Boolean>("robot"));
		robotColumn.setCellFactory(CheckBoxTableCell.forTableColumn(robotColumn));
		robotColumn.setEditable(false);

		anhangColumn.setCellValueFactory(new PropertyValueFactory<Station, Boolean>("anhang"));
		anhangColumn.setCellFactory(CheckBoxTableCell.forTableColumn(anhangColumn));
		anhangColumn.setEditable(false);

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2)
					if (event.getButton() == MouseButton.PRIMARY)
						showEditDialog(table.getSelectionModel().getSelectedItem(), anlage);

			}
		});
		table.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDetails(newValue));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setData(Anlage anlage) {

		this.anlage = anlage;

		stationen = FXCollections.observableArrayList(Service.getInstance().getStationenFromAnlage(anlage));

		for (Station station : stationen) {
			if (Service.getInstance().getAnhangAnzahlFromStation(station))
				station.setAnhang(true);
		}

		table.setItems(stationen);

	}

	private void showDetails(Station data) {

		stationDataController.setEditable(false);
		stationDataController.setData(data);
		stationDataController.setDialogStage(dialogStage);
	}

	@FXML
	private void handleDelete() {

		Station data;

		if (table.getSelectionModel().getSelectedItem() != null) {

			data = table.getSelectionModel().getSelectedItem();

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.getDialogPane().getStyleableParent();
			alert.setTitle("MaintenanceManager");
			alert.setHeaderText("Entfernen");
			alert.setContentText("Wollen Sie die Daten wirklich entfernen?");

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			stage.toFront();

			ButtonType buttonTypeOk = new ButtonType("Ja");
			ButtonType buttonTypeCancel = new ButtonType("Nein");
			alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == buttonTypeOk) {
				Service.getInstance().deleteStation(data);
				if (!Service.getInstance().isErrorStatus()) {
					setData(anlage);

				}
			}

		} else
			new NoSelectionAlert(dialogStage).showAndWait();

	}

	@FXML
	private void handleDeleteKeyPressed(KeyEvent event) {

		if (event.getEventType() == KeyEvent.KEY_PRESSED)
			if (event.getCode() == KeyCode.DELETE)
				handleDelete();

	}

	@FXML
	private void handleEdit() {

		if (table.getSelectionModel().getSelectedItem() != null) {

			showEditDialog(table.getSelectionModel().getSelectedItem(), anlage);
			setData(anlage);

		} else
			new NoSelectionAlert(dialogStage).showAndWait();

	}

	@FXML
	private void handleNew() {

		Station station = new Station();

		boolean okClicked = showEditDialog(station, anlage);
		if (okClicked) {
			setData(anlage);
		}
	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			setData(anlage);
		}

	}

	private boolean showEditDialog(Station station, Anlage anlage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/station/StationEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			if (station.getId() == 0)
				dialogStage.setTitle("Station: Erstellen");
			else
				dialogStage.setTitle("Station: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			StationEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(station, anlage);

			dialogStage.showAndWait();

			showDetails(station);

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

	}

}
