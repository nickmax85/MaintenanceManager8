package com.maintenance.view.anlage;

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

import com.maintenance.Main;
import com.maintenance.db.dto.Abteilung;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.InfoAlert;
import com.maintenance.view.alert.InputValidatorAlert;
import com.maintenance.view.alert.NoLastWartungDateAlert;
import com.maintenance.view.calendarwartung.CalendarWartungOverviewController;
import com.maintenance.view.wartung.EIntervallDateUnit;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;

public class AnlageDataController {

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
	public CheckBox untermenuCheckBox;
	@FXML
	public TextField jahresStueckzahlField;
	@FXML
	public TextField aktuelleStueckzahlField;
	@FXML
	public TextField wartungStueckIntervallField;
	@FXML
	public TextField warnungStueckField;
	@FXML
	public TextField fehlerStueckField;
	@FXML
	public CheckBox auswertungCheckBox;
	@FXML
	public TextField lastWartungStueckField;
	@FXML
	public TextArea wartungsPlanField;
	@FXML
	public Hyperlink wartungsPlanLink;

	@FXML
	public ComboBox<Abteilung> abteilungComboBox;
	@FXML
	public ComboBox<Integer> tpmStepComboBox;
	@FXML
	public TextArea produkteField;

	@FXML
	public ComboBox<EWartungArt> wartungArtComboBox;

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
	private GridPane dateGridPane;
	@FXML
	private GridPane calendarWartungGridPane;
	@FXML
	private GridPane warnungenGridPane;

	@FXML
	public DatePicker createDateField;

	private Anlage anlage;

	private Stage dialogStage;

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

