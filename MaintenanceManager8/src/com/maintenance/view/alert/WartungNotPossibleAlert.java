package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class WartungNotPossibleAlert extends Alert {

	public WartungNotPossibleAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Wartung nicht möglich");
		setHeaderText("Es ist bereits eine Wartung eröffnet.");
		setContentText("Bitte schließen Sie die die eröffnete Wartung zuerst ab.");
	}

}
