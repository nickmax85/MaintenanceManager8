package com.maintenance.view.alert;

import javafx.scene.control.Alert;
import javafx.stage.Stage;

public class MESAnlageImportAlert extends Alert {

	public MESAnlageImportAlert(Stage dialogStage, int countInserts, int countUpdates) {

		super(AlertType.INFORMATION);

		initOwner(dialogStage);

		setTitle("Stückzahlenimport");
		setHeaderText("Bericht");
		setContentText("Aktualisiert: " + countUpdates + "\n" + "Hinzugefügt: " + countInserts);

	}

}
