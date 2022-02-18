package com.maintenance.view.mesanlage;

import java.util.ResourceBundle;

import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class MESAnlageEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private MESAnlageDataController mesAnlageDataController;

	private Stage dialogStage;

	private MESAnlage data;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(MESAnlage data) {

		this.data = data;

		mesAnlageDataController.setData(data);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!mesAnlageDataController.isInputValid())
			return;

		if (data == null)
			data = new MESAnlage();

		if (data != null) {
			if (!mesAnlageDataController.anlageComboBox.getSelectionModel().isEmpty()) {
				data.setAnlage(mesAnlageDataController.anlageComboBox.getValue());
				data.setAnlageId(mesAnlageDataController.anlageComboBox.getValue().getId());
			}

			if (!mesAnlageDataController.anlage2ComboBox.getSelectionModel().isEmpty()) {
				data.setAnlage2(mesAnlageDataController.anlage2ComboBox.getValue());
				data.setAnlage2Id(mesAnlageDataController.anlage2ComboBox.getValue().getId());
			}

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

		Service.getInstance().insertMESAnlage(data);

	}

	private void update() {

		Service.getInstance().updateMESAnlage(data);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
