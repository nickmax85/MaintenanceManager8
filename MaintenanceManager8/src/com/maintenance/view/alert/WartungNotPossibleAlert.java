package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class WartungNotPossibleAlert extends Alert {

	public WartungNotPossibleAlert(Stage dialogStage) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Wartung nicht m�glich");
		setHeaderText("Es ist bereits eine Wartung er�ffnet.");
		setContentText("Bitte schlie�en Sie die die er�ffnete Wartung zuerst ab.");
	}

}
