package com.maintenance.db.dto;

import java.sql.Timestamp;
import java.util.ArrayList;
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

public class Station {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty equipment = new SimpleStringProperty();
	private StringProperty auftrag = new SimpleStringProperty();
	private IntegerProperty wartungStueckIntervall = new SimpleIntegerProperty();
	private IntegerProperty wartungDateIntervall = new SimpleIntegerProperty();
	private IntegerProperty lastWartungStueckzahl = new SimpleIntegerProperty();
	private ObjectProperty<Date> lastWartungDate = new SimpleObjectProperty<Date>();
	private BooleanProperty auswertung = new SimpleBooleanProperty();
	private IntegerProperty wartungStueckWarnung = new SimpleIntegerProperty();
	private IntegerProperty wartungStueckFehler = new SimpleIntegerProperty();
	private IntegerProperty wartungDateWarnung = new SimpleIntegerProperty();
	private BooleanProperty status = new SimpleBooleanProperty();
	private BooleanProperty tpm = new SimpleBooleanProperty();
	private BooleanProperty robot = new SimpleBooleanProperty();
	private BooleanProperty mailSent = new SimpleBooleanProperty();
	private IntegerProperty wartungArt = new SimpleIntegerProperty();
	private IntegerProperty intervallDateUnit = new SimpleIntegerProperty();
	private IntegerProperty warnungDateUnit = new SimpleIntegerProperty();
	private StringProperty wartungsplanLink = new SimpleStringProperty();
	private StringProperty user = new SimpleStringProperty();

	private ObjectProperty<Date> createDate = new SimpleObjectProperty<Date>();
	private StringProperty timestamp = new SimpleStringProperty();
	private Timestamp timestampSql;

	private int panelFormatId;
	private PanelFormat panelFormat;

	private int anlageId;
	private Anlage anlage;

	private List<Wartung> wartungList = new ArrayList<Wartung>();

	private List<Anhang> anhangList;
	private BooleanProperty anhang = new SimpleBooleanProperty();

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

	public List<Wartung> getWartungList() {
		return wartungList;
	}

	public void setWartungList(List<Wartung> wartungList) {
		this.wartungList = wartungList;
	}

	public List<Anhang> getAnhangList() {
		return anhangList;
	}

