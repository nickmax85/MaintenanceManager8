package com.maintenance.view.leerflaeche;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

public class LeerflaechePanelController {

	private static final Logger logger = Logger.getLogger(LeerflaechePanelController.class);

	private Stage dialogStage;
	@FXML
	private ResourceBundle resources;

	@FXML
	private URL location;

	@FXML
	private Label nameLabel;

	@FXML
	private AnchorPane pane;

	private ContextMenu contextMenu = new ContextMenu();
	private MenuItem info = new MenuItem();
	private MenuItem settings = new MenuItem();

	private Main main;

	private Leerflaeche leerflaeche;

	@FXML
	public void initialize() {

		EventHandler<MouseEvent> ev = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getButton() == MouseButton.SECONDARY)
					contextMenu.show(pane, event.getScreenX(), event.getScreenY());

			}
		};
		pane.addEventHandler(MouseEvent.MOUSE_CLICKED, ev);

		ImageView infoImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/info48.png")));
		infoImage.setFitWidth(24);
		infoImage.setFitHeight(24);
		info.setGraphic(infoImage);
		info.setText("Info");
		info.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {

				String message = "Id: " + leerflaeche.getId() + "\nName: " + leerflaeche.getName() + "\n\nModifiedBy: "
						+ leerflaeche.getUser() + "\nTimestamp: " + leerflaeche.getTimestampSql();

				Alert alert = new Alert(AlertType.INFORMATION);
				alert.setTitle("Info");
				alert.setHeaderText("Info");
				alert.setContentText(message);

				Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
				stage.initOwner(dialogStage);
				stage.getIcons().add(new Image(Constants.APP_ICON));

				DialogPane dialogPane = alert.getDialogPane();
				dialogPane.getStylesheets().addAll(Main.class.getResource(Constants.STYLESHEET).toExternalForm());
				alert.showAndWait();

			}
		});

		ImageView settingsImage = new ImageView(
				new Image(Main.class.getResourceAsStream("/com/maintenance/resource/icons/config24.png")));
		settingsImage.setFitWidth(24);
		settingsImage.setFitHeight(24);

		settings.setGraphic(settingsImage);
		settings.setText("Konfiguration");
		settings.setOnAction(new EventHandler<ActionEvent>() {
			@Override
			public void handle(ActionEvent event) {
				main.showLeerflaecheEditDialog(leerflaeche);
			}
		});

		contextMenu.getItems().addAll(settings, info);

	}

	public void setMain(Main main) {

		this.main = main;

	}

	public void initData(Leerflaeche leerflaeche) {

		this.leerflaeche = leerflaeche;

		getDataFromDatabase();
		setGraphicComponents();
	}

	public void setGraphicComponents() {

		// if (leerflaeche.getName().equalsIgnoreCase("instandhaltung"))
		// logger.error(leerflaeche.getName() + ": " + leerflaeche.hashCode());

		nameLabel.setText(leerflaeche.getName());

		pane.setId("pane-grey");
		pane.setLayoutX(leerflaeche.getPanelFormat().getX());
		pane.setLayoutY(leerflaeche.getPanelFormat().getY());
		pane.setPrefSize(leerflaeche.getPanelFormat().getWidth(), leerflaeche.getPanelFormat().getHeigth());

	}

	public void getDataFromDatabase() {

		if (!Service.getInstance().isErrorStatus()) {
			leerflaeche = Service.getInstance().getLeerflaeche(leerflaeche);
		}
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
