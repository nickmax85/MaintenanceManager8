package com.maintenance.view.wartung;

public enum EWartungStatus {

	NOT_POSSIBLE("nicht moeglich"), DONE("abgeschlossen");

	private String label;

	EWartungStatus(String label) {
		this.label = label;

	}

	@Override
	public String toString() {
		return label;
	}

}
