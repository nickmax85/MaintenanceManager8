package com.maintenance.view.calendarwartung;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import com.maintenance.Main;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;

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
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class CalendarWartungOverviewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private TableView<CalendarWartung> table;
	@FXML
	private TableColumn<CalendarWartung, Date> dateColumn;
	@FXML
	private TableColumn<CalendarWartung, String> remarkColumn;

	@FXML
	private Label nameLabel;

	@FXML
	private CalendarWartungDataController calendarWartungDataController;

	private Anlage anlage;

	@FXML
	private void initialize() {

		calendarWartungDataController.setEditable(false);

		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		dateColumn.setCellFactory(column -> {
			return new TableCell<CalendarWartung, Date>() {

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
		remarkColumn.setCellValueFactory(cellData -> cellData.getValue().remarkProperty());

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
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setData(Anlage anlage) {

		this.anlage = anlage;

		ObservableList<CalendarWartung> data = FXCollections
				.observableArrayList(Service.getInstance().getCalendarWartungenFromAnlage(anlage));

		table.setItems(data);

	}

	private void showDetails(CalendarWartung data) {

		calendarWartungDataController.setEditable(false);
		calendarWartungDataController.setData(data);
	}

	@FXML
	private void handleDelete() {
		CalendarWartung data;
		int selectedIndex = table.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {

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
				data = table.getSelectionModel().getSelectedItem();
				Service.getInstance().deleteCalendarWartung(data);
				if (!Service.getInstance().isErrorStatus()) {
					table.getItems().remove(data);
					table.getSelectionModel().clearSelection();
					table.refresh();

				}
			}

		}

	}

	@FXML
	private void handleDeleteKeyPressed(KeyEvent event) {

		if (event.getEventType() == KeyEvent.KEY_PRESSED)
			if (event.getCode() == KeyCode.DELETE)
				handleDelete();

	}

	@FXML
	private void handleNew() {

		CalendarWartung data = new CalendarWartung();
		data.setAnlage(anlage);

		boolean okClicked = showEditDialog(data);
		if (okClicked) {
			table.setItems(
					FXCollections.observableArrayList(Service.getInstance().getCalendarWartungenFromAnlage(anlage)));
			table.refresh();
			showDetails(null);
		}
	}

	@FXML
	private void handleEdit() {

		showEditDialog(table.getSelectionModel().getSelectedItem());

	}

	@FXML
	private void handleEditMouseClick(MouseEvent event) {

		if (event.getButton() == MouseButton.PRIMARY)
			if (event.getClickCount() == 2)
				handleEdit();

	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			setData(anlage);
		}

	}

	private boolean showEditDialog(CalendarWartung data) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/calendarwartung/CalendarWartungEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setAlwaysOnTop(true);
			dialogStage.setTitle("Kalender Wartung: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			CalendarWartungEditController controller = loader.getController();
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
		// TODO Auto-generated method stub

	}

}
