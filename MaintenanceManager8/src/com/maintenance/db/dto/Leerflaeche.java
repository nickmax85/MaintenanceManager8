package com.maintenance.db.dto;

import java.sql.Timestamp;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Leerflaeche {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty timestampString = new SimpleStringProperty();
	private StringProperty user = new SimpleStringProperty();

	private Timestamp timestampSql;

	private int panelFormatId;
	private PanelFormat panelFormat;

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

	public StringProperty timestampStringProperty() {
		return this.timestampString;
	}

	public String getTimestampString() {
		return this.timestampStringProperty().get();
	}

	public void setTimestampString(final String timestampString) {
		this.timestampStringProperty().set(timestampString);
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

	public Timestamp getTimestampSql() {
		return timestampSql;
	}

	public void setTimestampSql(Timestamp timestampSql) {
		this.timestampSql = timestampSql;
	}

	public int getPanelFormatId() {
		return panelFormatId;
	}

	public void setPanelFormatId(int panelFormatId) {
		this.panelFormatId = panelFormatId;
	}

	public PanelFormat getPanelFormat() {
		return panelFormat;
	}

	public void setPanelFormat(PanelFormat panelFormat) {
		this.panelFormat = panelFormat;
	}

}
