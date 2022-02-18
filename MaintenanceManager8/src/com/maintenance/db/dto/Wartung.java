package com.maintenance.db.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Wartung {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty auftrag = new SimpleStringProperty();
	private ObjectProperty<Date> date = new SimpleObjectProperty<>();
	private IntegerProperty prozent = new SimpleIntegerProperty();
	private StringProperty mitarbeiter = new SimpleStringProperty();
	private StringProperty info = new SimpleStringProperty();
	private IntegerProperty status = new SimpleIntegerProperty();
	private BooleanProperty tpm = new SimpleBooleanProperty();

	private StringProperty timestamp = new SimpleStringProperty();

	private Timestamp timestampSql;

	private int anlageId;
	private Anlage anlage;

	private Station station;
	private int stationId;

	private List<Anhang> anhangList;
	private BooleanProperty anhang = new SimpleBooleanProperty();

	public IntegerProperty idProperty() {
		return this.id;
	}

	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public StringProperty auftragProperty() {
		return this.auftrag;
	}

	public String getAuftrag() {
		return this.auftragProperty().get();
	}

	public void setAuftrag(final String auftrag) {
		this.auftragProperty().set(auftrag);
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

	public IntegerProperty prozentProperty() {
		return this.prozent;
	}

	public int getProzent() {
		return this.prozentProperty().get();
	}

	public void setProzent(final int prozent) {
		this.prozentProperty().set(prozent);
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

	public StringProperty infoProperty() {
		return this.info;
	}

	public String getInfo() {
		return this.infoProperty().get();
	}

	public void setInfo(final String info) {
		this.infoProperty().set(info);
	}

	public StringProperty timestampProperty() {
		return this.timestamp;
	}

	public String getTimestamp() {
		return this.timestampProperty().get();
	}

	public void setTimestamp(final String timestamp) {
		this.timestampProperty().set(timestamp);
	}

	public Timestamp getTimestampSql() {
		return timestampSql;
	}

	public void setTimestampSql(Timestamp timestampSql) {
		this.timestampSql = timestampSql;
	}

	public int getAnlageId() {
		return anlageId;
	}

	public void setAnlageId(int anlageId) {
		this.anlageId = anlageId;
	}

	public Anlage getAnlage() {
		return anlage;
	}

	public void setAnlage(Anlage anlage) {
		this.anlage = anlage;
	}

	public Station getStation() {
		return station;
	}

	public void setStation(Station station) {
		this.station = station;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public List<Anhang> getAnhangList() {
		return anhangList;
	}

	public void setAnhangList(List<Anhang> anhangList) {
		this.anhangList = anhangList;
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

	public BooleanProperty anhangProperty() {
		return this.anhang;
	}

	public boolean isAnhang() {
		return this.anhangProperty().get();
	}

	public void setAnhang(final boolean anhang) {
		this.anhangProperty().set(anhang);
	}

	public BooleanProperty tpmProperty() {
		return this.tpm;
	}

	public boolean isTpm() {
		return this.tpmProperty().get();
	}

	public void setTpm(final boolean tpm) {
		this.tpmProperty().set(tpm);
	}

	public enum EWartungArt {

		STUECKZAHL("Stückzahl"), TIME_INTERVALL("Zeitintervall");

		private String label;

		EWartungArt(String label) {
			this.label = label;

		}

		@Override
		public String toString() {
			return label;
		}

	}
	
	public enum EWartungTyp {

		MAINTENANCE("Maintenance"), AUTONOMOUS_TPM("Total Productivity Management"), ROBOT("Robotic");

		private String label;

		EWartungTyp(String label) {
			this.label = label;

		}

		@Override
		public String toString() {
			return label;
		}

	}

}
