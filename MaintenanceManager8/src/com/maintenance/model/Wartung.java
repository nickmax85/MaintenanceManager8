package com.maintenance.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "wartung")
public class Wartung {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty auftragNr = new SimpleStringProperty();
	private ObjectProperty<Date> faellig = new SimpleObjectProperty<Date>();
	private IntegerProperty prozent = new SimpleIntegerProperty();
	private StringProperty mitarbeiter = new SimpleStringProperty();
	private StringProperty info = new SimpleStringProperty();
	private BooleanProperty wartungNichtMoeglich = new SimpleBooleanProperty();
	private IntegerProperty status = new SimpleIntegerProperty();
	private BooleanProperty tpm = new SimpleBooleanProperty();
	private ObjectProperty<Date> timestamp = new SimpleObjectProperty<Date>();
	private StringProperty user = new SimpleStringProperty();

	public IntegerProperty idProperty() {
		return this.id;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public StringProperty auftragNrProperty() {
		return this.auftragNr;
	}

	public String getAuftragNr() {
		return this.auftragNrProperty().get();
	}

	public void setAuftragNr(final String auftragNr) {
		this.auftragNrProperty().set(auftragNr);
	}

	public ObjectProperty<Date> faelligProperty() {
		return this.faellig;
	}

	public Date getFaellig() {
		return this.faelligProperty().get();
	}

	public void setFaellig(final Date faellig) {
		this.faelligProperty().set(faellig);
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

	public BooleanProperty wartungNichtMoeglichProperty() {
		return this.wartungNichtMoeglich;
	}

	public boolean isWartungNichtMoeglich() {
		return this.wartungNichtMoeglichProperty().get();
	}

	public void setWartungNichtMoeglich(final boolean wartungNichtMoeglich) {
		this.wartungNichtMoeglichProperty().set(wartungNichtMoeglich);
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

	public BooleanProperty tpmProperty() {
		return this.tpm;
	}

	public boolean isTpm() {
		return this.tpmProperty().get();
	}

	public void setTpm(final boolean tpm) {
		this.tpmProperty().set(tpm);
	}

	public ObjectProperty<Date> timestampProperty() {
		return this.timestamp;
	}

	public Date getTimestamp() {
		return this.timestampProperty().get();
	}

	public void setTimestamp(final Date timestamp) {
		this.timestampProperty().set(timestamp);
	}

	public StringProperty userProperty() {
		return this.user;
	}

	public String getUser() {
		return this.userProperty().get();
	}

	public void setUser(final String user) {
		this.userProperty().set(user);
	}

}
