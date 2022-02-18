package com.maintenance.view.root;

import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.stage.Stage;

public class MESAnlageHilfeController implements Initializable {

	private static final Logger logger = Logger.getLogger(MESAnlageHilfeController.class);

	@FXML
	private ResourceBundle resources;

	private Main main;
	private Stage dialogStage;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

}