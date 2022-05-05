package com.maintenance.db.dto;

import java.sql.Timestamp;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.Table;
import javax.persistence.Transient;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import com.maintenance.model.User;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "anlage")
public class Anlage {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty name = new SimpleStringProperty();
	private StringProperty equipment = new SimpleStringProperty();
	private StringProperty auftrag = new SimpleStringProperty();
	private IntegerProperty jahresStueck = new SimpleIntegerProperty();
	private IntegerProperty aktuelleStueck = new SimpleIntegerProperty();
	private IntegerProperty wartungStueckIntervall = new SimpleIntegerProperty();
	private IntegerProperty wartungDateIntervall = new SimpleIntegerProperty();
	private IntegerProperty lastWartungStueckzahl = new SimpleIntegerProperty();
	private ObjectProperty<Date> lastWartungDate = new SimpleObjectProperty<Date>();
	private BooleanProperty auswertung = new SimpleBooleanProperty();
	private IntegerProperty wartungStueckWarnung = new SimpleIntegerProperty();
	private IntegerProperty wartungStueckFehler = new SimpleIntegerProperty();
	private IntegerProperty wartungDateWarnung = new SimpleIntegerProperty();
	private BooleanProperty status = new SimpleBooleanProperty();
	private BooleanProperty subMenu = new SimpleBooleanProperty();
	private IntegerProperty wartungArt = new SimpleIntegerProperty();
	private StringProperty produkte = new SimpleStringProperty();
	private IntegerProperty intervallDateUnit = new SimpleIntegerProperty();
	private IntegerProperty warnungDateUnit = new SimpleIntegerProperty();
	private IntegerProperty wartungUeberfaellig = new SimpleIntegerProperty();
	private StringProperty wartungsplanLink = new SimpleStringProperty();
	private IntegerProperty tpmStep = new SimpleIntegerProperty();

	private ObjectProperty<Date> createDate = new SimpleObjectProperty<Date>();
	private StringProperty user = new SimpleStringProperty();
	private StringProperty timestamp = new SimpleStringProperty();

	private DoubleProperty intervall = new SimpleDoubleProperty();

	private int panelFormatId;
	private PanelFormat panelFormat;

	private int abteilungId;
	private Abteilung abteilung;

	private List<Anhang> anhangList;
	private BooleanProperty anhang = new SimpleBooleanProperty();

	private List<MESAnlage> mesAnlagen;

	private List<User> users;

	private Timestamp timestampSql;

	@Transient
	public PanelFormat getPanelFormat() {
		return panelFormat;
	}

	public void setPanelFormat(PanelFormat panelFormat) {
		this.panelFormat = panelFormat;
	}

	@Transient
	public Abteilung getAbteilung() {
		return abteilung;
	}

	public void setAbteilung(Abteilung abteilung) {
		this.abteilung = abteilung;
	}

	public List<Anhang> getAnhangList() {
		return anhangList;
	}

	public void setAnhangList(List<Anhang> anhangList) {
		this.anhangList = anhangList;
	}

	public int getPanelFormatId() {
		return panelFormatId;
	}

	public void setPanelFormatId(int panelFormatId) {
		this.panelFormatId = panelFormatId;
	}

	public int getAbteilungId() {
		return abteilungId;
	}

	public void setAbteilungId(int abteilungId) {
		this.abteilungId = abteilungId;
	}

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

	public StringProperty nameProperty() {
		return this.name;
	}

	public String getName() {
		return this.nameProperty().get();
	}

	public void setName(final String name) {
		this.nameProperty().set(name);
	}

	public StringProperty auftragProperty() {
		return this.auftrag;
	}

	@Transient
	public String getAuftrag() {
		return this.auftragProperty().get();
	}

	public void setAuftrag(final String auftrag) {
		this.auftragProperty().set(auftrag);
	}

	public IntegerProperty jahresStueckProperty() {
		return this.jahresStueck;
	}

	@Transient
	public int getJahresStueck() {
		return this.jahresStueckProperty().get();
	}

	public void setJahresStueck(final int jahresStueck) {
		this.jahresStueckProperty().set(jahresStueck);
	}

	public IntegerProperty aktuelleStueckProperty() {
		return this.aktuelleStueck;
	}

	@Transient
	public int getAktuelleStueck() {
		return this.aktuelleStueckProperty().get();
	}

	public void setAktuelleStueck(final int aktuelleStueck) {
		this.aktuelleStueckProperty().set(aktuelleStueck);
	}

	public IntegerProperty lastWartungStueckzahlProperty() {
		return this.lastWartungStueckzahl;
	}

	@Transient
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

	public BooleanProperty statusProperty() {
		return this.status;
	}

	public boolean isStatus() {
		return this.statusProperty().get();
	}

	public void setStatus(final boolean status) {
		this.statusProperty().set(status);
	}

	public StringProperty produkteProperty() {
		return this.produkte;
	}

	public String getProdukte() {
		return this.produkteProperty().get();
	}

