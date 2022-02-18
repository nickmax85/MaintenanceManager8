package com.maintenance.view.station;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.dto.Wartung.EWartungTyp;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class StationEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private CheckBox statusBox;
	@FXML
	private StationDataController stationDataController;

	private Stage dialogStage;

	private Anlage anlage;
	private Station station;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
	}

	public void setData(Station data, Anlage anlage) {

		this.station = data;
		this.anlage = anlage;

		station.setAnlageId(anlage.getId());
		station.setAnlage(anlage);

		stationDataController.setData(data);
		stationDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {

		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!stationDataController.isInputValid()) {

			return;
		}

		station.setName(stationDataController.nameField.getText());
		station.setEquipment(stationDataController.equipmentField.getText());
		station.setAuftrag(stationDataController.auftragField.getText());
		station.setStatus(stationDataController.statusCheckBox.isSelected());

		if (stationDataController.wartungTypComboBox.getSelectionModel().getSelectedItem() == EWartungTyp.MAINTENANCE) {
			station.setTpm(false);
			station.setRobot(false);
		} else if (stationDataController.wartungTypComboBox.getSelectionModel()
				.getSelectedItem() == EWartungTyp.AUTONOMOUS_TPM) {

			station.setTpm(true);
			station.setRobot(false);
		}
		else if (stationDataController.wartungTypComboBox.getSelectionModel()
				.getSelectedItem() == EWartungTyp.ROBOT) {

			station.setTpm(false);
			station.setRobot(true);
		}
	
		station.setWartungArt(stationDataController.wartungArtComboBox.getSelectionModel().getSelectedItem().ordinal());
		station.setWartungsplanLink(stationDataController.wartungsPlanField.getText());
		station.setMailSent(stationDataController.mailSentCheckBox.isSelected());

		if (stationDataController.createDateField.getValue() != null) {
			Date date = Date
					.from(stationDataController.createDateField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
			station.setCreateDate(date);
		}

		if (stationDataController.lastWartungDateField.getValue() != null) {
			Date date = Date.from(
					stationDataController.lastWartungDateField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
			station.setLastWartungDate(date);
		}

		if (stationDataController.wartungArtComboBox.getSelectionModel().getSelectedItem() == EWartungArt.STUECKZAHL) {
			station.setLastWartungStueckzahl(Integer.parseInt(stationDataController.lastWartungStueckField.getText()));
			station.setWartungStueckIntervall(
					Integer.parseInt(stationDataController.wartungStueckIntervallField.getText()));
			station.setWartungStueckWarnung(Integer.parseInt(stationDataController.warnungStueckField.getText()));
			station.setWartungStueckFehler(Integer.parseInt(stationDataController.fehlerStueckField.getText()));

		}

		if (stationDataController.wartungArtComboBox.getSelectionModel()
				.getSelectedItem() == EWartungArt.TIME_INTERVALL) {
			station.setIntervallDateUnit(
					stationDataController.wartungDateUnitComboBox.getSelectionModel().getSelectedItem().ordinal());

			station.setWartungDateIntervall(
					stationDataController.wartungDateIntervallComboBox.getSelectionModel().getSelectedItem());

			station.setWarnungDateUnit(
					stationDataController.warnungDateUnitComboBox.getSelectionModel().getSelectedItem().ordinal());
			station.setWartungDateWarnung(
					stationDataController.warnungDateWarnungComboBox.getSelectionModel().getSelectedItem());
		}

		if (station.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			dialogStage.close();

		}

	}

	private void insert() {

		PanelFormat panelFormat = new PanelFormat();
		panelFormat.setX(0);
		panelFormat.setY(0);
		panelFormat.setWidth(800);
		panelFormat.setHeigth(400);

		Service.getInstance().insertPanelFormat(panelFormat);

		if (!Service.getInstance().isErrorStatus()) {
			station.setPanelFormat(panelFormat);
			station.setPanelFormatId(panelFormat.getId());
			Service.getInstance().insertStation(station);
		}

	}

	private void update() {
		Service.getInstance().updateStation(station);
	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
