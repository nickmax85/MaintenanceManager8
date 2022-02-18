package com.maintenance.view.wartung;

import java.time.ZoneOffset;
import java.util.Date;
import java.util.ResourceBundle;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class WartungEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private WartungDataController wartungDataController;

	private Stage dialogStage;

	private boolean okClicked = false;

	private Wartung wartung;
	private boolean wartungClosed;

	private Anlage anlage;
	private Station station;

	@FXML
	private void initialize() {

	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(Wartung wartung) {

		this.wartung = wartung;

		wartungDataController.setData(wartung);

	}

	public void setData(Anlage anlage) {

		this.anlage = anlage;

		wartungDataController.setDialogStage(dialogStage);
		wartungDataController.setData(anlage);
		wartungDataController.setData(wartung);

	}

	public void setData(Station station) {

		this.station = station;

		wartungDataController.setDialogStage(dialogStage);
		wartungDataController.setData(station);
		wartungDataController.setData(wartung);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		CalendarWartung cal;

		if (!wartungDataController.validateFields()) {
			return;
		}

		if (wartung.getStatus() == EWartungStatus.DONE.ordinal()
				|| wartung.getStatus() == EWartungStatus.NOT_POSSIBLE.ordinal())
			wartungClosed = true;
		else
			wartungClosed = false;

		if (anlage != null) {
			wartung.setAnlageId(anlage.getId());
			wartung.setAnlage(anlage);

			// Damit eine Wartung vor dem Kalenderdatum abgeschlossen werden kann und das
			// nächste Datum verwendet wird, ist das nächstegelegene Kalenderdatum zu
			// entfernen
			if (wartungClosed) {
				if (anlage.getLastWartungDate() != null)
					cal = Service.getInstance().getNextCalendarWartung(anlage.getId(), anlage.getLastWartungDate());
				else
					cal = Service.getInstance().getNextCalendarWartung(anlage.getId(), anlage.getCreateDate());

				if (cal != null)
					Service.getInstance().deleteCalendarWartung(cal);

			}
		}

		if (station != null) {
			wartung.setStationId(station.getId());
			wartung.setStation(station);

		}

		wartung.setAuftrag(wartungDataController.auftragField.getText());

		Date date = Date.from(wartungDataController.datumField.getValue().atStartOfDay().toInstant(ZoneOffset.UTC));
		wartung.setDate(date);

		wartung.setStatus(wartungDataController.statusComboBox.getSelectionModel().getSelectedItem().ordinal());

		wartung.setMitarbeiter(wartungDataController.mitarbeiterField.getText());
		wartung.setInfo(wartungDataController.informationField.getText());

		if (wartung.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			if (dialogStage != null)
				dialogStage.close();

		}

	}

	private void insert() {

		Service.getInstance().insertWartung(wartung);
		if (!Service.getInstance().isErrorStatus()) {
			if (anlage != null) {
				if (wartung.getStatus() == EWartungStatus.DONE.ordinal()
						|| wartung.getStatus() == EWartungStatus.NOT_POSSIBLE.ordinal()) {

					if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
						wartung.setProzent(Service.getInstance().calcProzent(anlage));
						anlage.setLastWartungStueckzahl(anlage.getAktuelleStueck());

					}

					if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
						wartung.setProzent(-1);

					}
					anlage.setLastWartungDate(wartung.getDate());

					Service.getInstance().updateAnlage(anlage);

				} else
					wartung.setProzent(-1);

			}
			if (station != null) {
				if (wartung.getStatus() == EWartungStatus.DONE.ordinal()
						|| wartung.getStatus() == EWartungStatus.NOT_POSSIBLE.ordinal()) {

					if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
						wartung.setProzent(Service.getInstance().calcProzent(station));
						station.setLastWartungStueckzahl(station.getAnlage().getAktuelleStueck());

					}

					if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
						wartung.setProzent(-1);

					}
					station.setLastWartungDate(wartung.getDate());
					station.setMailSent(false);
					Service.getInstance().updateStation(station);

				} else
					wartung.setProzent(-1);

			}

			Service.getInstance().updateWartung(wartung);
		}

	}

	private void update() {

		Service.getInstance().updateWartung(wartung);
		if (!Service.getInstance().isErrorStatus()) {

			if (anlage != null) {
				if (!wartungClosed)
					if ((wartung.getStatus() == EWartungStatus.DONE.ordinal()
							|| wartung.getStatus() == EWartungStatus.NOT_POSSIBLE.ordinal())) {

						if (anlage.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
							anlage.setLastWartungStueckzahl(anlage.getAktuelleStueck());
							wartung.setProzent(Service.getInstance().calcProzent(anlage));
						}

						if (anlage.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
							wartung.setProzent(-1);
						}

						anlage.setLastWartungDate(wartung.getDate());

						Service.getInstance().updateAnlage(anlage);

					} else
						wartung.setProzent(-1);
			}
			if (station != null) {
				if (!wartungClosed)
					if ((wartung.getStatus() == EWartungStatus.DONE.ordinal()
							|| wartung.getStatus() == EWartungStatus.NOT_POSSIBLE.ordinal())) {

						if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
							station.setLastWartungStueckzahl(station.getAnlage().getAktuelleStueck());
							wartung.setProzent(Service.getInstance().calcProzent(station));
						}

						if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
							wartung.setProzent(-1);
						}

						station.setLastWartungDate(wartung.getDate());

						Service.getInstance().updateStation(station);
					} else
						wartung.setProzent(-1);
			}

			Service.getInstance().updateWartung(wartung);
		}

	}

	@FXML
	private void handleCancel() {

		okClicked = false;
		if (dialogStage != null)
			dialogStage.close();

	}

}