	public void setProdukte(final String produkte) {
		this.produkteProperty().set(produkte);
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
	
	public IntegerProperty tpmStepProperty() {
		return this.tpmStep;
	}

	public Integer getTpmStep() {
		return this.tpmStepProperty().get();
	}

	public void setTpmStep(final Integer tpmStep) {
		this.tpmStepProperty().set(tpmStep);
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

	@Transient
	public Timestamp getTimestampSql() {
		return timestampSql;
	}

	public void setTimestampSql(Timestamp timestampSql) {
		this.timestampSql = timestampSql;
	}

	public BooleanProperty subMenuProperty() {
		return this.subMenu;
	}

	@Transient
	public boolean isSubMenu() {
		return this.subMenuProperty().get();
	}

	public void setSubMenu(final boolean subMenu) {
		this.subMenuProperty().set(subMenu);
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

	public BooleanProperty anhangProperty() {
		return this.anhang;
	}

	public boolean isAnhang() {
		return this.anhangProperty().get();
	}

	public void setAnhang(final boolean anhang) {
		this.anhangProperty().set(anhang);
	}

	public IntegerProperty wartungArtProperty() {
		return this.wartungArt;
	}

	@Transient
	public int getWartungArt() {
		return this.wartungArtProperty().get();
	}

	public void setWartungArt(final int wartungArt) {
		this.wartungArtProperty().set(wartungArt);
	}

	public IntegerProperty wartungStueckIntervallProperty() {
		return this.wartungStueckIntervall;
	}

	@Transient
	public int getWartungStueckIntervall() {
		return this.wartungStueckIntervallProperty().get();
	}

	public void setWartungStueckIntervall(final int wartungStueckIntervall) {
		this.wartungStueckIntervallProperty().set(wartungStueckIntervall);
	}

	public ObjectProperty<Date> lastWartungDateProperty() {
		return this.lastWartungDate;
	}

	@Transient
	public Date getLastWartungDate() {
		return this.lastWartungDateProperty().get();
	}

	public void setLastWartungDate(final Date lastWartungDate) {
		this.lastWartungDateProperty().set(lastWartungDate);
	}

	public IntegerProperty wartungDateIntervallProperty() {
		return this.wartungDateIntervall;
	}

	@Transient
	public int getWartungDateIntervall() {
		return this.wartungDateIntervallProperty().get();
	}

	public void setWartungDateIntervall(final int wartungDateIntervall) {
		this.wartungDateIntervallProperty().set(wartungDateIntervall);
	}

	public IntegerProperty wartungStueckWarnungProperty() {
		return this.wartungStueckWarnung;
	}

	@Transient
	public int getWartungStueckWarnung() {
		return this.wartungStueckWarnungProperty().get();
	}

	public void setWartungStueckWarnung(final int wartungStueckWarnung) {
		this.wartungStueckWarnungProperty().set(wartungStueckWarnung);
	}

	public IntegerProperty wartungStueckFehlerProperty() {
		return this.wartungStueckFehler;
	}

	@Transient
	public int getWartungStueckFehler() {
		return this.wartungStueckFehlerProperty().get();
	}

	public void setWartungStueckFehler(final int wartungStueckFehler) {
		this.wartungStueckFehlerProperty().set(wartungStueckFehler);
	}

	public IntegerProperty wartungDateWarnungProperty() {
		return this.wartungDateWarnung;
	}

	@Transient
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

	@Transient
	public String getWartungsplanLink() {
		return this.wartungsplanLinkProperty().get();
	}

	public void setWartungsplanLink(final String wartungsplanLink) {
		this.wartungsplanLinkProperty().set(wartungsplanLink);
	}

	public IntegerProperty intervallDateUnitProperty() {
		return this.intervallDateUnit;
	}

	@Transient
	public int getIntervallDateUnit() {
		return this.intervallDateUnitProperty().get();
	}

	public void setIntervallDateUnit(final int intervallDateUnit) {
		this.intervallDateUnitProperty().set(intervallDateUnit);
	}

	public IntegerProperty warnungDateUnitProperty() {
		return this.warnungDateUnit;
	}

	@Transient
	public int getWarnungDateUnit() {
		return this.warnungDateUnitProperty().get();
	}

	public void setWarnungDateUnit(final int warnungDateUnit) {
		this.warnungDateUnitProperty().set(warnungDateUnit);
	}

	public DoubleProperty intervallProperty() {
		return this.intervall;
	}

	@Transient
	public double getIntervall() {
		return this.intervallProperty().get();
	}

	public void setIntervall(final double intervall) {
		this.intervallProperty().set(intervall);
	}

	@Transient
	public List<MESAnlage> getMesAnlagen() {
		return mesAnlagen;
	}

	public void setMesAnlagen(List<MESAnlage> mesAnlagen) {
		this.mesAnlagen = mesAnlagen;
	}

	public IntegerProperty wartungUeberfaelligProperty() {
		return this.wartungUeberfaellig;
	}

	@Transient
	public int getWartungUeberfaellig() {
		return this.wartungUeberfaelligProperty().get();
	}

	public void setWartungUeberfaellig(final int wartungUeberfaellig) {
		this.wartungUeberfaelligProperty().set(wartungUeberfaellig);
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

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "anlage_user", joinColumns = { @JoinColumn(name = "anlageId") }, inverseJoinColumns = {
			@JoinColumn(name = "userId") })
	public List<User> getUsers() {
		return users;
	}

	public void setUsers(List<User> users) {
		this.users = users;
	}

}
