package com.maintenance.view.tree;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.Main;
import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Leerflaeche;
import com.maintenance.db.dto.Station;
import com.maintenance.db.service.Service;
import com.maintenance.view.anlage.AnlagePanelController;
import com.maintenance.view.chart.NextWartungenBarChartController;
import com.maintenance.view.leerflaeche.LeerflaechePanelController;
import com.maintenance.view.wartung.WartungenOverviewController;

import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.Label;
import javafx.scene.control.MenuItem;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SplitPane;
import javafx.scene.control.TreeCell;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * 
 * @author Markus Thaler
 */
public class TreeViewController implements Initializable {

	private static final Logger logger = Logger.getLogger(TreeViewController.class);

	private Main main;
	private ResourceBundle resources;
	private Stage dialogStage;

	@FXML
	private SplitPane splitPane;
	@FXML
	private TreeView<Object> treeView;
	@FXML
	private BorderPane dataPane;
	@FXML
	private Label headerLabel;

	private ContextMenu contextMenu = new ContextMenu();
	private MenuItem info = new MenuItem();

	public abstract class AbstractTreeItem extends TreeItem {
		public abstract ContextMenu getMenu();
	}

	public class ProviderTreeItem extends AbstractTreeItem {
		// make class vars here like psswd
		public ProviderTreeItem(String name) {
			this.setValue(name);
		}

		@Override
		public ContextMenu getMenu() {
			MenuItem addInbox = new MenuItem("add inbox");
			addInbox.setOnAction(new EventHandler() {
				public void handle(Event t) {
					BoxTreeItem newBox = new BoxTreeItem("inbox");
					getChildren().add(newBox);
				}
			});
			return new ContextMenu(addInbox);
		}
	}

	public class BoxTreeItem extends AbstractTreeItem {
		// private List<String> emails = new LinkedList<>();
		public BoxTreeItem(String name) {
			this.setValue(name);
		}

		@Override
		public ContextMenu getMenu() {
			return new ContextMenu(new MenuItem("test"));
		}
	}

	private final class TreeCellImpl extends TreeCell<String> {

		@Override
		public void updateItem(String item, boolean empty) {
			super.updateItem(item, empty);

			if (empty) {
				setText(null);
				setGraphic(null);
			} else {
				setText(getItem() == null ? "" : getItem().toString());
				setGraphic(getTreeItem().getGraphic());
				setContextMenu(((AbstractTreeItem) getTreeItem()).getMenu());
			}
		}
	}

	@Override
	public void initialize(URL location, ResourceBundle resources) {

		this.resources = resources;

		createObjectTree();

		headerLabel.setText(resources.getString("appname"));

		dataPane.setCenter(new ImageView(new Image(getClass().getClassLoader()
				.getResourceAsStream("com/maintenance/resource/icons/maintenanceImage.jpg"))));

	}

	private class TestTreeCellFactory implements Callback<TreeView<Object>, TreeCell<Object>> {
		@Override
		public TreeCell<Object> call(TreeView<Object> treeView) {
			return new TreeCell<>();

		}

		public void mousePressed(MouseEvent event, TreeView<String> treeView) {
			if (event.getButton().equals(MouseButton.SECONDARY)) {
				System.out.println("Right Mouse Button Clicked!");
			}
		}
	}