		untermenuCheckBox.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {
				if (untermenuCheckBox.isSelected()) {
					wartungArtComboBox.getSelectionModel().select(EWartungArt.STUECKZAHL);
					wartungArtComboBox.setDisable(true);
					warnungenGridPane.setDisable(true);

					wartungArtComboBoxEvent();
				} else {
					wartungArtComboBox.setDisable(false);
					warnungenGridPane.setDisable(false);
					calendarWartungGridPane.setDisable(false);

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

	public void setData(Anlage data) {

		this.anlage = data;

		ObservableList<Abteilung> abteilungen = FXCollections.observableArrayList();
		abteilungen.setAll(Service.getInstance().getAllAbteilungen());
		abteilungComboBox.setItems(abteilungen);
		abteilungComboBox.setConverter(new StringConverter<Abteilung>() {

			@Override
			public Abteilung fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Abteilung object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		});

		ObservableList<EWartungArt> wartungsArt = FXCollections.observableArrayList(EWartungArt.values());
		wartungArtComboBox.setItems(wartungsArt);

		ObservableList<EIntervallDateUnit> wartungDateUnit = FXCollections
				.observableArrayList(EIntervallDateUnit.values());
		wartungDateUnitComboBox.setItems(wartungDateUnit);

		ObservableList<EIntervallDateUnit> warnungDateUnit = FXCollections
				.observableArrayList(EIntervallDateUnit.values());
		warnungDateUnitComboBox.setItems(warnungDateUnit);

		wartungDateUnitComboBox.getSelectionModel().select(data.getIntervallDateUnit());
		warnungDateUnitComboBox.getSelectionModel().select(data.getWarnungDateUnit());

		selectWartungDateIntervall();
		selectWarnungDate();
		selectTPMStep();

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
			untermenuCheckBox.setSelected(data.isSubMenu());
			wartungsPlanField.setText(data.getWartungsplanLink());

			if (untermenuCheckBox.isSelected()) {
				wartungArtComboBox.getSelectionModel().select(EWartungArt.STUECKZAHL);
				wartungArtComboBox.setDisable(true);
				warnungenGridPane.setDisable(true);
				// calendarWartungGridPane.setDisable(true);
				wartungArtComboBoxEvent();
			}
			produkteField.setText(data.getProdukte());

			wartungArtComboBox.getSelectionModel().select(data.getWartungArt());

			wartungArtComboBoxEvent();

			jahresStueckzahlField.setText(String.valueOf(data.getJahresStueck()));
			aktuelleStueckzahlField.setText(String.valueOf(data.getAktuelleStueck()));
			wartungStueckIntervallField.setText(String.valueOf(data.getWartungStueckIntervall()));
			warnungStueckField.setText(String.valueOf(data.getWartungStueckWarnung()));
			fehlerStueckField.setText(String.valueOf(data.getWartungStueckFehler()));
			auswertungCheckBox.setSelected(data.isAuswertung());
			auswertungCheckBox.setTooltip(new Tooltip(
					"Auswahl ob die Anlage mit den darunterliegenden Stationen in den Dashboard-Auswertungen berücksichtigt wird"));
			lastWartungStueckField.setText(String.valueOf(data.getLastWartungStueckzahl()));
			//
			// if (data.getName().contains("1 Aichelin")) {
			// System.out.println(data.getWartungDateIntervall());
			// System.out.println(data.getWartungDateWarnung());
			//
			// }

			wartungDateIntervallComboBox.getSelectionModel().select(data.getWartungDateIntervall());
			warnungDateWarnungComboBox.getSelectionModel().select(data.getWartungDateWarnung());

			createDateCal = Calendar.getInstance();
			createDateCal.setTime(data.getCreateDate());
			createDateField.setValue(LocalDate.of(createDateCal.get(Calendar.YEAR),
					createDateCal.get(Calendar.MONTH) + 1, createDateCal.get(Calendar.DAY_OF_MONTH)));

			nextWartungDate.setText(null);

			if (data.getLastWartungDate() != null) {
				Calendar calLastWartung = Calendar.getInstance();
				calLastWartung.setTime(data.getLastWartungDate());

				lastWartungDateField.setValue(LocalDate.of(calLastWartung.get(Calendar.YEAR),
						calLastWartung.get(Calendar.MONTH) + 1, calLastWartung.get(Calendar.DAY_OF_MONTH)));

				if (data.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal())
					calcNextWartungData();

			}

			if (data.getAbteilungId() != 0)
				for (Abteilung abteilung : abteilungen) {
					if (data.getAbteilungId() == abteilung.getId())
						abteilungComboBox.getSelectionModel().select(abteilung);
				}

			if (data.getMesAnlagen() != null)
				if (!data.getMesAnlagen().isEmpty())
					aktuelleStueckzahlField.setDisable(true);

		}
	}

	private void clearFields() {
		nameField.setText("");
		equipmentField.setText("");
		auftragField.setText("");
		statusCheckBox.setSelected(true);
		untermenuCheckBox.setSelected(false);
		produkteField.setText("");
		auswertungCheckBox.setSelected(false);
		abteilungComboBox.getSelectionModel().clearSelection();

		wartungArtComboBox.getSelectionModel().clearSelection();
		wartungsPlanField.setText("");
		wartungsPlanLink.setText("");

		jahresStueckzahlField.setText("0");
		aktuelleStueckzahlField.setText("0");
		wartungStueckIntervallField.setText("0");
		warnungStueckField.setText("80");
		fehlerStueckField.setText("100");
		lastWartungStueckField.setText("0");

		lastWartungDateField.setValue(null);
		// createDateField.setValue(null);

		wartungDateUnitComboBox.getSelectionModel().clearSelection();
		warnungDateUnitComboBox.getSelectionModel().clearSelection();
		wartungDateIntervallComboBox.getSelectionModel().clearSelection();
		warnungDateWarnungComboBox.getSelectionModel().clearSelection();

		nextWartungDate.setText("");

		stueckzahlGridPane.setDisable(true);
		dateGridPane.setDisable(true);

	}

	public void setEditable(boolean editable) {

		nameField.setDisable(!editable);
		equipmentField.setDisable(!editable);
		auftragField.setDisable(!editable);
		statusCheckBox.setDisable(!editable);
		untermenuCheckBox.setDisable(!editable);
		jahresStueckzahlField.setDisable(!editable);
		aktuelleStueckzahlField.setDisable(!editable);

		wartungStueckIntervallField.setDisable(!editable);
		warnungStueckField.setDisable(!editable);
		fehlerStueckField.setDisable(!editable);
		auswertungCheckBox.setDisable(!editable);
		lastWartungStueckField.setDisable(!editable);

		wartungArtComboBox.setDisable(!editable);
		wartungArtComboBox.getSelectionModel().selectFirst();
		wartungsPlanField.setDisable(!editable);
		wartungsPlanLink.setDisable(!editable);

		lastWartungDateField.setDisable(!editable);
		createDateField.setDisable(!editable);

		produkteField.setDisable(!editable);
		abteilungComboBox.setDisable(!editable);

		stueckzahlGridPane.setDisable(!editable);
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

		if (abteilungComboBox.getSelectionModel().isEmpty())
			errorMessage += "Keine gueltige Abteilung!\n";

		if (wartungArtComboBox.getSelectionModel().isEmpty())
			errorMessage += "Keine gueltige Wartungsart!\n";

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
			warnungStueckField.setDisable(false);
			fehlerStueckField.setDisable(false);
			dateGridPane.setDisable(true);
			if (untermenuCheckBox.isSelected())
				calendarWartungGridPane.setDisable(true);
			else
				calendarWartungGridPane.setDisable(false);
		}

		if (wartungArtComboBox.getSelectionModel().getSelectedItem() == EWartungArt.TIME_INTERVALL) {
			stueckzahlGridPane.setDisable(true);
			warnungStueckField.setDisable(true);
			fehlerStueckField.setDisable(true);
			dateGridPane.setDisable(false);

			calendarWartungGridPane.setDisable(true);

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

	private void selectTPMStep() {

		tpmStepComboBox.getItems().addAll(0, 1, 2, 3, 4, 5);
		tpmStepComboBox.getSelectionModel().select(anlage.getTpmStep());

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

	@FXML
	private void handleCalendarWartungen() {

		if (anlage.getCreateDate() == null) {

			new NoLastWartungDateAlert(dialogStage).showAndWait();
			return;
		}

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/calendarwartung/CalendarWartungOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Kalender Wartungen");
			dialogStage.initOwner(this.dialogStage);

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			CalendarWartungOverviewController controller = loader.getController();
			controller.setDialogStage(this.dialogStage);
			controller.setData(anlage);

			dialogStage.showAndWait();

			setData(Service.getInstance().getAnlage(anlage.getId()));

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;

	}

}
