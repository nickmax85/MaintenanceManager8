package com.maintenance.model;

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
import javax.persistence.OneToMany;
import javax.persistence.Table;

import org.hibernate.annotations.LazyCollection;
import org.hibernate.annotations.LazyCollectionOption;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleBooleanProperty;
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
	private StringProperty auftragNr = new SimpleStringProperty();

	private IntegerProperty jahresStueck = new SimpleIntegerProperty();
	private IntegerProperty aktuelleStueck = new SimpleIntegerProperty();
	private IntegerProperty wartungStueckIntervall = new SimpleIntegerProperty();
	private IntegerProperty wartungDateIntervall = new SimpleIntegerProperty();
	private IntegerProperty lastWartungStueck = new SimpleIntegerProperty();

	private ObjectProperty<Date> lastWartungDate = new SimpleObjectProperty<Date>();

	private IntegerProperty wartungStueckWarnung = new SimpleIntegerProperty();
	private IntegerProperty wartungStueckFehler = new SimpleIntegerProperty();
	private IntegerProperty wartungDateWarnung = new SimpleIntegerProperty();

	private BooleanProperty auswertung = new SimpleBooleanProperty();
	private BooleanProperty status = new SimpleBooleanProperty();
	private BooleanProperty subMenu = new SimpleBooleanProperty();

	private IntegerProperty wartungArt = new SimpleIntegerProperty();
	private StringProperty wartungsPlanLink = new SimpleStringProperty();
	private StringProperty produkte = new SimpleStringProperty();

	private IntegerProperty intervallDateUnit = new SimpleIntegerProperty();
	private IntegerProperty warnungDateUnit = new SimpleIntegerProperty();
	private IntegerProperty wartungUeberfaellig = new SimpleIntegerProperty();

	private BooleanProperty mailSent = new SimpleBooleanProperty();

	private ObjectProperty<Date> createDate = new SimpleObjectProperty<Date>();
	private ObjectProperty<Date> timestamp = new SimpleObjectProperty<Date>();
	private StringProperty user = new SimpleStringProperty();

	private List<Station> stationen;
	private List<User> users;

	public IntegerProperty idProperty() {
		return this.id;
	}

	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	public Integer getId() {
		return this.idProperty().get();
	}

	public void setId(final Integer id) {
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

	@OneToMany(fetch = FetchType.EAGER, mappedBy = "anlage", cascade = CascadeType.ALL)
	public List<Station> getStationen() {
		return stationen;
	}

	public void setStationen(List<Station> stationen) {
		this.stationen = stationen;
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

	public StringProperty equipmentProperty() {
		return this.equipment;
	}

	public String getEquipment() {
		return this.equipmentProperty().get();
	}

	public void setEquipment(final String equipment) {
		this.equipmentProperty().set(equipment);
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

	public IntegerProperty jahresStueckProperty() {
		return this.jahresStueck;
	}

	public Integer getJahresStueck() {
		return this.jahresStueckProperty().get();
	}

	public void setJahresStueck(final Integer jahresStueck) {
		if (jahresStueck != null)
			this.jahresStueckProperty().set(jahresStueck);
	}

	public IntegerProperty aktuelleStueckProperty() {
		return this.aktuelleStueck;
	}

	public Integer getAktuelleStueck() {
		return this.aktuelleStueckProperty().get();
	}

	public void setAktuelleStueck(final Integer aktuelleStueck) {
		if (aktuelleStueck != null)
			this.aktuelleStueckProperty().set(aktuelleStueck);
	}

	public IntegerProperty wartungStueckIntervallProperty() {
		return this.wartungStueckIntervall;
	}

	public Integer getWartungStueckIntervall() {
		return this.wartungStueckIntervallProperty().get();
	}

	public void setWartungStueckIntervall(final Integer wartungStueckIntervall) {
		if (wartungStueckIntervall != null)
			this.wartungStueckIntervallProperty().set(wartungStueckIntervall);
	}

	public IntegerProperty wartungDateIntervallProperty() {
		return this.wartungDateIntervall;
	}

	public Integer getWartungDateIntervall() {
		return this.wartungDateIntervallProperty().get();
	}

	public void setWartungDateIntervall(final Integer wartungDateIntervall) {
		if (wartungDateIntervall != null)
			this.wartungDateIntervallProperty().set(wartungDateIntervall);
	}

	public IntegerProperty lastWartungStueckProperty() {
		return this.lastWartungStueck;
	}

	public Integer getLastWartungStueck() {
		return this.lastWartungStueckProperty().get();
	}

	public void setLastWartungStueck(final Integer lastWartungStueck) {
		if (lastWartungStueck != null)
			this.lastWartungStueckProperty().set(lastWartungStueck);
	}

	public ObjectProperty<Date> lastWartungDateProperty() {
		return this.lastWartungDate;
	}

	public Date getLastWartungDate() {
		return this.lastWartungDateProperty().get();
	}

	public void setLastWartungDate(final Date lastWartungDate) {
		if (lastWartungDate != null)
			this.lastWartungDateProperty().set(lastWartungDate);
	}

	public IntegerProperty wartungStueckWarnungProperty() {
		return this.wartungStueckWarnung;
	}

	public Integer getWartungStueckWarnung() {
		return this.wartungStueckWarnungProperty().get();
	}

	public void setWartungStueckWarnung(final Integer wartungStueckWarnung) {
		if (wartungStueckWarnung != null)
			this.wartungStueckWarnungProperty().set(wartungStueckWarnung);
	}

	public IntegerProperty wartungStueckFehlerProperty() {
		return this.wartungStueckFehler;
	}

	public Integer getWartungStueckFehler() {
		return this.wartungStueckFehlerProperty().get();
	}

	public void setWartungStueckFehler(final Integer wartungStueckFehler) {
		if (wartungStueckFehler != null)
			this.wartungStueckFehlerProperty().set(wartungStueckFehler);
	}

	public IntegerProperty wartungDateWarnungProperty() {
		return this.wartungDateWarnung;
	}

	public Integer getWartungDateWarnung() {
		return this.wartungDateWarnungProperty().get();
	}

	public void setWartungDateWarnung(final Integer wartungDateWarnung) {
		if (wartungDateWarnung != null)
			this.wartungDateWarnungProperty().set(wartungDateWarnung);
	}

	public BooleanProperty auswertungProperty() {
		return this.auswertung;
	}

	public Boolean isAuswertung() {
		return this.auswertungProperty().get();
	}

	public void setAuswertung(final Boolean auswertung) {
		this.auswertungProperty().set(auswertung);
	}

	public BooleanProperty statusProperty() {
		return this.status;
	}

	public Boolean isStatus() {
		return this.statusProperty().get();
	}

	public void setStatus(final Boolean status) {
		this.statusProperty().set(status);
	}

	public BooleanProperty subMenuProperty() {
		return this.subMenu;
	}

	public Boolean isSubMenu() {
		return this.subMenuProperty().get();
	}

	public void setSubMenu(final Boolean subMenu) {

		if (subMenu != null)
			this.subMenuProperty().set(subMenu);
	}

	public IntegerProperty wartungArtProperty() {
		return this.wartungArt;
	}

	public Integer getWartungArt() {
		return this.wartungArtProperty().get();
	}

	public void setWartungArt(final Integer wartungArt) {
		if (wartungArt != null)
			this.wartungArtProperty().set(wartungArt);
	}

	public StringProperty wartungsPlanLinkProperty() {
		return this.wartungsPlanLink;
	}

	public String getWartungsPlanLink() {
		return this.wartungsPlanLinkProperty().get();
	}

	public void setWartungsPlanLink(final String wartungsPlanLink) {
		this.wartungsPlanLinkProperty().set(wartungsPlanLink);
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

	public IntegerProperty intervallDateUnitProperty() {
		return this.intervallDateUnit;
	}

	public Integer getIntervallDateUnit() {
		return this.intervallDateUnitProperty().get();
	}

	public void setIntervallDateUnit(final Integer intervallDateUnit) {
		if (intervallDateUnit != null)
			this.intervallDateUnitProperty().set(intervallDateUnit);
	}

	public IntegerProperty warnungDateUnitProperty() {
		return this.warnungDateUnit;
	}

	public Integer getWarnungDateUnit() {
		return this.warnungDateUnitProperty().get();
	}

	public void setWarnungDateUnit(final Integer warnungDateUnit) {
		if (warnungDateUnit != null)
			this.warnungDateUnitProperty().set(warnungDateUnit);
	}

	public IntegerProperty wartungUeberfaelligProperty() {
		return this.wartungUeberfaellig;
	}

	public Integer getWartungUeberfaellig() {
		return this.wartungUeberfaelligProperty().get();
	}

	public void setWartungUeberfaellig(final Integer wartungUeberfaellig) {
		if (wartungUeberfaellig != null)
			this.wartungUeberfaelligProperty().set(wartungUeberfaellig);
	}

	public BooleanProperty mailSentProperty() {
		return this.mailSent;
	}

	public Boolean isMailSent() {
		return this.mailSentProperty().get();
	}

	public void setMailSent(final Boolean mailSent) {
		if (mailSent != null)
			this.mailSentProperty().set(mailSent);
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
