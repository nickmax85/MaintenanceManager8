package com.maintenance.view.root;

import java.util.Optional;

import com.maintenance.Main;
import com.maintenance.util.Constants;

import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.scene.image.ImageView;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;

public class LoginDialog {

	public static boolean loggedIn;
	private static String adminNames;

	public static boolean isLoggedIn(Stage stage) {

		if (!loggedIn)
			login(stage);

		return loggedIn;
	}

	public static boolean login(Stage stage) {

		// Superuser
		// if (System.getProperty("user.name").contains("steinb") ||
		// System.getProperty("user.name").contains("potz")) {
		// loggedIn = true;
		// }

		if (loggedIn)
			return true;

		Dialog<Pair<String, String>> dialog = new Dialog<>();
		dialog.initOwner(stage);
		dialog.setTitle("Anmelden");
		dialog.setHeaderText("Anmeldung");

		DialogPane dialogPane = dialog.getDialogPane();
		dialogPane.getStylesheets().addAll(Main.class.getResource(Constants.STYLESHEET).toExternalForm());

		dialog.setGraphic(
				new ImageView(Main.class.getResource("/com/maintenance/resource/icons/login64.png").toExternalForm()));

		ButtonType loginButtonType = new ButtonType("Login", ButtonData.OK_DONE);
		dialog.getDialogPane().getButtonTypes().addAll(loginButtonType, ButtonType.CANCEL);

		// Create the username and password labels and fields.
		GridPane grid = new GridPane();
		grid.setHgap(10);
		grid.setVgap(10);
		grid.setPadding(new Insets(20, 150, 10, 10));

		TextField username = new TextField();
		username.setPromptText("Username");
		username.setText("admin");
		PasswordField password = new PasswordField();
		password.setPromptText("Password");

		grid.add(new Label("Username:"), 0, 0);
		grid.add(username, 1, 0);
		grid.add(new Label("Password:"), 0, 1);
		grid.add(password, 1, 1);

		Node loginButton = dialog.getDialogPane().lookupButton(loginButtonType);
		loginButton.setDisable(false);

		username.textProperty().addListener((observable, oldValue, newValue) -> {
			loginButton.setDisable(newValue.trim().isEmpty());
		});

		dialog.getDialogPane().setContent(grid);

		Platform.runLater(() -> password.requestFocus());

		dialog.setResultConverter(dialogButton -> {
			if (dialogButton == loginButtonType) {
				return new Pair<>(username.getText(), password.getText());
			}
			return null;
		});

		Optional<Pair<String, String>> result = dialog.showAndWait();

		result.ifPresent(usernamePassword -> {
			// System.out.println("Username=" + usernamePassword.getKey() + ",
			// Password=" + usernamePassword.getValue());

			if (usernamePassword.getValue().equals("garfield")) {
				loggedIn = true;
			} else
				loggedIn = false;

		});
		return loggedIn;
	}

}
