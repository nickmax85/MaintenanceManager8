package com.maintenance.view.user;

import java.util.ResourceBundle;

import com.maintenance.db.service.Service;
import com.maintenance.model.User;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class UserEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private UserDataController userDataController;

	private Stage dialogStage;

	private User data;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(User data) {

		this.data = data;

		userDataController.setData(data);
		userDataController.setDialogStage(dialogStage);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!userDataController.isInputValid())
			return;

		if (data == null)
			data = new User();

		if (data != null) {
			data.setFirstName(userDataController.firstNameField.getText());
			data.setLastName(userDataController.lastNameField.getText());
			data.setMail(userDataController.mailField.getText());

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

		Service.getInstance().getUserService().insert(data);

	}

	private void update() {

		Service.getInstance().getUserService().update(data);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
