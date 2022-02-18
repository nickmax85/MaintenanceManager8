package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class GeneralInfoAlert extends Alert {

	public GeneralInfoAlert(Stage dialogStage, String title, String header, String content) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle(title);
		setHeaderText(header);
		setContentText(content);
	}

}
