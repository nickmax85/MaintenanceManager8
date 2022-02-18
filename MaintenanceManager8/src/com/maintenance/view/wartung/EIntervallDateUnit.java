package com.maintenance.view.wartung;

public enum EIntervallDateUnit {

	DEFAULT("keine Auswahl", 0), DAY("Tag(e)", 1), WEEK("Woche(n)", 7), MONTH("Monat(e)", 31), YEAR("Jahr(e)", 365);

	private String label;
	private int period;

	EIntervallDateUnit(String label, int period) {
		this.label = label;
		this.period = period;
	}

	public int getPeriod() {
		return period;
	}

	@Override
	public String toString() {
		return label;
	}

}
