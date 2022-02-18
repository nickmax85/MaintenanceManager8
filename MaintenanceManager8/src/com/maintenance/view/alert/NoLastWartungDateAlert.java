package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class NoLastWartungDateAlert extends Alert {

	public NoLastWartungDateAlert(Stage dialogStage) {

		super(AlertType.ERROR);

		initOwner(dialogStage);

		setTitle("Kalenderwartung");
		setHeaderText("Es ist noch kein Erstelldatum der Anlage vorhanden.");
		setContentText("Bitte geben Sie ein Erstelldatum ein,\n von welchem die Wartungen berechnet werden sollen.");
	}

}
