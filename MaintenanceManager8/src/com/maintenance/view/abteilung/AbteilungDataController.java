package com.maintenance.view.abteilung;

import java.util.ResourceBundle;

import com.maintenance.db.dto.Abteilung;
import com.maintenance.db.service.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class AbteilungDataController {

	@FXML
	private ResourceBundle resources;

	@FXML
	public TextField nameField;

	private Abteilung abteilung;

	@FXML
	private void initialize() {

	}

	public void setData(Abteilung data) {

		this.abteilung = data;

		ObservableList<Abteilung> abteilungen = FXCollections.observableArrayList();
		abteilungen.setAll(Service.getInstance().getAllAbteilungen());

		if (data != null) {
			nameField.setText(data.getName());

		} else {
			nameField.setText("");

		}

	}

	public void setEditable(boolean editable) {

		nameField.setDisable(!editable);

	}

	public boolean isInputValid() {

		if (nameField.getText().isEmpty())
			return false;

		return true;

	}

}
