package com.maintenance.view.anlageuser;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.maintenance.db.service.Service;
import com.maintenance.model.Anlage;
import com.maintenance.model.User;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class AnlageUserEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private AnlageUserDataController anlageUserDataController;

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

		anlageUserDataController.setData(data);
		anlageUserDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!anlageUserDataController.isInputValid())
			return;

		if (data == null)
			data = new Anlage();

		if (data != null) {

			List<User> mitarbeiter = new ArrayList<>();
			for (User h : anlageUserDataController.table.getItems()) {
				if (h.isActive())
					mitarbeiter.add(h);

			}

			data.setUsers(mitarbeiter);

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

		Service.getInstance().getAnlageService().insert(data);

	}

	private void update() {

		Service.getInstance().getAnlageService().update(data);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
