package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class InfoAlert extends Alert {

	public InfoAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Daten unvollständig");
		setHeaderText("Es wurden nicht alle benötigten Daten eingegeben.");
		setContentText("Bitte geben Sie alle Daten ein.");
	}

}
