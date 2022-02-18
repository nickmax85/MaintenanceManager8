package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class InfoAlert extends Alert {

	public InfoAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Daten unvollst�ndig");
		setHeaderText("Es wurden nicht alle ben�tigten Daten eingegeben.");
		setContentText("Bitte geben Sie alle Daten ein.");
	}

}
