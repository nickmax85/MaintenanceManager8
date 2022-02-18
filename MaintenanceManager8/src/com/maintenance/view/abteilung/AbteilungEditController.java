package com.maintenance.view.abteilung;

import java.util.ResourceBundle;

import com.maintenance.db.dto.Abteilung;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AbteilungEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private AbteilungDataController abteilungDataController;

	private Stage dialogStage;

	private Abteilung abteilung;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Abteilung abteilung) {

		this.abteilung = abteilung;

		abteilungDataController.setData(abteilung);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!abteilungDataController.isInputValid())
			return;

		if (abteilung == null)
			abteilung = new Abteilung();

		if (abteilung != null) {
			abteilung.setName(abteilungDataController.nameField.getText());

		}

		if (abteilung.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			dialogStage.close();
		}

	}

	private void insert() {

		Service.getInstance().insertAbteilung(abteilung);

	}

	private void update() {

		Service.getInstance().updateAbteilung(abteilung);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
