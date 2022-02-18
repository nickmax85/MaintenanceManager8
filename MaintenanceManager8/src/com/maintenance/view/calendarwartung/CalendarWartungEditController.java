package com.maintenance.view.calendarwartung;

import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import com.maintenance.db.dto.CalendarWartung;
import com.maintenance.db.service.Service;

import javafx.fxml.FXML;
import javafx.stage.Stage;

public class CalendarWartungEditController {

	@FXML
	private ResourceBundle resources;

	@FXML
	private CalendarWartungDataController calendarWartungDataController;

	private Stage dialogStage;

	private CalendarWartung data;

	private boolean okClicked = false;

	@FXML
	private void initialize() {
	}

	public void setDialogStage(Stage dialogStage) {
		this.dialogStage = dialogStage;
	}

	public void setData(CalendarWartung data) {

		this.data = data;

		calendarWartungDataController.setData(data);

	}

	public boolean isOkClicked() {
		return okClicked;
	}

	@FXML
	private void handleOk() {

		if (!calendarWartungDataController.isInputValid())
			return;

		if (data == null)
			data = new CalendarWartung();

		if (data != null) {

			Date date = Date.from(calendarWartungDataController.calendarField.getValue()
					.atStartOfDay(ZoneId.systemDefault()).toInstant());
			data.setDate(date);
			data.setRemark(calendarWartungDataController.remarkField.getText());

		}

		if (data.getId() == 0)
			insert();
		else
			update();

		if (!Service.getInstance().isErrorStatus()) {
			okClicked = true;
			dialogStage.close();
		}

	}

	private void insert() {

		Service.getInstance().insertCalendarWartung(data);

	}

	private void update() {

		Service.getInstance().updateCalendarWartung(data);

	}

	@FXML
	private void handleCancel() {
		dialogStage.close();
	}

}
