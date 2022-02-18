package com.maintenance.view.root;

import java.awt.Desktop;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import com.maintenance.util.ApplicationProperties;
import com.maintenance.util.Constants;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

/**
 * 
 * 
 * @author Markus Thaler
 */
public class SettingsController implements Initializable {

	@FXML
	private TextField hostField;
	@FXML
	private TextField dbModelField;
	@FXML
	private TextField portField;
	@FXML
	private TextField timeoutField;

	private Stage dialogStage;

	private boolean okClicked = false;
	private ResourceBundle resources;

	@Override
	public void initialize(URL location, ResourceBundle resources) {
		this.resources = resources;

	}

	/**
	 * Sets the stage of this dialog.
	 * 
	 * @param dialogStage
	 */
	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
		setData();
	}

	public void setData() {

		hostField.setText(ApplicationProperties.getInstance().getProperty("db_host"));
		dbModelField.setText(ApplicationProperties.getInstance().getProperty("db_model"));
		portField.setText(ApplicationProperties.getInstance().getProperty("db_port"));
		timeoutField.setText(ApplicationProperties.getInstance().getProperty("db_timeout"));

	}

	/**
	 * Returns true if the user clicked OK, false otherwise.
	 * 
	 * @return
	 */
	public boolean isOkClicked() {
		return okClicked;
	}

	/**
	 * Called when the user clicks ok.
	 */
	@FXML
	private void handleOk() {
		if (isInputValid()) {

			ApplicationProperties.getInstance().edit("db_host", hostField.getText());
			ApplicationProperties.getInstance().edit("db_model", dbModelField.getText());
			ApplicationProperties.getInstance().edit("db_port", portField.getText());
			ApplicationProperties.getInstance().edit("db_timeout", timeoutField.getText());
			ApplicationProperties.getInstance().save();

			okClicked = true;

			Alert alert = new Alert(AlertType.INFORMATION);
			alert.setTitle("Hinweis");
			alert.setHeaderText("�nderungen werden erst nach Neustart vorgenommen");
			alert.setContentText("Wenn Sie �nderungen vorgenommen haben, starten Sie das Programm bitte neu.");
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			alert.showAndWait();

			dialogStage.close();

		}

	}

	/**
	 * Called when the user clicks cancel.
	 */
	@FXML
	private void handleSettingsLink() {

		FileChooser fileChooser = new FileChooser();
		fileChooser.setTitle("�ffne Einstellungen");
		String userHome = System.getProperty("user.home");
		fileChooser.setInitialDirectory(
				new File(userHome + File.separator + resources.getString("appname") + File.separator));
		fileChooser.getExtensionFilters().addAll(new ExtensionFilter("Property Files", "*.properties"));

		File selectedFile = fileChooser.showOpenDialog(dialogStage);
		try {
			if (selectedFile != null)
				Desktop.getDesktop().open(selectedFile);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

	/**
	 * Validates the user input in the text fields.
	 * 
	 * @return true if the input is valid
	 */
	private boolean isInputValid() {
		String errorMessage = "";

		if (hostField.getText() == null || hostField.getText().length() == 0) {
			errorMessage += "Ung�ltige Eingabe des Host!\n";
		}

		if (dbModelField.getText() == null || dbModelField.getText().length() == 0) {
			errorMessage += "Ung�ltige Eingabe der Datenbank!\n";
		}

		if (portField.getText() == null || portField.getText().length() == 0) {
			errorMessage += "Ung�ltige Eingabe der Portnummer!\n";
		}

		if (timeoutField.getText() == null || timeoutField.getText().length() == 0) {
			errorMessage += "Ung�ltige Eingabe des Timeout!\n";
		}

		if (errorMessage.length() == 0) {
			return true;
		} else {

			Alert alert = new Alert(AlertType.ERROR);
			alert.setTitle("Error");
			alert.setHeaderText("Fehlerhafte Eingabe!");
			alert.setContentText(errorMessage);
			DialogPane dialogPane = alert.getDialogPane();
			dialogPane.getStylesheets().addAll(getClass().getResource(Constants.STYLESHEET).toExternalForm());
			alert.show();
			return false;
		}
	}

}