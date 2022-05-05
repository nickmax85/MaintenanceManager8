package com.maintenance.view.anlage;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.scene.control.CheckBox;
import javafx.stage.Stage;

public class AnlageEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private CheckBox statusBox;
	@FXML
	private AnlageDataController anlageDataController;

	private Stage dialogStage;

	private Anlage data;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Anlage data) {

		this.data = data;

		anlageDataController.setData(data);
		anlageDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!anlageDataController.isInputValid()) {
			return;
		}

		data.setName(anlageDataController.nameField.getText());
		data.setEquipment(anlageDataController.equipmentField.getText());
		data.setAuftrag(anlageDataController.auftragField.getText());
		data.setStatus(anlageDataController.statusCheckBox.isSelected());
		data.setSubMenu(anlageDataController.untermenuCheckBox.isSelected());
		data.setProdukte(anlageDataController.produkteField.getText());
		data.setAuswertung(anlageDataController.auswertungCheckBox.isSelected());
		data.setAbteilung(anlageDataController.abteilungComboBox.getValue());
		data.setAbteilungId(anlageDataController.abteilungComboBox.getValue().getId());
		data.setWartungArt(anlageDataController.wartungArtComboBox.getSelectionModel().getSelectedItem().ordinal());
		data.setWartungsplanLink(anlageDataController.wartungsPlanField.getText());
		data.setTpmStep(anlageDataController.tpmStepComboBox.getValue());

		if (anlageDataController.createDateField.getValue() != null) {
			Date date = Date
					.from(anlageDataController.createDateField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
			data.setCreateDate(date);
		}

		data.setLastWartungStueckzahl(Integer.parseInt(anlageDataController.lastWartungStueckField.getText()));
		if (anlageDataController.lastWartungDateField.getValue() != null) {
			Date date = Date.from(
					anlageDataController.lastWartungDateField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
			data.setLastWartungDate(date);
		}

		if (anlageDataController.wartungArtComboBox.getSelectionModel().getSelectedItem() == EWartungArt.STUECKZAHL) {
			data.setAktuelleStueck(Integer.parseInt(anlageDataController.aktuelleStueckzahlField.getText()));
			data.setJahresStueck(Integer.parseInt(anlageDataController.jahresStueckzahlField.getText()));
			// data.setLastWartungStueckzahl(Integer.parseInt(anlageDataController.lastWartungStueckField.getText()));
			data.setWartungStueckIntervall(
					Integer.parseInt(anlageDataController.wartungStueckIntervallField.getText()));
			data.setWartungStueckWarnung(Integer.parseInt(anlageDataController.warnungStueckField.getText()));
			data.setWartungStueckFehler(Integer.parseInt(anlageDataController.fehlerStueckField.getText()));
		}

		if (anlageDataController.wartungArtComboBox.getSelectionModel()
				.getSelectedItem() == EWartungArt.TIME_INTERVALL) {
			// Date date = Date.from(
			// anlageDataController.lastWartungDateField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
			// data.setLastWartungDate(date);

			data.setIntervallDateUnit(
					anlageDataController.wartungDateUnitComboBox.getSelectionModel().getSelectedItem().ordinal());

			data.setWartungDateIntervall(
					anlageDataController.wartungDateIntervallComboBox.getSelectionModel().getSelectedItem());

			data.setWarnungDateUnit(
					anlageDataController.warnungDateUnitComboBox.getSelectionModel().getSelectedItem().ordinal());
			data.setWartungDateWarnung(
					anlageDataController.warnungDateWarnungComboBox.getSelectionModel().getSelectedItem());
		}

		if (data.getId() == 0)
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
			data.setPanelFormat(panelFormat);
			data.setPanelFormatId(panelFormat.getId());
			Service.getInstance().insertAnlage(data);
		}

	}

	private void update() {

		Service.getInstance().updateAnlage(data);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	public Anlage getData() {
		return data;
	}

}
