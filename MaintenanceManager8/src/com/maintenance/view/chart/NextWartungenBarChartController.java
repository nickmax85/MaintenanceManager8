package com.maintenance.view.chart;

import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;
import com.maintenance.util.ProzentCalc;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.BarChart;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.NumberAxis;
import javafx.stage.Stage;

public class NextWartungenBarChartController implements Initializable {

	private Stage dialogStage;

	private static final Logger logger = Logger.getLogger(NextWartungenBarChartController.class);

	@FXML
	private BarChart<NumberAxis, CategoryAxis> barChart;
	@FXML
	private CategoryAxis barChartXaxis;
	@FXML
	private NumberAxis barChartYaxis;

	private BarChart.Data<String, Number> barData;
	private List<BarChart.Data<String, Number>> barDataList;

	private ObservableList<Anlage> anlagen;

	@Override
	public void initialize(URL location, ResourceBundle resources) {

	}

	public void setData() {

		anlagen = FXCollections
				.observableArrayList(Service.getInstance().getAllAnlageLeerflaecheAbteilungPanelFormat());

		initBarData();

	}

	public void initBarData() {

		List<Station> stationen;
		BarChart.Series seriesData = new BarChart.Series();
		List<BarChart.Data> barDataList = new ArrayList<>();

		for (Anlage anlage : anlagen) {

			if (anlage.isStatus()) {
				if (!anlage.isSubMenu()) {

					if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {

						float prozStueck = 0;
						float prozDate = 0;
						float prozentAnlage = 0;
						float prozent;

						prozentAnlage = ProzentCalc.calcProzent(anlage);
						prozent = prozentAnlage;

						// if (anlage.getName().contains("ATC 350"))
						// System.out.println(prozent);

						stationen = Service.getInstance().getStationenFromAnlage(anlage);
						Station stationStueck = ProzentCalc.getMaxIntervallStationStueck(stationen);
						Station stationDate = ProzentCalc.getMaxIntervallStationDate(stationen);

						if (stationStueck != null) {
							if (stationStueck.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
								prozStueck = ProzentCalc.calcProzent(stationStueck);

								if (prozStueck > prozent)
									prozent = prozStueck;

							}
						}

						if (stationDate != null) {
							if (stationDate.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

								Date nextWartungDate;
								Date cal = null;

								if (stationDate.getLastWartungDate() != null)
									cal = stationDate.getLastWartungDate();
								else
									cal = stationDate.getCreateDate();

								nextWartungDate = ProzentCalc.calcNextWartungDate(cal,
										stationDate.getIntervallDateUnit(), stationDate.getWartungDateIntervall());

								prozDate = ProzentCalc.calcProzent(cal.getTime(), nextWartungDate.getTime());

								if (prozDate > prozent)
									prozent = prozDate;

							}
						}

						BarChart.Data bData = new BarChart.Data(anlage.getName(), prozent);
						barDataList.add(bData);

					}

					if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

						Date nextWartungDate;
						float prozDate = 0;
						float prozentAnlage = 0;
						float prozent = 0;

						nextWartungDate = ProzentCalc.calcNextWartungDate(anlage.getLastWartungDate(),
								anlage.getIntervallDateUnit(), anlage.getWartungDateIntervall());

						prozentAnlage = ProzentCalc.calcProzent(anlage.getLastWartungDate().getTime(),
								nextWartungDate.getTime());
						prozent = prozentAnlage;

						stationen = Service.getInstance().getStationenFromAnlage(anlage);
						Station stationDate = ProzentCalc.getMaxIntervallStationDate(stationen);
						if (stationDate != null) {
							if (stationDate.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

								Date next;

								next = ProzentCalc.calcNextWartungDate(stationDate.getLastWartungDate(),
										stationDate.getIntervallDateUnit(), stationDate.getWartungDateIntervall());

								prozDate = ProzentCalc.calcProzent(stationDate.getLastWartungDate().getTime(),
										nextWartungDate.getTime());

								if (prozDate > prozent)
									prozent = prozDate;

							}
						}

						BarChart.Data bData = new BarChart.Data(anlage.getName(), prozent);
						barDataList.add(bData);

					}
				}

				if (anlage.isSubMenu()) {

					stationen = Service.getInstance().getStationenFromAnlage(anlage);

					Station stationStueck = ProzentCalc.getMaxIntervallStationStueck(stationen);
					Station stationDate = ProzentCalc.getMaxIntervallStationDate(stationen);

					float prozent;
					float prozStueck = 0;
					float prozDate = 0;

					if (stationStueck != null) {
						if (stationStueck.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())

							prozStueck = ProzentCalc.calcProzent(stationStueck);

					}

					if (stationDate != null) {
						if (stationDate.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

							Date nextWartungDate;

							Date cal = null;

							if (stationDate.getLastWartungDate() != null)
								cal = stationDate.getLastWartungDate();
							else
								cal = stationDate.getCreateDate();

							nextWartungDate = ProzentCalc.calcNextWartungDate(cal, stationDate.getIntervallDateUnit(),
									stationDate.getWartungDateIntervall());

							prozDate = ProzentCalc.calcProzent(cal.getTime(), nextWartungDate.getTime());

						}
					}

					if (prozStueck > prozDate)
						prozent = prozStueck;
					else
						prozent = prozDate;

					BarChart.Data bData = new BarChart.Data(anlage.getName(), prozent);
					barDataList.add(bData);
				}
			}
			// if (anlage.getName().contains("ATC 350"))
			// logger.info(anlage.getIntervall() * 100.0f);

		}

		seriesData.getData().addAll(barDataList);

		barChart.getData().add(seriesData);

	}

	public void setDialogStage(Stage dialogStage) {

		this.dialogStage = dialogStage;

	}

}
