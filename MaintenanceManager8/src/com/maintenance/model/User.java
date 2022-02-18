package com.maintenance.model;

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

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

@Entity
@Table(name = "user")
public class User {

	private IntegerProperty id = new SimpleIntegerProperty();
	private StringProperty firstName = new SimpleStringProperty();
	private StringProperty lastName = new SimpleStringProperty();
	private StringProperty mail = new SimpleStringProperty();

	private List<Anlage> anlagen;

	private BooleanProperty active = new SimpleBooleanProperty();

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

	public StringProperty firstNameProperty() {
		return this.firstName;
	}

	public String getFirstName() {
		return this.firstNameProperty().get();
	}

	public void setFirstName(final String firstName) {
		this.firstNameProperty().set(firstName);
	}

	public StringProperty mailProperty() {
		return this.mail;
	}

	public String getMail() {
		return this.mailProperty().get();
	}

	public void setMail(final String mail) {
		this.mailProperty().set(mail);
	}

	@LazyCollection(LazyCollectionOption.FALSE)
	@ManyToMany(fetch = FetchType.LAZY, cascade = { CascadeType.MERGE })
	@JoinTable(name = "anlage_user", joinColumns = { @JoinColumn(name = "anlageId") }, inverseJoinColumns = {
			@JoinColumn(name = "userId") })
	public List<Anlage> getAnlagen() {
		return anlagen;
	}

	public void setAnlagen(List<Anlage> anlagen) {
		this.anlagen = anlagen;
	}

	public BooleanProperty activeProperty() {
		return this.active;
	}

	@Transient
	public boolean isActive() {
		return this.activeProperty().get();
	}

	public void setActive(final boolean active) {
		this.activeProperty().set(active);
	}

	public StringProperty lastNameProperty() {
		return this.lastName;
	}

	public String getLastName() {
		return this.lastNameProperty().get();
	}

	public void setLastName(final String lastName) {
		this.lastNameProperty().set(lastName);
	}

}
