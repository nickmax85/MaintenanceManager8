package com.maintenance.view.anlageuser;

import java.util.ResourceBundle;

import com.maintenance.db.service.Service;
import com.maintenance.model.Anlage;
import com.maintenance.model.User;
import com.maintenance.view.alert.InputValidatorAlert;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.CheckBoxTableCell;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class AnlageUserDataController {

	@FXML
	private ResourceBundle resources;

	private Stage dialogStage;

	@FXML
	private Label nameLabel;

	@FXML
	public TableView<User> table;
	@FXML
	private TableColumn<User, Boolean> auswahlColumn;
	@FXML
	private TableColumn<User, String> firstNameColumn;
	@FXML
	private TableColumn<User, String> lastNameColumn;
	@FXML
	private TableColumn<User, String> mailColumn;

	private Anlage data;

	@FXML
	private void initialize() {

		clearFields();

	}

	public void setData(Anlage data) {

		this.data = data;

		if (data != null) {

			nameLabel.setText(data.getName());

			ObservableList<User> mitarbeiterAll = FXCollections
					.observableArrayList(Service.getInstance().getUserService().findAll());

			if (data.getUsers() != null)
				for (User h : mitarbeiterAll) {
					for (User h1 : data.getUsers()) {
						if (h.getId() == h1.getId()) {
							h.setActive(true);

						}

					}

				}

			auswahlColumn.setCellValueFactory(new PropertyValueFactory<User, Boolean>("active"));
			auswahlColumn.setCellValueFactory(new PropertyValueFactory<>("active"));
			auswahlColumn.setCellFactory(CheckBoxTableCell.forTableColumn(auswahlColumn));
			auswahlColumn.setEditable(true);
			auswahlColumn.setSortType(TableColumn.SortType.DESCENDING);

			firstNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("firstName"));
			firstNameColumn.setCellFactory(column -> {
				return new TableCell<User, String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
							setStyle("");
						} else {
							setText(item);

						}
					}

				};

			});
			
			lastNameColumn.setCellValueFactory(new PropertyValueFactory<User, String>("lastName"));
			lastNameColumn.setCellFactory(column -> {
				return new TableCell<User, String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
							setStyle("");
						} else {
							setText(item);

						}
					}

				};

			});

			mailColumn.setCellValueFactory(new PropertyValueFactory<User, String>("mail"));
			mailColumn.setCellFactory(column -> {
				return new TableCell<User, String>() {

					@Override
					protected void updateItem(String item, boolean empty) {
						super.updateItem(item, empty);

						if (item == null || empty) {
							setText(null);
							setStyle("");
						} else {
							setText(item);

						}
					}

				};

			});

			table.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
			table.setEditable(true);
			table.setItems(mitarbeiterAll);
			table.getSortOrder().add(auswahlColumn);
		

		} else {
			clearFields();
		}

	}

	private void clearFields() {

		nameLabel.setText("");
		table.setItems(null);

	}

	public void setEditable(boolean editable) {

		table.setDisable(!editable);

	}

	public boolean isInputValid() {

		String text = "";

		if (text.length() == 0) {
			return true;

		} else {
			new InputValidatorAlert(dialogStage, text).showAndWait();
			return false;
		}

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}
