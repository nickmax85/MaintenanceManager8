package com.maintenance.view.wartung;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.NoSelectionAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WartungenOverviewController {

	private Stage dialogStage;

	@FXML
	private ResourceBundle resources;

	@FXML
	private WartungDataController wartungDataController;

	@FXML
	private TableView<Wartung> table;
	@FXML
	private TableColumn<Wartung, String> auftragColumn;
	@FXML
	private TableColumn<Wartung, Date> startColumn;
	@FXML
	private TableColumn<Wartung, Integer> statusColumn;
	// @FXML
	// private TableColumn<Wartung, Integer> prozentColumn;
	@FXML
	private TableColumn<Wartung, Boolean> anhangColumn;
	@FXML
	private TableColumn<Wartung, Boolean> tpmColumn;

	private Anlage anlage;
	private Station station;

	private Wartung wartung;
	private ObservableList<Wartung> wartungen;

	@FXML
	private void initialize() {

		auftragColumn.setCellValueFactory(cellData -> cellData.getValue().auftragProperty());
		startColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		startColumn.setCellFactory(column -> {
			return new TableCell<Wartung, Date>() {

				@Override
				protected void updateItem(Date item, boolean empty) {

					super.updateItem(item, empty);

					SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {
						String date = sdf.format(item);
						setText(date);
						setAlignment(Pos.CENTER);

					}
				}
			};
		});
		// prozentColumn.setCellValueFactory(new PropertyValueFactory<Wartung,
		// Integer>("prozent"));
		// prozentColumn.setCellFactory(column -> {
		// return new TableCell<Wartung, Integer>() {
		//
		// @Override
		// protected void updateItem(Integer item, boolean empty) {
		//
		// super.updateItem(item, empty);
		//
		// if (item == null || empty) {
		// setText(null);
		// setStyle("");
		// } else {
		//
		// if (item == -1)
		// setText("---");
		// else
		// setText(item + "%");
		// setAlignment(Pos.CENTER);
		//
		// }
		// }
		// };
		// });

		statusColumn.setCellValueFactory(new PropertyValueFactory<Wartung, Integer>("status"));
		statusColumn.setCellFactory(column -> {
			return new TableCell<Wartung, Integer>() {
				@Override
				protected void updateItem(Integer item, boolean empty) {
					super.updateItem(item, empty);

					TableRow<?> currentRow = getTableRow();

					if (item == null || empty) {
						setText(null);
						setStyle("");
					} else {

						if (item == EWartungStatus.DONE.ordinal()) {
							setText(EWartungStatus.DONE.toString());
							setStyle("-fx-background-color:lightgreen");
						}

						if (item == EWartungStatus.NOT_POSSIBLE.ordinal()) {
							setText(EWartungStatus.NOT_POSSIBLE.toString());
							setStyle("-fx-background-color:lightcoral");
						}

						setAlignment(Pos.CENTER);

					}
				}
			};
		});
		anhangColumn.setCellValueFactory(new PropertyValueFactory<Wartung, Boolean>("anhang"));
		anhangColumn.setCellFactory(CheckBoxTableCell.forTableColumn(anhangColumn));

		tpmColumn.setCellValueFactory(new PropertyValueFactory<Wartung, Boolean>("tpm"));
		tpmColumn.setCellFactory(CheckBoxTableCell.forTableColumn(tpmColumn));

		showDetails(null);

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
	}

	public void setData(Anlage anlage) {

		this.anlage = anlage;

		wartungen = FXCollections.observableArrayList(Service.getInstance().getAllWartungenFromAnlage(anlage));

		for (Wartung wartung : wartungen) {
			if (Service.getInstance().getAnhangAnzahlFromWartung(wartung))
				wartung.setAnhang(true);
		}
		table.setItems(wartungen);
	}

	public void setData(Station data) {

		this.station = data;

		wartungen = FXCollections.observableArrayList(Service.getInstance().getAllWartungenFromStation(data));

		for (Wartung wartung : wartungen) {
			if (Service.getInstance().getAnhangAnzahlFromWartung(wartung))
				wartung.setAnhang(true);
		}
		table.setItems(wartungen);
	}

	private void showDetails(Wartung wartung) {

		wartungDataController.setEditable(false);
		wartungDataController.setDialogStage(dialogStage);
		wartungDataController.setData(wartung);

	}

	@FXML
	private void handleDelete() {
		Wartung data;

		if (table.getSelectionModel().getSelectedItem() != null) {

			data = table.getSelectionModel().getSelectedItem();

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.getDialogPane().getStyleableParent();
			alert.setTitle("MaintenanceVisualization");
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

				Service.getInstance().deleteWartung(data);
				if (!Service.getInstance().isErrorStatus()) {
					if (anlage != null)
						setData(anlage);

					if (station != null)
						setData(station);

				}
			}

		}

		else
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

		// War vorher, dass Wartungen aufgemacht werden konnten, deswegen durfte keine
		// weitere Wartung erstellt werden
		// if (station != null)
		// for (Wartung wartung :
		// Service.getInstance().getAllWartungenFromStation(station)) {
		//
		// if (wartung.getStatus() == EWartungStatus.IN_PROGRESS.ordinal()
		// || wartung.getStatus() == EWartungStatus.OPEN.ordinal()) {
		//
		// new WartungNotPossibleAlert(dialogStage).showAndWait();
		//
		// return;
		// }
		//
		// }
		// if (anlage != null)
		// for (Wartung wartung :
		// Service.getInstance().getAllWartungenFromAnlage(anlage)) {
		//
		// if (wartung.getStatus() == EWartungStatus.IN_PROGRESS.ordinal()
		// || wartung.getStatus() == EWartungStatus.OPEN.ordinal()) {
		//
		// new WartungNotPossibleAlert(dialogStage).showAndWait();
		//
		// return;
		// }
		//
		// }

		Wartung wartung = new Wartung();

		boolean okClicked = showEditDialog(wartung);

		if (okClicked) {

			if (anlage != null)
				setData(anlage);

			if (station != null)
				setData(station);
		}

	}

	@FXML
	private void handleEdit() {

		if (table.getSelectionModel().getSelectedItem() != null) {
			showEditDialog(table.getSelectionModel().getSelectedItem());

			if (anlage != null)
				setData(anlage);

			if (station != null)
				setData(station);

		} else
			new NoSelectionAlert(dialogStage).showAndWait();

	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			if (anlage != null)
				setData(anlage);

			if (station != null)
				setData(station);
		}

	}

	private boolean showEditDialog(Wartung data) {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/wartung/WartungEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());
			if (data.getId() == 0)
				dialogStage.setTitle("Wartung: Erstellen");
			else
				dialogStage.setTitle("Wartung: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			WartungEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(anlage);
			controller.setData(station);
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
