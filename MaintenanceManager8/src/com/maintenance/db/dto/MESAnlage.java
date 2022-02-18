package com.maintenance.db.dto;

import java.sql.Timestamp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class MESAnlage {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private IntegerProperty prodStueck = new SimpleIntegerProperty();
	private StringProperty timestamp = new SimpleStringProperty();

	private Timestamp timestampSql;

	private int anlageId;
	private Anlage anlage;

	private int anlage2Id;
	private Anlage anlage2;

	public Timestamp getTimestampSql() {
		return timestampSql;
	}

	public void setTimestampSql(Timestamp timestampSql) {
		this.timestampSql = timestampSql;
	}

	public IntegerProperty idProperty() {
		return this.id;
	}

	public int getId() {
		return this.idProperty().get();
	}

	public void setId(final int id) {
		this.idProperty().set(id);
	}

	public StringProperty nameProperty() {
		return this.name;
	}

	public String getName() {
		return this.nameProperty().get();
	}

	public void setName(final String name) {
		this.nameProperty().set(name);
	}

	public IntegerProperty prodStueckProperty() {
		return this.prodStueck;
	}

	public int getProdStueck() {
		return this.prodStueckProperty().get();
	}

	public void setProdStueck(final int prodStueck) {
		this.prodStueckProperty().set(prodStueck);
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

	public int getAnlage2Id() {
		return anlage2Id;
	}

	public void setAnlage2Id(int anlage2Id) {
		this.anlage2Id = anlage2Id;
	}

	public Anlage getAnlage2() {
		return anlage2;
	}

	public void setAnlage2(Anlage anlage2) {
		this.anlage2 = anlage2;
	}

	@Override
	public String toString() {
		return "MESAnlage [id=" + id + ", name=" + name + ", anlageId=" + anlageId + ", anlage=" + anlage
				+ ", anlage2Id=" + anlage2Id + ", anlage2=" + anlage2 + "]";
	}

}
