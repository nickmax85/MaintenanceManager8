package com.maintenance.view.report;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.view.wartung.EWartungStatus;
import com.maintenance.view.wartung.WartungEditController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableRow;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WartungReportController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private DatePicker dateFrom;
	@FXML
	private DatePicker dateTo;
	@FXML
	private ComboBox<EAuswahl> auswahlComboBox;
	@FXML
	private ComboBox<EWartungStatus> statusComboBox;

	@FXML
	private TableView<WartungReportModel> table;
	@FXML
	private TableColumn<WartungReportModel, String> anlageColumn;
	@FXML
	private TableColumn<WartungReportModel, String> stationColumn;
	@FXML
	private TableColumn<WartungReportModel, Integer> statusColumn;
	@FXML
	private TableColumn<WartungReportModel, Date> dateColumn;
	@FXML
	private TableColumn<WartungReportModel, String> infoColumn;
	@FXML
	private TableColumn<WartungReportModel, String> mitarbeiterColumn;

	private Stage dialogStage;

	@FXML
	private void initialize() {

		dateFrom.setValue(LocalDate.now());
		dateTo.setValue(LocalDate.now());

		ObservableList<EAuswahl> auswahlList = FXCollections.observableArrayList(EAuswahl.values());
		auswahlComboBox.setItems(auswahlList);
		auswahlComboBox.getSelectionModel().select(EAuswahl.ANLAGEN);
		ObservableList<EWartungStatus> statusList = FXCollections.observableArrayList(EWartungStatus.values());
		statusComboBox.setItems(statusList);
		statusComboBox.getSelectionModel().select(EWartungStatus.NOT_POSSIBLE);

		anlageColumn.setCellValueFactory(cellData -> cellData.getValue().anlageProperty());
		stationColumn.setCellValueFactory(cellData -> cellData.getValue().stationProperty());
		statusColumn.setCellValueFactory(new PropertyValueFactory<WartungReportModel, Integer>("status"));
		statusColumn.setCellFactory(column -> {
			return new TableCell<WartungReportModel, Integer>() {
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
		dateColumn.setCellValueFactory(cellData -> cellData.getValue().dateProperty());
		dateColumn.setCellFactory(column -> {
			return new TableCell<WartungReportModel, Date>() {

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
		infoColumn.setCellValueFactory(cellData -> cellData.getValue().infoProperty());
		mitarbeiterColumn.setCellValueFactory(cellData -> cellData.getValue().mitarbeiterProperty());

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2)
					if (event.getButton() == MouseButton.PRIMARY)
						showEditDialog(table.getSelectionModel().getSelectedItem().getWartung());

			}
		});

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	@FXML
	private void handleSearch() {

		List<WartungReportModel> wartungReportModelList = new ArrayList<WartungReportModel>();
		WartungReportModel wartungReportModel;

		Date date1 = Date.from(dateFrom.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sqlDate1 = new java.sql.Date(date1.getTime());

		Date date2 = Date.from(dateTo.getValue().atStartOfDay(ZoneId.systemDefault()).toInstant());
		java.sql.Date sqlDate2 = new java.sql.Date(date2.getTime());

		for (Anlage anlage : Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat()) {

			if (auswahlComboBox.getSelectionModel().getSelectedItem() == EAuswahl.ANLAGEN)
				for (Wartung wartung : Service.getInstance().getWartungenAnlageDate(anlage, sqlDate1, sqlDate2)) {

					if (statusComboBox.getSelectionModel().getSelectedItem().ordinal() == wartung.getStatus()) {

						wartungReportModel = new WartungReportModel();
						wartungReportModel.setWartung(wartung);
						wartungReportModel.setId(wartung.getId());
						wartungReportModel.setAnlage(wartung.getAnlage().getName());
						wartungReportModel.setStation("Anlage");
						wartungReportModel.setStatus(wartung.getStatus());
						wartungReportModel.setDate(wartung.getDate());
						wartungReportModel.setInfo(wartung.getInfo());
						wartungReportModel.setMitarbeiter(wartung.getMitarbeiter());
						wartungReportModelList.add(wartungReportModel);

					}
				}

			if (auswahlComboBox.getSelectionModel().getSelectedItem() == EAuswahl.STATIONEN)
				for (Station station : Service.getInstance().getStationenFromAnlage(anlage)) {
					for (Wartung wartung : Service.getInstance().getWartungenStationDate(station, sqlDate1, sqlDate2)) {

						System.out.println(wartung.getStatus());

						if (statusComboBox.getSelectionModel().getSelectedItem().ordinal() == wartung.getStatus()) {

							wartungReportModel = new WartungReportModel();
							wartungReportModel.setWartung(wartung);
							wartungReportModel.setId(wartung.getId());
							wartungReportModel.setAnlage(anlage.getName());
							wartungReportModel.setStation(station.getName());
							wartungReportModel.setStatus(wartung.getStatus());
							wartungReportModel.setDate(wartung.getDate());
							wartungReportModel.setInfo(wartung.getInfo());
							wartungReportModel.setMitarbeiter(wartung.getMitarbeiter());
							wartungReportModelList.add(wartungReportModel);
						}
					}
				}

		}

		ObservableList<WartungReportModel> wartungen = FXCollections.observableArrayList(wartungReportModelList);
		table.setItems(wartungen);

	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {

		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

	}

	public void saveAsCsv(File file) throws Exception {

		Writer writer = null;

		writer = new BufferedWriter(new FileWriter(file));

		writer.write(anlageColumn.getText() + ";" + stationColumn.getText() + ";" + dateColumn.getText() + ";"
				+ infoColumn.getText() + ";" + mitarbeiterColumn.getText() + "\n");

		for (WartungReportModel data : table.getItems()) {

			String text1 = data.getAnlage() + ";" + data.getStation() + ";" + data.getDate() + ";" + data.getInfo()
					+ ";" + data.getMitarbeiter() + "\n";

			writer.write(text1.replace("null", ""));
		}

		writer.flush();

		writer.close();
	}

	@FXML
	public void exportCSV() throws Exception {

		File file = null;

		DateFormat tf = new SimpleDateFormat("HH-mm-ss");
		String timeString = tf.format(Calendar.getInstance().getTime());

		DateFormat df = new SimpleDateFormat("dd-MM-yyyy");
		String dateString = df.format(Calendar.getInstance().getTime());

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Speichern unter");
		FileChooser.ExtensionFilter extFilterPng = new FileChooser.ExtensionFilter("CSV files (*.csv)", "*.csv");
		chooser.getExtensionFilters().addAll(extFilterPng);

		chooser.setInitialFileName("Wartungen" + ".csv");

		file = chooser.showSaveDialog(dialogStage);

		if (file != null) {

			if (chooser.getSelectedExtensionFilter() == extFilterPng) {
				saveAsCsv(file);
			}
		} else {
			// logger.info("Speichern unter abgebrochen");
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
			controller.setData(data.getAnlage());
			controller.setData(data.getStation());
			controller.setData(data);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