	private void createObjectTree() {

		// Root
		TreeItem<Object> root = new TreeItem<>("Wartungen");
		root.setGraphic(new ImageView(new Image(
				getClass().getClassLoader().getResourceAsStream("com/maintenance/resource/icons/maintenance24.png"))));
		root.setExpanded(true);

		// Wartungsvorschau
//		TreeItem<Object> itemWartungVorschau = new TreeItem<>("Wartungsvorschau");
//		itemWartungVorschau.setGraphic(new ImageView(new Image(
//				getClass().getClassLoader().getResourceAsStream("com/maintenance/resource/icons/next24.png"))));
//		itemWartungVorschau.setExpanded(false);

		//root.getChildren().add(itemWartungVorschau);

		// Anlagen
		TreeItem<Object> itemAnlagen = new TreeItem<>("Anlagen");
		itemAnlagen.setGraphic(new ImageView(new Image(
				getClass().getClassLoader().getResourceAsStream("com/maintenance/resource/icons/anlage24.png"))));
		itemAnlagen.setExpanded(false);

		for (Anlage anlage : Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat()) {
			TreeItem<Object> itemAnlage = new TreeItem<Object>(anlage);

			for (Station station : Service.getInstance().getStationenFromAnlage(anlage)) {

				TreeItem<Object> itemStation = new TreeItem<Object>(station);
				itemAnlage.getChildren().add(itemStation);
			}
			itemAnlagen.getChildren().add(itemAnlage);
		}

		root.getChildren().add(itemAnlagen);

		treeView.setRoot(root);

		EventHandler<MouseEvent> mouseEvent = new EventHandler<MouseEvent>() {

			@Override
			public void handle(MouseEvent event) {

				if (event.getButton() == MouseButton.PRIMARY) {

					TreeItem<Object> selectedItem = treeView.getSelectionModel().getSelectedItem();

					if (selectedItem != null) {

						if (selectedItem.getValue() instanceof String) {

							if (selectedItem.getValue().toString().equalsIgnoreCase(resources.getString("appname"))) {
								headerLabel.setText("Wartungen");
								dataPane.setCenter(new ImageView(new Image(getClass().getClassLoader()
										.getResourceAsStream("com/maintenance/resource/icons/maintenanceImage.jpg"))));
							}

							if (selectedItem.getValue().toString().equalsIgnoreCase("Anlagen")) {
								headerLabel.setText("Anlagen");
								dataPane.setCenter(getHalleOverviewPane());
							}

							if (selectedItem.getValue().toString().equalsIgnoreCase("Wartungsvorschau")) {
								headerLabel.setText("Wartungsvorschau");
								showWartungsVorschau(null, null);
							}

						}

						if (selectedItem.getValue() instanceof Anlage) {

							Anlage data = (Anlage) selectedItem.getValue();
							headerLabel.setText(data.getName());

							showWartungen(data, null);

						}

						if (selectedItem.getValue() instanceof Station) {
							Station data = (Station) selectedItem.getValue();
							headerLabel.setText(data.getName());

							showWartungen(null, data);
						}

					}

				}

			}
		};

		EventHandler<KeyEvent> keyEvent = new EventHandler<KeyEvent>() {

			@Override
			public void handle(KeyEvent event) {

				if (event.getCode() == KeyCode.UP || event.getCode() == KeyCode.DOWN) {

					TreeItem<Object> selectedItem = treeView.getSelectionModel().getSelectedItem();

					if (selectedItem.getValue() instanceof String) {

						if (selectedItem.getValue().toString().equalsIgnoreCase(resources.getString("appname"))) {
							headerLabel.setText(resources.getString("appname"));
							dataPane.setCenter(new ImageView(new Image(getClass().getClassLoader()
									.getResourceAsStream("com/maintenance/resource/icons/maintenanceImage.jpg"))));
						}

						if (selectedItem.getValue().toString().equalsIgnoreCase("Anlagen")) {
							headerLabel.setText("Anlagen");
							dataPane.setCenter(getHalleOverviewPane());
						}

						if (selectedItem.getValue().toString().equalsIgnoreCase("Wartungsvorschau")) {
							headerLabel.setText("Wartungsvorschau");
							dataPane.setCenter(null);
						}

					}

					if (selectedItem.getValue() instanceof Anlage) {
						Anlage data = (Anlage) selectedItem.getValue();
						headerLabel.setText(data.getName());
						showWartungen(data, null);

					}

					if (selectedItem.getValue() instanceof Station) {
						Station data = (Station) selectedItem.getValue();
						headerLabel.setText(data.getName());
						showWartungen(null, data);
					}

				}

			}
		};

		treeView.addEventHandler(MouseEvent.MOUSE_CLICKED, mouseEvent);
		treeView.addEventFilter(KeyEvent.KEY_RELEASED, keyEvent);

	}

	private void showWartungsVorschau(Anlage anlage, Station station) {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/chart/NextWartungenBarChart.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			NextWartungenBarChartController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			controller.setData();

			dataPane.setCenter(page);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private void showWartungen(Anlage anlage, Station station) {

		try {

			FXMLLoader loader = new FXMLLoader();
			loader.setLocation(Main.class.getResource("view/wartung/WartungenOverview.fxml"));
			loader.setResources(resources);
			AnchorPane page = (AnchorPane) loader.load();

			WartungenOverviewController controller = loader.getController();
			controller.setDialogStage(dialogStage);

			if (anlage != null)
				controller.setData(anlage);

			if (station != null)
				controller.setData(station);

			dataPane.setCenter(page);

		} catch (IOException e) {
			e.printStackTrace();

		}
	}

	private ScrollPane getHalleOverviewPane() {

		ScrollPane sp = new ScrollPane();
		AnchorPane overviewPane = new AnchorPane();

		try {

			for (Anlage anlage : Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat()) {

				if (anlage.isStatus()) {

					FXMLLoader loader = new FXMLLoader();
					loader.setLocation(Main.class.getResource("view/anlage/AnlagePanel.fxml"));
					loader.setResources(resources);

					AnchorPane pane = (AnchorPane) loader.load();
					pane.setUserData(anlage);

					overviewPane.getChildren().add(pane);

					AnlagePanelController controller = loader.getController();
					controller.setMain(main);
					controller.setDialogStage(this.dialogStage);
					controller.setGraphicComponents();

				}

			}

			for (Leerflaeche leerflaeche : Service.getInstance().getAllLeerflaechePanelFormat()) {

				FXMLLoader loader = new FXMLLoader();
				loader.setLocation(Main.class.getResource("view/leerflaeche/LeerflaechePanel.fxml"));
				loader.setResources(resources);

				AnchorPane pane = (AnchorPane) loader.load();
				pane.setUserData(leerflaeche);

				overviewPane.getChildren().add(pane);

				LeerflaechePanelController controller = loader.getController();
				controller.setMain(main);
				controller.setDialogStage(this.dialogStage);
				controller.setGraphicComponents();

			}

		} catch (IOException e) {
			e.printStackTrace();
		}

		sp.setContent(overviewPane);

		return sp;

	}

	@FXML
	private void handleClose() {
		this.dialogStage.close();

	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;
	}

	public void setMain(Main main) {
		this.main = main;

	}

}
