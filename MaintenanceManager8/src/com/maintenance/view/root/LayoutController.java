package com.maintenance.view.root;

import java.awt.Desktop;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.util.Constants;
import com.maintenance.view.alert.NotImplementedYetAlert;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.CheckMenuItem;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class LayoutController implements Initializable {

	private static final Logger logger = Logger.getLogger(LayoutController.class);

	public static boolean dragResize;

	@FXML
	private ResourceBundle resources;
	@FXML
	private CheckMenuItem dragResizeCheckMenuItem;

	private Main main;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

		dragResizeCheckMenuItem.setOnAction(new EventHandler<ActionEvent>() {

			@Override
			public void handle(ActionEvent event) {

				if (LoginDialog.isLoggedIn(main.getPrimaryStage())) {

					if (dragResizeCheckMenuItem.isSelected())
						dragResize = true;
					else
						dragResize = false;

				}

			}
		});

	}

	public void setMain(Main main) {
		this.main = main;

	}

	@FXML
	private void handleSettings() {

		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showSettingsDialog();

	}

	@FXML
	private void handleTreeView() {
		main.showTreeViewOverviewDialog();
	}

	@FXML
	private void handleAnlagen() {
		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showAnlagenOverviewDialog();
	}

	@FXML
	private void handleLeerFlaechen() {

		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showLeerflaechenOverviewDialog();

	}

	@FXML
	private void handleAbteilungen() {
		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showAbteilungenOverviewDialog();
	}

	@FXML
	private void handleMailVerteiler() {

	}

	@FXML
	private void handleUser() {
		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showUserOverviewDialog();
	}

	@FXML
	private void handleAnlageUser() {

		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showAnlageUserOverviewDialog();

	}

	@FXML
	private void handleAllgemeineAnhaenge() {

		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showAllgemeineAnhaenge();

	}

	@FXML
	private void handleStueckzahlenImport() {

		if (LoginDialog.isLoggedIn(main.getPrimaryStage()))
			main.showMESAnlagenOverview();

	}

	@FXML
	private void handleNextWartungenBarChart() {

		new NotImplementedYetAlert(main.getPrimaryStage()).showAndWait();

	}

	@FXML
	private void handleReportWartungen() {

		main.showReportWartungenDialog();

	}

	@FXML
	private void handleUpdateMenuItem() {

		logger.info("handleUpdateMenuItem");
		main.updateRootLayout();

	}

	@FXML
	private void handleUpdateKeyEvent(KeyEvent event) {

		if (event.getCode() == KeyCode.F5) {
			logger.info("handleUpdateKeyEvent: " + event);
			main.updateRootLayout();

		}

	}

	@FXML
	private void handleLegende() {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/root/Legende.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			Stage dialogStage = new Stage();
			dialogStage.getIcons().addAll(main.getPrimaryStage().getIcons());
			dialogStage.centerOnScreen();
			dialogStage.initModality(Modality.APPLICATION_MODAL);
			dialogStage.setTitle("Legende");
			dialogStage.initOwner(main.getPrimaryStage());

			Scene scene = new Scene(page);
			scene.getStylesheets().add(Constants.STYLESHEET);
			dialogStage.setScene(scene);

			LegendeController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			dialogStage.showAndWait();

		} catch (IOException e) {
			e.printStackTrace();

		}

	}

	@FXML
	private void handleHilfeStueckzahlenImport() {

//		if (Desktop.isDesktopSupported()) {
//			try {
//				URL url = getClass().getResource("../../../../Stueckzahlimport.pdf");
//				File file = new File(url.getPath());
//				Desktop.getDesktop().open(file);
//			} catch (IOException ex) {
//				// no application registered for PDFs
//			}
//		}
		
		 if (Desktop.isDesktopSupported())   
	        {   
	            InputStream jarPdf = getClass().getClassLoader().getResourceAsStream("com/maintenance/resource/images/Stueckzahlimport.pdf");

	            try {
	                File pdfTemp = new File("temp.pdf");
	              
	                FileOutputStream fos = new FileOutputStream(pdfTemp);
	                while (jarPdf.available() > 0) {
	                      fos.write(jarPdf.read());
	                }  
	                fos.close();
	               
	                Desktop.getDesktop().open(pdfTemp);
	            }   

	            catch (IOException e) {
	                System.out.println("erreur : " + e);
	            }  
	        }

	}

	@FXML
	private void handleAbout() {

		StringBuilder sb = new StringBuilder();

		sb.append("Version: " + Main.VERSION_HAUPT + "." + Main.VERSION_NEBEN + "." + Main.REVISION + "\n\n");
		sb.append("Entwicklung mit JDK: " + Main.JDK + "\n");
		sb.append("Java: " + System.getProperty("java.version") + "\n");
		sb.append("JavaFX: " + System.getProperty("javafx.version"));

		Alert alert = new Alert(AlertType.INFORMATION);
		alert.setTitle(resources.getString("about"));
		alert.setHeaderText(resources.getString("appname") + "\n" + sb.toString().replace("$", ""));
		alert.initOwner(main.getPrimaryStage());

		alert.setContentText("Entwicklung:\n" + resources.getString("programer"));

		DialogPane dialogPane = alert.getDialogPane();
		dialogPane.getStylesheets().addAll(Constants.STYLESHEET);

		alert.showAndWait();
	}

	@FXML
	private void handleExit() {
		System.exit(0);
	}

}