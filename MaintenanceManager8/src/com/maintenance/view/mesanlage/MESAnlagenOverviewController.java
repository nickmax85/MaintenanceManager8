package com.maintenance.view.mesanlage;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.util.MESAnlageExcelRead;
import com.maintenance.view.alert.GeneralInfoAlert;
import com.maintenance.view.alert.MESAnlageImportAlert;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MESAnlagenOverviewController {

	private static final Logger logger = Logger.getLogger(MESAnlagenOverviewController.class);

	private Stage dialogStage;

	@FXML
	private ResourceBundle resources;

	@FXML
	private TableView<MESAnlage> table;
	@FXML
	private TableColumn<MESAnlage, Number> idColumn;
	@FXML
	private TableColumn<MESAnlage, String> nameColumn;
	@FXML
	private TableColumn<MESAnlage, Number> prodStueckColumn;
	@FXML
	private TableColumn<MESAnlage, Anlage> anlageColumn;
	@FXML
	private TableColumn<MESAnlage, Anlage> anlage2Column;
	@FXML
	private TableColumn<MESAnlage, String> timestampColumn;

	@FXML
	private TextArea importResultTextArea;
	@FXML
	private TextField sheetNrTextField;

	private List<MESAnlage> anlagen;

	@FXML
	private void initialize() {

		importResultTextArea.setEditable(false);
		sheetNrTextField.setText("2");

		idColumn.setCellValueFactory(cellData -> cellData.getValue().idProperty());
		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());
		prodStueckColumn.setCellValueFactory(cellData -> cellData.getValue().prodStueckProperty());
		timestampColumn.setCellValueFactory(cellData -> cellData.getValue().timestampProperty());
		anlageColumn.setCellValueFactory(new PropertyValueFactory<MESAnlage, Anlage>("anlage"));
		anlageColumn.setCellFactory(column -> {
			return new TableCell<MESAnlage, Anlage>() {

				@Override
				protected void updateItem(Anlage item, boolean empty) {
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
		anlage2Column.setCellValueFactory(new PropertyValueFactory<MESAnlage, Anlage>("anlage2"));
		anlage2Column.setCellFactory(column -> {
			return new TableCell<MESAnlage, Anlage>() {

				@Override
				protected void updateItem(Anlage item, boolean empty) {
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

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2)
					if (event.getButton() == MouseButton.PRIMARY)
						showEditDialog(table.getSelectionModel().getSelectedItem());

			}
		});
		// table.getSelectionModel().selectedItemProperty()
		// .addListener((observable, oldValue, newValue) ->
		// showDetails(newValue));

		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setData() {

		ObservableList<MESAnlage> abteilungenFX = FXCollections
				.observableArrayList(Service.getInstance().getMESAnlagen());
		table.setItems(abteilungenFX);

	}

	@FXML
	private void handleUpdateAktuelleStueckzahlenAdd() {

		Service.getInstance().updateAnlageStueckzahlenAdd();

	}

	@FXML
	private void handleUpdateAktuelleStueckzahlenSubtract() {

		Service.getInstance().updateAnlageStueckzahlenSubtract();

	}

	@FXML
	private void handleDelete() {
		MESAnlage data;
		int selectedIndex = table.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.getDialogPane().getStyleableParent();
			alert.setTitle("MaintenanceManager");
			alert.setHeaderText("Entfernen");
			alert.setContentText("Wollen Sie die Daten wirklich entfernen?");

			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.initOwner(dialogStage);
			stage.getIcons().addAll(dialogStage.getIcons());
			stage.setAlwaysOnTop(true);
			stage.toFront();

			ButtonType buttonTypeOk = new ButtonType("Ja");
			ButtonType buttonTypeCancel = new ButtonType("Nein");
			alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == buttonTypeOk) {
				data = table.getSelectionModel().getSelectedItem();
				Service.getInstance().deleteMESAnlage(data);
				if (!Service.getInstance().isErrorStatus()) {
					table.getItems().remove(data);
					table.getSelectionModel().clearSelection();
					table.refresh();

				}
			}

		}

	}

	@FXML
	private void handleImport() {

		File file = null;

		FileChooser chooser = new FileChooser();
		chooser.setTitle("Öffnen");
		FileChooser.ExtensionFilter extFilterCsv = new FileChooser.ExtensionFilter("Excel Dateien (*.xlsx)", "*.xlsx");
		chooser.getExtensionFilters().addAll(extFilterCsv);

		file = chooser.showOpenDialog(dialogStage);

		if (file != null) {

			if (chooser.getSelectedExtensionFilter() == extFilterCsv) {

				try {
					openFile(file);

				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		} else {

		}

	}

	public void openFile(File file) throws Exception {

		int countInserts = 0;
		int countUpdates = 0;

		addText(file.getAbsolutePath() + " Import starten\n");
		addText("Datei: " + file.getAbsolutePath() + " laden\n");

		MESAnlageExcelRead excelRead = new MESAnlageExcelRead(file.getAbsolutePath(),
				Integer.parseInt(sheetNrTextField.getText()) - 1);

		addText("Datei: " + file.getAbsolutePath() + " geladen\n");

		addText("Datei: " + file.getAbsolutePath() + " lesen\n");
		anlagen = excelRead.readExcel(importResultTextArea);
		addText("Anzahl der gefundenen Anlagen: " + anlagen.size() + "\n");
		addText("Datei: " + file.getAbsolutePath() + " gelesen\n");

		logger.info("MESAnlagen: " + anlagen);

		if (anlagen.size() > 0) {
			addText("Daten in Datenbank übertragen\n");

			for (MESAnlage mesAnlage : anlagen) {

				boolean found = false;

				for (MESAnlage mesAnlageDB : Service.getInstance().getMESAnlagen()) {

					if (mesAnlage.getId() == mesAnlageDB.getId()) {
						mesAnlage.setAnlageId(mesAnlageDB.getAnlageId());
						mesAnlage.setAnlage2Id(mesAnlageDB.getAnlage2Id());
						mesAnlage.setAnlage(mesAnlageDB.getAnlage());
						mesAnlage.setAnlage2(mesAnlageDB.getAnlage2());
						found = true;
						break;
					}
				}

				String text = "Id: " + mesAnlage.getId() + "; Name: " + mesAnlage.getName()
						+ "; Produzierte Stückzahl: " + mesAnlage.getProdStueck() + "\n";

				if (found) {
					addText("Update: " + text);
					Service.getInstance().updateMESAnlage(mesAnlage);
					countUpdates++;

				} else {
					addText("Insert: " + text);
					Service.getInstance().insertMESAnlage(mesAnlage);
					countInserts++;
				}
			}

			new MESAnlageImportAlert(dialogStage, countInserts, countUpdates).showAndWait();

			addText("Datei: " + file.getAbsolutePath() + " Import abgeschlossen\n");

		} else
			new GeneralInfoAlert(dialogStage, "Import", "Excel Datei lesen",
					"Es wurden keine Daten im Dokument gefunden").showAndWait();

	}

	private void addText(String text) {

		Platform.runLater(new Runnable() {

			@Override
			public void run() {
				importResultTextArea.appendText(text);

			}
		});

	}

	@FXML
	private void handleDeleteKeyPressed(KeyEvent event) {

		if (event.getEventType() == KeyEvent.KEY_PRESSED)
			if (event.getCode() == KeyCode.DELETE)
				handleDelete();

	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			setData();
		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	private boolean showEditDialog(MESAnlage mesAnlage) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(com.maintenance.Main.class.getResource("view/mesanlage/MESAnlageEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());

			if (mesAnlage.getId() == 0)
				dialogStage.setTitle("Anlagenimport: Erstellen");
			else
				dialogStage.setTitle("Anlagenimport: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			MESAnlageEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(mesAnlage);

			dialogStage.showAndWait();

			table.refresh();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
