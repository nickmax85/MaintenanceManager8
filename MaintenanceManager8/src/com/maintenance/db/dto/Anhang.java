package com.maintenance.db.dto;

import java.io.File;
import java.sql.Timestamp;

public class Anhang {

	private int id;
	private String name;
	private File file;

	private int wartungId;
	private int stationId;
	private int anlageId;

	private Timestamp timestamp;
	private String user;

	public File getFile() {
		return file;
	}

	public int getId() {
		return id;
	}

	public String getName() {
		return name;
	}

	public Timestamp getTimestamp() {
		return timestamp;
	}

	public String getUser() {
		return user;
	}

	public int getWartungId() {
		return wartungId;
	}

	public void setFile(File file) {
		this.file = file;
	}

	public void setId(int id) {
		this.id = id;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setTimestamp(Timestamp timestamp) {
		this.timestamp = timestamp;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public void setWartungId(int wartungId) {
		this.wartungId = wartungId;
	}

	public int getStationId() {
		return stationId;
	}

	public void setStationId(int stationId) {
		this.stationId = stationId;
	}

	public int getAnlageId() {
		return anlageId;
	}

	public void setAnlageId(int anlageId) {
		this.anlageId = anlageId;
	}

	@Override
	public String toString() {
		return name;
	}

}
