package com.maintenance.db.dto;

import java.util.Date;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class CalendarWartung {

	private IntegerProperty id = new SimpleIntegerProperty();
	private ObjectProperty<Date> date = new SimpleObjectProperty<>();
	private StringProperty remark = new SimpleStringProperty();

	private Anlage anlage;

	public IntegerProperty idProperty() {
		return this.id;
	}

	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public ObjectProperty<Date> dateProperty() {
		return this.date;
	}

	public Date getDate() {
		return this.dateProperty().get();
	}

	public void setDate(final Date date) {
		this.dateProperty().set(date);
	}

	public StringProperty remarkProperty() {
		return this.remark;
	}

	public String getRemark() {
		return this.remarkProperty().get();
	}

	public void setRemark(final String remark) {
		this.remarkProperty().set(remark);
	}

	public Anlage getAnlage() {
		return anlage;
	}

	public void setAnlage(Anlage anlage) {
		this.anlage = anlage;
	}

	@Override
	public String toString() {
		return "CalendarWartung [id=" + id + ", date=" + date + ", remark=" + remark + "]";
	}

}
