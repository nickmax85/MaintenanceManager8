package com.maintenance.db.dto;

import java.sql.Timestamp;

import com.maintenance.model.User;

public class AnlageUser {

	private int anlageId;
	private int userId;

	private Timestamp timestamp;
	private String user;

	private Anlage anlage;
	private User benutzer;

	public Anlage getAnlage() {
		return anlage;
	}

	public int getAnlageId() {
		return anlageId;
	}

	public User getBenutzer() {
		return benutzer;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getUser() {
		return user;
	}

	public int getUserId() {
		return userId;
	}

	public void setAnlage(Anlage anlage) {
		this.anlage = anlage;
	}

	public void setAnlageId(int anlageId) {
		this.anlageId = anlageId;
	}

	public void setBenutzer(User benutzer) {
		this.benutzer = benutzer;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

}
