package com.maintenance.view.user;

import java.io.IOException;
import java.util.ResourceBundle;

import com.maintenance.Main;
import com.maintenance.db.service.Service;
import com.maintenance.model.User;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.DeleteYesNoAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
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

public class UserOverviewController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private TableView<User> table;
	@FXML
	private TableColumn<User, String> firstNameColumn;
	@FXML
	private TableColumn<User, String> lastNameColumn;
	@FXML
	private TableColumn<User, String> mailColumn;

	@FXML
	private Label nameLabel;

	@FXML
	private UserDataController userDataController;

	private Stage dialogStage;

	@FXML
	private void initialize() {

		firstNameColumn.setCellValueFactory(cellData -> cellData.getValue().firstNameProperty());
		lastNameColumn.setCellValueFactory(cellData -> cellData.getValue().lastNameProperty());
		mailColumn.setCellValueFactory(cellData -> cellData.getValue().mailProperty());

		showDetails(null);

		table.setOnMouseClicked(new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getClickCount() == 2)
					if (event.getButton() == MouseButton.PRIMARY)
						if (!table.getSelectionModel().isEmpty())
							showEditDialog(table.getSelectionModel().getSelectedItem());

			}
		});
		table.getSelectionModel().selectedItemProperty()
				.addListener((observable, oldValue, newValue) -> showDetails(newValue));
		table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
	}

	public void setData() {

		ObservableList<User> dataFx = FXCollections
				.observableArrayList(Service.getInstance().getUserService().findAll());
		table.setItems(dataFx);

	}

	private void showDetails(User abteilung) {

		userDataController.setData(abteilung);
		userDataController.setEditable(false);
	}

	@FXML
	private void handleDelete() {
		User data;
		int selectedIndex = table.getSelectionModel().getSelectedIndex();

		if (selectedIndex >= 0) {

			DeleteYesNoAlert alert = new DeleteYesNoAlert(dialogStage);

			if (alert.isOKButton()) {
				data = table.getSelectionModel().getSelectedItem();
				Service.getInstance().getUserService().delete(data);
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

		User data = new User();

		boolean okClicked = showEditDialog(data);
		if (okClicked) {
			table.setItems(FXCollections.observableArrayList(Service.getInstance().getUserService().findAll()));
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

	private boolean showEditDialog(User data) {
		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/user/UserEdit.fxml"));

			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.initOwner(this.dialogStage);
			dialogStage.centerOnScreen();
			dialogStage.setResizable(false);
			dialogStage.getIcons().addAll(this.dialogStage.getIcons());
			dialogStage.initModality(Modality.WINDOW_MODAL);
			dialogStage.setTitle("User: Bearbeiten");

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			UserEditController controller = loader.getController();
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

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
