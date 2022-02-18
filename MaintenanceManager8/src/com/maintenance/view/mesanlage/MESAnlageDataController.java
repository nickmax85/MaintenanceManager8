package com.maintenance.view.mesanlage;

import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.MESAnlage;
import com.maintenance.db.service.Service;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.util.StringConverter;

public class MESAnlageDataController {

	@FXML
	private ResourceBundle resources;

	// @FXML
	// public TextField nameField;
	@FXML
	public ComboBox<Anlage> anlageComboBox;
	@FXML
	public ComboBox<Anlage> anlage2ComboBox;

	private MESAnlage data;

	@FXML
	private void initialize() {

	}

	public void setData(MESAnlage data) {

		this.data = data;

		ObservableList<Anlage> anlagen = FXCollections.observableArrayList();
		anlagen.setAll(Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat());

		Anlage anl = new Anlage();
		anl.setId(0);
		anl.setName("keine Auswahl");
		anlagen.add(0, anl);

		anlageComboBox.setItems(anlagen);
		anlageComboBox.setConverter(new StringConverter<Anlage>() {

			@Override
			public Anlage fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Anlage object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		});
		anlage2ComboBox.setItems(anlagen);
		anlage2ComboBox.setConverter(new StringConverter<Anlage>() {

			@Override
			public Anlage fromString(String string) {
				// TODO Auto-generated method stub
				return null;
			}

			@Override
			public String toString(Anlage object) {
				// TODO Auto-generated method stub
				return object.getName();
			}
		});

		if (data != null) {

			if (data.getAnlageId() != 0)
				for (Anlage anlage : anlagen) {
					if (data.getAnlageId() == anlage.getId())
						anlageComboBox.getSelectionModel().select(anlage);
				}

			if (data.getAnlage2Id() != 0)
				for (Anlage anlage : anlagen) {
					if (data.getAnlage2Id() == anlage.getId())
						anlage2ComboBox.getSelectionModel().select(anlage);
				}
		}

	}

	public void setEditable(boolean editable) {

	}

	public boolean isInputValid() {

		// if (nameField.getText().isEmpty())
		// return false;

		return true;

	}

}
