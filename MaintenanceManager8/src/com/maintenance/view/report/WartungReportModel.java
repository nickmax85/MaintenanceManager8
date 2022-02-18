package com.maintenance.view.report;

import java.util.Date;

import com.maintenance.db.dto.Wartung;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class WartungReportModel {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty anlage = new SimpleStringProperty();
	private StringProperty station = new SimpleStringProperty();
	private IntegerProperty status = new SimpleIntegerProperty();
	private ObjectProperty<Date> date = new SimpleObjectProperty<>();
	private StringProperty info = new SimpleStringProperty();
	private StringProperty mitarbeiter = new SimpleStringProperty();

	private Wartung wartung;

	public IntegerProperty idProperty() {
		return this.id;
	}

	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public StringProperty anlageProperty() {
		return this.anlage;
	}

	public String getAnlage() {
		return this.anlageProperty().get();
	}

	public void setAnlage(final String anlage) {
		this.anlageProperty().set(anlage);
	}

	public StringProperty stationProperty() {
		return this.station;
	}

	public String getStation() {
		return this.stationProperty().get();
	}

	public void setStation(final String station) {
		this.stationProperty().set(station);
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

	public StringProperty infoProperty() {
		return this.info;
	}

	public String getInfo() {
		return this.infoProperty().get();
	}

	public void setInfo(final String info) {
		this.infoProperty().set(info);
	}

	public StringProperty mitarbeiterProperty() {
		return this.mitarbeiter;
	}

	public String getMitarbeiter() {
		return this.mitarbeiterProperty().get();
	}

	public void setMitarbeiter(final String mitarbeiter) {
		this.mitarbeiterProperty().set(mitarbeiter);
	}

	public IntegerProperty statusProperty() {
		return this.status;
	}

	public int getStatus() {
		return this.statusProperty().get();
	}

	public void setStatus(final int status) {
		this.statusProperty().set(status);
	}

	public Wartung getWartung() {
		return wartung;
	}

	public void setWartung(Wartung wartung) {
		this.wartung = wartung;
	}

}
