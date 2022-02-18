package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class InputValidatorAlert extends Alert {

	public InputValidatorAlert(Stage dialogStage, String message) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);
		setTitle("Daten unvollst�ndig");
		setHeaderText("Es wurden nicht alle ben�tigten Daten eingegeben.");

		if (message.length() == 0) {
			setContentText("Bitte geben Sie alle Daten ein.");
		} else {
			setContentText(message);
		}
	}

}
