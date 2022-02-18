package com.maintenance.view.leerflaeche;

import java.io.IOException;
import java.util.Optional;
import java.util.ResourceBundle;

import com.maintenance.Main;
import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.service.Service;
import com.maintenance.util.Constants;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LeerflaechenOverviewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private TableView<Leerflaeche> table;
	@FXML
	private TableColumn<Leerflaeche, String> nameColumn;

	@FXML
	private Label nameLabel;

	@FXML
	private LeerflaecheDataController leerflaecheDataController;

	@FXML
	private void initialize() {

		nameColumn.setCellValueFactory(cellData -> cellData.getValue().nameProperty());

		showDetails(null);

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2)
					if (event.getButton() == MouseButton.PRIMARY)
						showEditDialog(table.getSelectionModel().getSelectedItem());

			}
		});
		table.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDetails(newValue));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setData() {

		ObservableList<Leerflaeche> leerflaechenFX = FXCollections
				.observableArrayList(Service.getInstance().getAllLeerflaechePanelFormat());
		table.setItems(leerflaechenFX);

	}

	private void showDetails(Leerflaeche abteilung) {

		leerflaecheDataController.setEditable(false);
		leerflaecheDataController.setData(abteilung);
	}

	@FXML
	private void handleDelete() {
		Leerflaeche data;
		int selectedIndex = table.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {

			Alert alert = new Alert(AlertType.CONFIRMATION);
			alert.getDialogPane().getStyleableParent();
			alert.setTitle("MaintenanceVisualization");
			alert.setHeaderText("Entfernen");
			alert.setContentText("Wollen Sie die Daten wirklich entfernen?");

			Stage stage = (Stage) alert.getDialogPane().getScene().getWindow();
			stage.setAlwaysOnTop(true);
			stage.toFront();

			ButtonType buttonTypeOk = new ButtonType("Ja");
			ButtonType buttonTypeCancel = new ButtonType("Nein");
			alert.getButtonTypes().setAll(buttonTypeOk, buttonTypeCancel);
			Optional<ButtonType> result = alert.showAndWait();

			if (result.get() == buttonTypeOk) {
				data = table.getSelectionModel().getSelectedItem();
				Service.getInstance().deleteLeerflaeche(data);
				if (!Service.getInstance().isErrorStatus()) {
					table.getItems().remove(data);
					table.getSelectionModel().clearSelection();
					table.refresh();

				}
			}

		}

	}

	@FXML
	private void handleDeleteKeyPressed(KeyEvent event) {

		if (event.getEventType() == KeyEvent.KEY_PRESSED)
			if (event.getCode() == KeyCode.DELETE)
				handleDelete();

	}

	@FXML
	private void handleNew() {

		Leerflaeche data = new Leerflaeche();

		boolean okClicked = showEditDialog(data);
		if (okClicked) {
			table.setItems(FXCollections.observableArrayList(Service.getInstance().getAllLeerflaechePanelFormat()));
			table.refresh();
			showDetails(null);
		}
	}

	@FXML
	private void handleEdit() {

		showEditDialog(table.getSelectionModel().getSelectedItem());

	}

	@FXML
	private void handleEditMouseClick(MouseEvent event) {

		if (event.getButton() == MouseButton.PRIMARY)
			if (event.getClickCount() == 2)
				handleEdit();

	}

	@FXML
	private void handleRefresh(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			setData();
		}

	}

	private boolean showEditDialog(Leerflaeche data) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/leerflaeche/LeerflaecheEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setAlwaysOnTop(true);
			dialogStage.setTitle("Leerflaeche: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			LeerflaecheEditController controller = loader.getController();
			controller.setDialogStage(dialogStage);
			controller.setData(data);

			dialogStage.showAndWait();

			showDetails(data);

			return controller.isOkClicked();
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

}
