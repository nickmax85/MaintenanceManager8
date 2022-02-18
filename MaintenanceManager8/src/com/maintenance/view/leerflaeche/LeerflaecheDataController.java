package com.maintenance.view.leerflaeche;

import java.util.ResourceBundle;

import com.maintenance.db.dto.Leerflaeche;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class LeerflaecheDataController {

	@FXML
	private ResourceBundle resources;

	@FXML
	public TextField nameField;

	private Leerflaeche leerflaeche;

	@FXML
	private void initialize() {

	}

	public void setData(Leerflaeche data) {

		this.leerflaeche = data;

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
