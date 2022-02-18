package com.maintenance.view.calendarwartung;

import java.time.LocalDate;
import java.util.Calendar;
import java.util.ResourceBundle;

import com.maintenance.db.dto.CalendarWartung;

import javafx.fxml.FXML;
import javafx.scene.control.DatePicker;
import javafx.scene.control.TextArea;

public class CalendarWartungDataController {

	@FXML
	private ResourceBundle resources;

	@FXML
	public DatePicker calendarField;
	@FXML
	public TextArea remarkField;

	private CalendarWartung calendarWartung;

	@FXML
	private void initialize() {

		calendarField.setShowWeekNumbers(true);

	}

	public void setData(CalendarWartung data) {

		this.calendarWartung = data;

		if (data != null) {

			if (data.getDate() != null) {
				Calendar cal = Calendar.getInstance();
				cal.setTime(data.getDate());
				calendarField.setValue(LocalDate.of(cal.get(Calendar.YEAR), cal.get(Calendar.MONTH) + 1,
						cal.get(Calendar.DAY_OF_MONTH)));

			}

			remarkField.setText(data.getRemark());

		} else {
			calendarField.setValue(null);
			remarkField.setText("");

		}

	}

	public void setEditable(boolean editable) {

		calendarField.setDisable(!editable);
		remarkField.setDisable(!editable);

	}

	public boolean isInputValid() {

		if (calendarField.getValue() == null)
			return false;

		return true;

	}

}
