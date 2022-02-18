package com.maintenance.view.anlage;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.maintenance.Main;
import com.maintenance.db.dto.Abteilung;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.util.ProzentCalc;
import com.maintenance.util.TableUtils;
import com.maintenance.view.alert.DeleteYesNoAlert;
import com.maintenance.view.alert.NoSelectionAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.ProgressBarTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class AnlagenOverviewController {

	private Stage dialogStage;

	@FXML
	private ResourceBundle resources;
	@FXML
	private TableView<Anlage> table;
	@FXML
	private TableColumn<Anlage, String> equipmentColumn;
	@FXML
	private TableColumn<Anlage, String> nameColumn;
	@FXML
	private TableColumn<Anlage, String> auftragColumn;
	@FXML
	private TableColumn<Anlage, Abteilung> abteilungColumn;
	@FXML
	private TableColumn<Anlage, Boolean> statusColumn;
	@FXML
	private TableColumn<Anlage, Double> intervallColumn;

	@FXML
	private AnlageDataController anlageDataController;

	private ObservableList<Anlage> anlagen;

	private Main main;

	@FXML
	private void initialize() {

		anlageDataController.setEditable(false);

		equipmentColumn.setCellValueFactory(cellData -> cellData.getValue().equipmentProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		auftragColumn.setCellValueFactory(cellData -> cellData.getValue().auftragProperty());
		abteilungColumn.setCellValueFactory(new PropertyValueFactory<Anlage, Abteilung>("abteilung"));
		abteilungColumn.setCellFactory(column -> {
			return new TableCell<Anlage, Abteilung>() {

				@Override
				protected void updateItem(Abteilung item, boolean empty) {
					super.updateItem(item, empty);

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						setText(item.getName());
					}
				}

			};

		});
		statusColumn.setCellValueFactory(new PropertyValueFactory<Anlage, Boolean>("status"));
		statusColumn.setCellFactory(CheckBoxTableCell.forTableColumn(statusColumn));

		intervallColumn.setCellValueFactory(new PropertyValueFactory<Anlage, Double>("intervall"));
		intervallColumn.setCellFactory(ProgressBarTableCell.<Anlage>forTableColumn());

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2)
					if (event.getButton() == MouseButton.PRIMARY)
						showEditDialog(table.getSelectionModel().getSelectedItem());

			}
		});
		table.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDetails(newValue));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
		
		table.getSelectionModel().setCellSelectionEnabled(true);
		table.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		TableUtils.installCopyPasteHandler(table);
	}

	public void setData() {

		anlagen = FXCollections
				.observableArrayList(Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat());

		float prozent;

		List<Station> stationen;
		for (Anlage anlage : anlagen) {

			if (!anlage.isSubMenu()) {

				if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

					anlage.setIntervall(ProzentCalc.calcProzent(anlage) / 100.0f);

				}

				if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					Date nextWartungDate;
					Date lastWartungDate;

					if (anlage.getLastWartungDate() != null)
						lastWartungDate = anlage.getLastWartungDate();
					else
						lastWartungDate = anlage.getCreateDate();

					nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate, anlage.getIntervallDateUnit(),
							anlage.getWartungDateIntervall());
					prozent = ProzentCalc.calcProzent(lastWartungDate.getTime(), nextWartungDate.getTime());

					anlage.setIntervall(prozent / 100.0f);

				}
			}

			if (anlage.isSubMenu()) {

				stationen = Service.getInstance().getStationenFromAnlage(anlage);

				Station stationStueck = ProzentCalc.getMaxIntervallStationStueck(stationen);
				Station stationDate = ProzentCalc.getMaxIntervallStationDate(stationen);

				float prozStueck = 0;
				float prozDate = 0;

				if (stationStueck != null) {
					if (stationStueck.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())

						prozStueck = ProzentCalc.calcProzent(stationStueck);

				}

				if (stationDate != null) {
					if (stationDate.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

						Date nextWartungDate;
						Date lastWartungDate;
						if (stationDate.getLastWartungDate() != null)
							lastWartungDate = stationDate.getLastWartungDate();
						else
							lastWartungDate = stationDate.getCreateDate();

						nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate,
								stationDate.getIntervallDateUnit(), stationDate.getWartungDateIntervall());
						prozent = ProzentCalc.calcProzent(lastWartungDate.getTime(), nextWartungDate.getTime());

					}
				}

				if (prozStueck > prozDate)
					prozent = prozStueck;
				else
					prozent = prozDate;

				anlage.setIntervall(prozent / 100.0f);
			}

		}

		table.setItems(anlagen);

	}

	private void showDetails(Anlage data) {

		anlageDataController.setEditable(false);
		anlageDataController.setDialogStage(dialogStage);
		anlageDataController.setData(data);

	}

	@FXML
	private void handleDelete() {

		Anlage data;

		if (table.getSelectionModel().getSelectedItem() != null) {

			data = table.getSelectionModel().getSelectedItem();

			DeleteYesNoAlert alert = new DeleteYesNoAlert(dialogStage);

			if (alert.isOKButton()) {
				Service.getInstance().deleteAnlage(data);
				if (!Service.getInstance().isErrorStatus()) {
					setData();
				}
			}

		} else
			new NoSelectionAlert(dialogStage).showAndWait();

	}

	@FXML
	private void handleEdit() {

		if (table.getSelectionModel().getSelectedItem() != null) {
			showEditDialog(table.getSelectionModel().getSelectedItem());
			setData();
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
	private void handleNew() {

		Anlage anlage = new Anlage();

		boolean okClicked = showEditDialog(anlage);
		if (okClicked) {
			setData();
		}
	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			setData();

		}

	}

	private boolean showEditDialog(Anlage data) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/anlage/AnlageEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			if (data.getId() == 0)
				dialogStage.setTitle("Anlage: Erstellen");
			else
				dialogStage.setTitle("Anlage: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			AnlageEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(data);

			dialogStage.showAndWait();

			showDetails(data);

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