package com.maintenance.view.leerflaeche;

import java.util.ResourceBundle;

import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.dto.PanelFormat;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class LeerflaecheEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private LeerflaecheDataController leerflaecheDataController;

	private Stage dialogStage;

	private Leerflaeche leerflaeche;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Leerflaeche leerflaeche) {

		this.leerflaeche = leerflaeche;

		leerflaecheDataController.setData(leerflaeche);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!leerflaecheDataController.isInputValid())
			return;

		if (leerflaeche == null)
			leerflaeche = new Leerflaeche();

		if (leerflaeche != null) {
			leerflaeche.setName(leerflaecheDataController.nameField.getText());

		}

		if (leerflaeche.getId() == 0)
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
			leerflaeche.setPanelFormat(panelFormat);
			leerflaeche.setPanelFormatId(panelFormat.getId());
			Service.getInstance().insertLeerflaeche(leerflaeche);
		}

	}

	private void update() {

		Service.getInstance().updateLeerflaeche(leerflaeche);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