	public void setAnhangList(List<Anhang> anhangList) {
		this.anhangList = anhangList;
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

	public StringProperty equipmentProperty() {
		return this.equipment;
	}

	public String getEquipment() {
		return this.equipmentProperty().get();
	}

	public void setEquipment(final String equipment) {
		this.equipmentProperty().set(equipment);
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

	public IntegerProperty lastWartungStueckzahlProperty() {
		return this.lastWartungStueckzahl;
	}

	public int getLastWartungStueckzahl() {
		return this.lastWartungStueckzahlProperty().get();
	}

	public void setLastWartungStueckzahl(final int lastWartungStueckzahl) {
		this.lastWartungStueckzahlProperty().set(lastWartungStueckzahl);
	}

	public BooleanProperty auswertungProperty() {
		return this.auswertung;
	}

	public boolean isAuswertung() {
		return this.auswertungProperty().get();
	}

	public void setAuswertung(final boolean auswertung) {
		this.auswertungProperty().set(auswertung);
	}

	public IntegerProperty wartungStueckWarnungProperty() {
		return this.wartungStueckWarnung;
	}

	public int getWartungStueckWarnung() {
		return this.wartungStueckWarnungProperty().get();
	}

	public void setWartungStueckWarnung(final int wartungStueckWarnung) {
		this.wartungStueckWarnungProperty().set(wartungStueckWarnung);
	}

	public IntegerProperty wartungStueckFehlerProperty() {
		return this.wartungStueckFehler;
	}

	public int getWartungStueckFehler() {
		return this.wartungStueckFehlerProperty().get();
	}

	public void setWartungStueckFehler(final int wartungFehler) {
		this.wartungStueckFehlerProperty().set(wartungFehler);
	}

	public BooleanProperty statusProperty() {
		return this.status;
	}

	public boolean isStatus() {
		return this.statusProperty().get();
	}

	public void setStatus(final boolean status) {
		this.statusProperty().set(status);
	}

	public IntegerProperty wartungArtProperty() {
		return this.wartungArt;
	}

	public int getWartungArt() {
		return this.wartungArtProperty().get();
	}

	public void setWartungArt(final int wartungArt) {
		this.wartungArtProperty().set(wartungArt);
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

	public StringProperty timestampProperty() {
		return this.timestamp;
	}

	public String getTimestamp() {
		return this.timestampProperty().get();
	}

	public void setTimestamp(final String timestamp) {
		this.timestampProperty().set(timestamp);
	}

	public IntegerProperty wartungStueckIntervallProperty() {
		return this.wartungStueckIntervall;
	}

	public int getWartungStueckIntervall() {
		return this.wartungStueckIntervallProperty().get();
	}

	public void setWartungStueckIntervall(final int wartungStueckIntervall) {
		this.wartungStueckIntervallProperty().set(wartungStueckIntervall);
	}

	public ObjectProperty<Date> lastWartungDateProperty() {
		return this.lastWartungDate;
	}

	public Date getLastWartungDate() {
		return this.lastWartungDateProperty().get();
	}

	public void setLastWartungDate(final Date lastWartungDate) {
		this.lastWartungDateProperty().set(lastWartungDate);
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

	public IntegerProperty wartungDateIntervallProperty() {
		return this.wartungDateIntervall;
	}

	public int getWartungDateIntervall() {
		return this.wartungDateIntervallProperty().get();
	}

	public void setWartungDateIntervall(final int wartungDateIntervall) {
		this.wartungDateIntervallProperty().set(wartungDateIntervall);
	}

	public IntegerProperty wartungDateWarnungProperty() {
		return this.wartungDateWarnung;
	}

	public int getWartungDateWarnung() {
		return this.wartungDateWarnungProperty().get();
	}

	public void setWartungDateWarnung(final int wartungDateWarnung) {
		this.wartungDateWarnungProperty().set(wartungDateWarnung);
	}

	@Override
	public String toString() {
		return getName();
	}

	public StringProperty wartungsplanLinkProperty() {
		return this.wartungsplanLink;
	}

	public String getWartungsplanLink() {
		return this.wartungsplanLinkProperty().get();
	}

	public void setWartungsplanLink(final String wartungsplanLink) {
		this.wartungsplanLinkProperty().set(wartungsplanLink);
	}

	public IntegerProperty intervallDateUnitProperty() {
		return this.intervallDateUnit;
	}

	public int getIntervallDateUnit() {
		return this.intervallDateUnitProperty().get();
	}

	public void setIntervallDateUnit(final int intervallDateUnit) {
		this.intervallDateUnitProperty().set(intervallDateUnit);
	}

	public IntegerProperty warnungDateUnitProperty() {
		return this.warnungDateUnit;
	}

	public int getWarnungDateUnit() {
		return this.warnungDateUnitProperty().get();
	}

	public void setWarnungDateUnit(final int warnungDateUnit) {
		this.warnungDateUnitProperty().set(warnungDateUnit);
	}

	public ObjectProperty<Date> createDateProperty() {
		return this.createDate;
	}

	public Date getCreateDate() {
		return this.createDateProperty().get();
	}

	public void setCreateDate(final Date createDate) {
		this.createDateProperty().set(createDate);
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

	public BooleanProperty robotProperty() {
		return this.robot;
	}

	public boolean isRobot() {
		return this.robotProperty().get();
	}

	public void setRobot(final boolean robot) {
		this.robotProperty().set(robot);
	}

	public BooleanProperty mailSentProperty() {
		return this.mailSent;
	}

	public boolean isMailSent() {
		return this.mailSentProperty().get();
	}

	public void setMailSent(final boolean mailSent) {
		this.mailSentProperty().set(mailSent);
	}

}
