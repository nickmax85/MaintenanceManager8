package com.maintenance.view.station;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.dto.Wartung.EWartungTyp;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.InfoAlert;
import com.maintenance.view.alert.InputValidatorAlert;
import com.maintenance.view.anhang.AnhangOverviewController;
import com.maintenance.view.wartung.EIntervallDateUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class StationDataController {

	@FXML
	private ResourceBundle resources;

	@FXML
	public TextField nameField;
	@FXML
	public TextField auftragField;
	@FXML
	public TextField equipmentField;
	@FXML
	public CheckBox statusCheckBox;
	@FXML
	public TextField wartungStueckIntervallField;
	@FXML
	public TextField warnungStueckField;
	@FXML
	public TextField fehlerStueckField;
	
	@FXML
	public TextField lastWartungStueckField;
	@FXML
	public TextArea wartungsPlanField;
	@FXML
	public Hyperlink wartungsPlanLink;
	@FXML
	public Button anhaengeButton;
	
	@FXML
	public CheckBox mailSentCheckBox;

	@FXML
	public ComboBox<EWartungArt> wartungArtComboBox;
	@FXML
	public ComboBox<EWartungTyp> wartungTypComboBox;
	@FXML
	public DatePicker lastWartungDateField;

	@FXML
	public ComboBox<Integer> wartungDateIntervallComboBox;
	@FXML
	public ComboBox<EIntervallDateUnit> wartungDateUnitComboBox;

	@FXML
	public ComboBox<Integer> warnungDateWarnungComboBox;
	@FXML
	public ComboBox<EIntervallDateUnit> warnungDateUnitComboBox;

	@FXML
	public Label nextWartungDate;

	@FXML
	private GridPane stueckzahlGridPane;
	@FXML
	private GridPane warnungenGridPane;
	@FXML
	private GridPane dateGridPane;

	@FXML
	public DatePicker createDateField;

	private Stage dialogStage;

	private Station station;

	@FXML
	private void initialize() {

		wartungDateUnitComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				selectWartungDateIntervall();

				calcNextWartungData();

			}

		});

		wartungDateIntervallComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				calcNextWartungData();

			}
		});

		warnungDateUnitComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				selectWarnungDate();

			}

		});

		warnungDateWarnungComboBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

			}
		});

		lastWartungDateField.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				calcNextWartungData();

			}
		});

		wartungsPlanLink.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (wartungsPlanField.getText().isEmpty()) {
					new InfoAlert(null).showAndWait();
				}

				else {
					try {

						System.out.println(wartungsPlanField.getText());

						if (wartungsPlanField.getText().contains("http"))
							try {

								String link = wartungsPlanField.getText().replace("{", "%7B");
								link = link.replace("}", "%7D");
								openWebpage(new URL(link).toURI());
							} catch (URISyntaxException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						else
							Runtime.getRuntime().exec("explorer " + wartungsPlanField.getText());

					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}

			}
		});
	}

	public static boolean openWebpage(URI uri) {
		Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
		if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
			try {
				desktop.browse(uri);
				return true;
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	public static boolean openWebpage(URL url) {
		try {
			return openWebpage(url.toURI());
		} catch (URISyntaxException e) {
			e.printStackTrace();
		}
		return false;
	}

	public void setData(Station data) {

		this.station = data;
		


		ObservableList<EWartungArt> wartungsArt = FXCollections.observableArrayList(EWartungArt.values());
		wartungArtComboBox.setItems(wartungsArt);

		ObservableList<EWartungTyp> wartungsTyp = FXCollections.observableArrayList(EWartungTyp.values());
		wartungTypComboBox.setItems(wartungsTyp);

		ObservableList<EIntervallDateUnit> wartungDateIntervall = FXCollections
				.observableArrayList(EIntervallDateUnit.values());
		wartungDateUnitComboBox.setItems(wartungDateIntervall);

		ObservableList<EIntervallDateUnit> wartungDateWarnung = FXCollections
				.observableArrayList(EIntervallDateUnit.values());
		warnungDateUnitComboBox.setItems(wartungDateWarnung);

		wartungDateUnitComboBox.getSelectionModel().select(data.getIntervallDateUnit());
		warnungDateUnitComboBox.getSelectionModel().select(data.getWarnungDateUnit());

		selectWartungDateIntervall();
		selectWarnungDate();

		lastWartungDateField.setValue(null);
		Calendar createDateCal = Calendar.getInstance();
		createDateField.setValue(LocalDate.of(createDateCal.get(Calendar.YEAR), createDateCal.get(Calendar.MONTH) + 1,
				createDateCal.get(Calendar.DAY_OF_MONTH)));

		if (data == null || data.getId() == 0) {
			clearFields();
			return;
		}

		if (data.getId() != 0) {

			nameField.setText(data.getName());
			equipmentField.setText(data.getEquipment());
			auftragField.setText(data.getAuftrag());
			statusCheckBox.setSelected(data.isStatus());
		
			
			mailSentCheckBox.setSelected(data.isMailSent());
			

			wartungArtComboBox.getSelectionModel().select(data.getWartungArt());

			if (data.isTpm())
				wartungTypComboBox.getSelectionModel().select(EWartungTyp.AUTONOMOUS_TPM);
			else if (data.isRobot())
				wartungTypComboBox.getSelectionModel().select(EWartungTyp.ROBOT);
			else
				wartungTypComboBox.getSelectionModel().select(EWartungTyp.MAINTENANCE);

			wartungsPlanField.setText(data.getWartungsplanLink());

			wartungArtComboBoxEvent();

			wartungStueckIntervallField.setText(String.valueOf(data.getWartungStueckIntervall()));
			warnungStueckField.setText(String.valueOf(data.getWartungStueckWarnung()));
			fehlerStueckField.setText(String.valueOf(data.getWartungStueckFehler()));
			lastWartungStueckField.setText(String.valueOf(data.getLastWartungStueckzahl()));

			wartungDateIntervallComboBox.getSelectionModel().select(station.getWartungDateIntervall());
			warnungDateWarnungComboBox.getSelectionModel().select(station.getWartungDateWarnung());

			createDateCal = Calendar.getInstance();
			createDateCal.setTime(data.getCreateDate());
			createDateField.setValue(LocalDate.of(createDateCal.get(Calendar.YEAR),
					createDateCal.get(Calendar.MONTH) + 1, createDateCal.get(Calendar.DAY_OF_MONTH)));

			nextWartungDate.setText(null);

			if (data.getLastWartungDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(data.getLastWartungDate());
				lastWartungDateField.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH)));

				calcNextWartungData();
			}

		}

	}

	private void clearFields() {

		nameField.setText("");
		equipmentField.setText("");
		auftragField.setText("");
		statusCheckBox.setSelected(true);
	

		wartungArtComboBox.getSelectionModel().clearSelection();
		wartungTypComboBox.getSelectionModel().clearSelection();
		wartungsPlanField.setText("");
		wartungsPlanLink.setText("");

		wartungStueckIntervallField.setText("0");
		warnungStueckField.setText("80");
		fehlerStueckField.setText("100");
		
		lastWartungStueckField.setText("0");

		lastWartungDateField.setValue(null);
		// createDateField.setValue(null);
		mailSentCheckBox.setSelected(false);

		wartungDateUnitComboBox.getSelectionModel().clearSelection();
		warnungDateUnitComboBox.getSelectionModel().clearSelection();
		wartungDateIntervallComboBox.getSelectionModel().clearSelection();
		warnungDateWarnungComboBox.getSelectionModel().clearSelection();

		nextWartungDate.setText("");

		stueckzahlGridPane.setDisable(true);
		warnungenGridPane.setDisable(true);
		dateGridPane.setDisable(true);
	}

	public void setEditable(boolean editable) {

		nameField.setDisable(!editable);
		equipmentField.setDisable(!editable);
		auftragField.setDisable(!editable);
		statusCheckBox.setDisable(!editable);
		mailSentCheckBox.setDisable(!editable);

		wartungStueckIntervallField.setDisable(!editable);
		warnungStueckField.setDisable(!editable);
		fehlerStueckField.setDisable(!editable);
		lastWartungStueckField.setDisable(!editable);

		wartungArtComboBox.setDisable(!editable);
		wartungArtComboBox.getSelectionModel().selectFirst();
		wartungTypComboBox.setDisable(!editable);
		wartungTypComboBox.getSelectionModel().selectFirst();
		wartungsPlanField.setDisable(!editable);
		wartungsPlanLink.setDisable(!editable);

		lastWartungDateField.setDisable(!editable);
		createDateField.setDisable(!editable);

		stueckzahlGridPane.setDisable(!editable);
		warnungenGridPane.setDisable(!editable);
		dateGridPane.setDisable(!editable);

		wartungDateUnitComboBox.setDisable(!editable);
		warnungDateUnitComboBox.setDisable(!editable);
		wartungDateIntervallComboBox.setDisable(!editable);
		warnungDateWarnungComboBox.setDisable(!editable);
	}

	public boolean isInputValid() {

		String errorMessage = "";

		if (nameField.getText().isEmpty())
			errorMessage += "Kein gueltiger Name!\n";

		if (wartungArtComboBox.getSelectionModel().isEmpty())
			errorMessage += "Keine gueltige Wartungsart!\n";
		
		if (wartungTypComboBox.getSelectionModel().isEmpty())
			errorMessage += "Kein gueltiger Wartungstyp!\n";

		if (createDateField.getValue() == null)
			errorMessage += "Kein gueltiges Erstelldatum!\n";

		if (wartungArtComboBox.getSelectionModel().getSelectedItem() == EWartungArt.TIME_INTERVALL) {
			if (wartungDateUnitComboBox.getSelectionModel().isEmpty())
				errorMessage += "Kein gueltiges Datum Intervall!\n";
			if (wartungDateIntervallComboBox.getSelectionModel().isEmpty()
					|| warnungDateWarnungComboBox.getSelectionModel().isEmpty())
				errorMessage += "Kein gueltiges Datum Intervall!\n";
			if (warnungDateUnitComboBox.getSelectionModel().isEmpty())
				errorMessage += "Kein gueltiges Datum Intervall!\n";
		}

		if (errorMessage.length() == 0)
			return true;
		else {
			new InputValidatorAlert(dialogStage, errorMessage).showAndWait();
			return false;
		}

	}

	@FXML
	private void wartungArtComboBoxEvent() {

		if (wartungArtComboBox.getSelectionModel().getSelectedItem() == EWartungArt.STUECKZAHL) {
			stueckzahlGridPane.setDisable(false);
			warnungenGridPane.setDisable(false);
			dateGridPane.setDisable(true);
		}

		if (wartungArtComboBox.getSelectionModel().getSelectedItem() == EWartungArt.TIME_INTERVALL) {
			stueckzahlGridPane.setDisable(true);
			warnungenGridPane.setDisable(true);
			dateGridPane.setDisable(false);
		}

	}

	@FXML
	private boolean handleAnhaenge() {
		
		
		if (station.getId() == 0) {
			Alert alert = new Alert(AlertType.INFORMATION);
			alert.initOwner(dialogStage);
			alert.setTitle("Information");
			alert.setHeaderText("Anhänge");
			alert.setContentText("Anhänge können erst hinzugefügt werden, wenn die Station gespeichert wurde.\n\n"
					+ "Bitte die Anhänge nach dem Speichern in der Übersicht Stationen hinzufügen.");
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
			controller.setData(station);

			dialogStage.showAndWait();

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private void selectWartungDateIntervall() {

		List<Integer> l = new ArrayList<>();

		if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.DEFAULT) {

			for (int i = 0; i < 1; i++) {

				l.add(i);
			}

			ObservableList<Integer> no = FXCollections.observableArrayList(l);

			wartungDateIntervallComboBox.setItems(no);
			wartungDateIntervallComboBox.getSelectionModel().selectFirst();

		}
		if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.DAY) {

			for (int i = 0; i < 32; i++) {

				l.add(i);
			}

			ObservableList<Integer> days = FXCollections.observableArrayList(l);

			wartungDateIntervallComboBox.setItems(days);

		}
		if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.WEEK) {

			for (int i = 0; i < 13; i++) {

				l.add(i);
			}

			ObservableList<Integer> weeks = FXCollections.observableArrayList(l);

			wartungDateIntervallComboBox.setItems(weeks);

		}
		if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.MONTH) {

			for (int i = 0; i < 24; i++) {

				l.add(i);
			}

			ObservableList<Integer> months = FXCollections.observableArrayList(l);

			wartungDateIntervallComboBox.setItems(months);

		}
		if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.YEAR) {

			for (int i = 0; i < 11; i++) {

				l.add(i);
			}

			ObservableList<Integer> years = FXCollections.observableArrayList(l);

			wartungDateIntervallComboBox.setItems(years);

		}

	}

	private void selectWarnungDate() {

		List<Integer> l = new ArrayList<>();

		if (warnungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.DEFAULT) {

			for (int i = 0; i < 1; i++) {

				l.add(i);
			}

			ObservableList<Integer> no = FXCollections.observableArrayList(l);

			warnungDateWarnungComboBox.setItems(no);
			warnungDateWarnungComboBox.getSelectionModel().selectFirst();

		}

		if (warnungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.DAY) {

			for (int i = 0; i < 32; i++) {

				l.add(i);
			}

			ObservableList<Integer> days = FXCollections.observableArrayList(l);

			warnungDateWarnungComboBox.setItems(days);

		}
		if (warnungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.WEEK) {

			for (int i = 0; i < 13; i++) {

				l.add(i);
			}

			ObservableList<Integer> weeks = FXCollections.observableArrayList(l);

			warnungDateWarnungComboBox.setItems(weeks);

		}
		if (warnungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.MONTH) {

			for (int i = 0; i < 12; i++) {

				l.add(i);
			}

			ObservableList<Integer> months = FXCollections.observableArrayList(l);

			warnungDateWarnungComboBox.setItems(months);

		}
		if (warnungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.YEAR) {

			for (int i = 0; i < 11; i++) {

				l.add(i);
			}

			ObservableList<Integer> years = FXCollections.observableArrayList(l);

			warnungDateWarnungComboBox.setItems(years);

		}

	}

	private void calcNextWartungData() {

		if (lastWartungDateField.getValue() != null && !wartungDateIntervallComboBox.getSelectionModel().isEmpty()) {

			Calendar calLastWartung = Calendar.getInstance();
			Date date1 = Date.from(lastWartungDateField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
			calLastWartung.setTime(date1);

			if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.DAY) {

				calLastWartung.add(Calendar.DAY_OF_YEAR,
						wartungDateIntervallComboBox.getSelectionModel().getSelectedItem());

			}

			if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.WEEK) {

				calLastWartung.add(Calendar.WEEK_OF_YEAR,
						wartungDateIntervallComboBox.getSelectionModel().getSelectedItem());

			}

			if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.MONTH) {

				calLastWartung.add(Calendar.MONTH, wartungDateIntervallComboBox.getSelectionModel().getSelectedItem());

			}

			if (wartungDateUnitComboBox.getSelectionModel().getSelectedItem() == EIntervallDateUnit.YEAR) {

				calLastWartung.add(Calendar.YEAR, wartungDateIntervallComboBox.getSelectionModel().getSelectedItem());

			}

			SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
			nextWartungDate.setText(df.format(calLastWartung.getTime()));

		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

	}

}
