package com.maintenance.view.wartung;

import java.io.IOException;
import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.InputValidatorAlert;
import com.maintenance.view.anhang.AnhangOverviewController;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class WartungDataController {

	@FXML
	private ResourceBundle resources;

	@FXML
	public TextField auftragField;
	@FXML
	public DatePicker datumField;
	@FXML
	public TextArea mitarbeiterField;
	@FXML
	public TextArea informationField;
	// @FXML
	// public HTMLEditor informationField;
	@FXML
	public ComboBox<EWartungStatus> statusComboBox;
	@FXML
	public Button anhaengeButton;

	private Anlage anlage;
	private Station station;

	private Stage dialogStage;

	private Wartung wartung;

	public void setEditable(boolean editable) {

		auftragField.setDisable(!editable);
		datumField.setDisable(!editable);
		mitarbeiterField.setDisable(!editable);
		informationField.setDisable(!editable);
		statusComboBox.setDisable(!editable);
		// anhaengeButton.setDisable(!editable);

	}

	@FXML
	private void initialize() {

		ObservableList<EWartungStatus> status = FXCollections.observableArrayList(EWartungStatus.values());
		statusComboBox.setItems(status);

	}

	public void setData(Anlage anlage) {

		this.anlage = anlage;

	}

	public void setData(Station data) {

		this.station = data;
	}

	public void setData(Wartung wartung) {

		this.wartung = wartung;

		if (wartung == null || wartung.getId() == 0) {
			clearFields();
			return;
		}

		if (wartung.getId() != 0) {

			auftragField.setText(wartung.getAuftrag());

			Calendar cal = Calendar.getInstance();
			cal.setTime(wartung.getDate());
			datumField.setValue(
					LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));

			if (wartung.getDate() != null) {
				cal.setTime(wartung.getDate());
				datumField.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH)));

			}

			statusComboBox.getSelectionModel().select(EWartungStatus.values()[wartung.getStatus()]);
			mitarbeiterField.setText(wartung.getMitarbeiter());
			informationField.setText(wartung.getInfo());

		}
	}

	private void clearFields() {

		auftragField.setText("");
		if (anlage != null)
			auftragField.setText(anlage.getAuftrag());
		if (station != null)
			auftragField.setText(station.getAuftrag());

		datumField.setValue(LocalDate.now());
		statusComboBox.getSelectionModel().select(null);
		mitarbeiterField.setText("");
		informationField.setText("");

	}

	public boolean validateFields() {

		String errorMessage = "";

		if (statusComboBox.getSelectionModel().isEmpty())
			errorMessage += "Kein gueltiger Status!\n";

		if (datumField.getValue() == null)
			errorMessage += "Kein gueltiges Datum!\n";

		if (errorMessage.length() == 0)
			return true;
		else {
			new InputValidatorAlert(dialogStage, errorMessage).showAndWait();
			return false;
		}

	}

	@FXML
	private boolean handleAnhaenge() {

		if (wartung == null || wartung.getId() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(dialogStage);
			alert.setTitle("Information");
			alert.setHeaderText("Anhänge");
			alert.setContentText("Anhänge können erst hinzugefügt werden, wenn die Wartung gespeichert wurde.\n\n"
					+ "Bitte die Anhänge nach dem Speichern in der Übericht Wartungen hinzufügen.");
			alert.showAndWait();
			return false;
		}

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
			controller.setData(wartung);

			dialogStage.showAndWait();

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
