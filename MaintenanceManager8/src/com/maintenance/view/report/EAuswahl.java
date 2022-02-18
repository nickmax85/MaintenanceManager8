package com.maintenance.view.report;

public enum EAuswahl {

	ANLAGEN("Anlagen"), STATIONEN("Stationen");

	private String label;

	EAuswahl(String label) {
		this.label = label;
	}

	@Override
	public String toString() {
		return label;
	}

}
