package com.maintenance.util;

import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

import com.maintenance.db.dto.Anlage;
import com.maintenance.db.dto.Station;
import com.maintenance.db.dto.Wartung.EWartungArt;
import com.maintenance.view.wartung.EIntervallDateUnit;

public class ProzentCalc {

	private static final Logger logger = Logger.getLogger(ProzentCalc.class);

	public static float calcProzent(Anlage anlage) {

		float prozent = 0;
		float prozentUeberproduktion = 0;
		int produziert;
		int ueberproduktion;

		produziert = anlage.getAktuelleStueck() - anlage.getLastWartungStueckzahl();

		prozent = (float) 100 * produziert / anlage.getWartungStueckIntervall();

		ueberproduktion = produziert - anlage.getWartungStueckIntervall();
		prozentUeberproduktion = (float) 100 * ueberproduktion / anlage.getWartungStueckIntervall();

		if (prozent >= 100) {
			prozent = 100 + prozentUeberproduktion;
		}

		if (prozent >= 200) {
			prozent = 200 + prozentUeberproduktion;
		}

		// if (anlage.getName().contains("ATC 350"))
		// logger.info(prozent);

		// logger.info(anlage.getName() + ": " + prozent);

		// if (prozent > 100.0f)
		// prozent = 100.0f;
		//
		if (prozent < 0.0f)
			prozent = 0.0f;

		return Math.round(prozent);
	}

	public static float calcProzent(Station station) {

		float prozent = 0;
		float prozentUeberproduktion = 0;
		int produziert;
		int ueberproduktion;

		produziert = station.getAnlage().getAktuelleStueck() - station.getLastWartungStueckzahl();

		prozent = (float) 100 * produziert / station.getWartungStueckIntervall();

		ueberproduktion = produziert - station.getWartungStueckIntervall();
		prozentUeberproduktion = (float) 100 * ueberproduktion / station.getWartungStueckIntervall();

		if (prozent >= 100) {
			prozent = 100 + prozentUeberproduktion;
		}

		if (prozent >= 200) {
			prozent = 200 + prozentUeberproduktion;
		}

		// if (prozent > 100.0f)
		// prozent = 100.0f;
		//
		if (prozent < 0.0f)
			prozent = 0.0f;

		return Math.round(prozent);
	}

	public static float calcProzent(com.maintenance.model.Station station) {

		float prozent = 0;
		float prozentUeberproduktion = 0;
		int produziert;
		int ueberproduktion;

		produziert = station.getAnlage().getAktuelleStueck() - station.getLastWartungStueck();

		prozent = (float) 100 * produziert / station.getWartungStueckIntervall();

		ueberproduktion = produziert - station.getWartungStueckIntervall();
		prozentUeberproduktion = (float) 100 * ueberproduktion / station.getWartungStueckIntervall();

		if (prozent >= 100) {
			prozent = 100 + prozentUeberproduktion;
		}

		if (prozent >= 200) {
			prozent = 200 + prozentUeberproduktion;
		}

		// if (prozent > 100.0f)
		// prozent = 100.0f;
		//
		if (prozent < 0.0f)
			prozent = 0.0f;

		return Math.round(prozent);
	}

	public static float calcProzent(long lastWartung, long nextWartung) {

		float prozent = 0;
		long diff;
		long act;

		diff = nextWartung - lastWartung;
		act = Calendar.getInstance().getTimeInMillis();

		long d = nextWartung - act;

		float d2 = d;
		float diff2 = diff;

		if (diff != 0)
			prozent = 100 * d2 / diff2;

		if (prozent > 100.0f)
			prozent = 100.0f;

		if (prozent < 0.0f)
			prozent = 0.0f;

		return Math.round((100 - prozent) * 100) / 100.0f;

	}

	public static Date calcNextWartungDate(Date lastWartungDate, int dateUnit, int intervall) {

		long intervallMillis;

		Date nextWartungDate = new Date();

		Calendar calLastWartung = Calendar.getInstance();
		Calendar calNextWartung = Calendar.getInstance();

		calLastWartung.setTime(lastWartungDate);
		calNextWartung.setTime(calLastWartung.getTime());

		if (dateUnit == EIntervallDateUnit.DAY.ordinal()) {
			calNextWartung.add(Calendar.DAY_OF_YEAR, intervall);
		}

		if (dateUnit == EIntervallDateUnit.WEEK.ordinal()) {
			calNextWartung.add(Calendar.WEEK_OF_YEAR, intervall);
		}

		if (dateUnit == EIntervallDateUnit.MONTH.ordinal()) {
			calNextWartung.add(Calendar.MONTH, intervall);
		}

		if (dateUnit == EIntervallDateUnit.YEAR.ordinal()) {
			calNextWartung.add(Calendar.YEAR, intervall);
		}

		intervallMillis = calNextWartung.getTimeInMillis() - calLastWartung.getTimeInMillis();
		nextWartungDate.setTime(calLastWartung.getTime().getTime() + intervallMillis);

		return nextWartungDate;

	}

	public static Date calcNextCalendarWartungDate(Date lastWartungDate, int intervall) {

		long intervallMillis;

		Date nextWartungDate = new Date();

		Calendar calLastWartung = Calendar.getInstance();
		Calendar calNextWartung = Calendar.getInstance();

		calLastWartung.setTime(lastWartungDate);
		calNextWartung.setTime(calLastWartung.getTime());

		intervallMillis = calNextWartung.getTimeInMillis() - calLastWartung.getTimeInMillis();
		nextWartungDate.setTime(calLastWartung.getTime().getTime() + intervallMillis);

		return nextWartungDate;

	}

	public static Date calcNextCalendarWarnungDate(Date lastWartungDate, Date nextWartungDate, int intervall) {

		Date nextWarnungDate = new Date();
		Calendar calIntervall = Calendar.getInstance();

		calIntervall.setTimeInMillis(nextWartungDate.getTime());

		nextWarnungDate.setTime(calIntervall.getTimeInMillis());

		return nextWarnungDate;

	}

	public static Date calcNextWarnungDate(int dateUnit, Date lastWartungDate, Date nextWartungDate, int intervall) {

		Date nextWarnungDate = new Date();
		Calendar calIntervall = Calendar.getInstance();

		calIntervall.setTimeInMillis(nextWartungDate.getTime());

		if (dateUnit == EIntervallDateUnit.DAY.ordinal()) {
			calIntervall.add(Calendar.DAY_OF_YEAR, -intervall);
		}

		if (dateUnit == EIntervallDateUnit.WEEK.ordinal()) {
			calIntervall.add(Calendar.WEEK_OF_YEAR, -intervall);
		}

		if (dateUnit == EIntervallDateUnit.MONTH.ordinal()) {
			calIntervall.add(Calendar.MONTH, -intervall);
		}

		if (dateUnit == EIntervallDateUnit.YEAR.ordinal()) {
			calIntervall.add(Calendar.YEAR, -intervall);
		}

		nextWarnungDate.setTime(calIntervall.getTimeInMillis());

		return nextWarnungDate;

	}

	public static Station getMaxIntervallStationDate(List<Station> stationen) {

		int i = 0;
		long minTimeDiff = 0;

		Station maxStation = null;

		for (Station station : stationen) {

			if (station.isStatus() && !station.isTpm() && !station.isRobot()) {

				if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					//
					// if (station.getAnlage().getName().contains("ATC Wellenzelle HART Fertigung"))
					// {
					// logger.info("station: " + station.getName());
					//
					// }

					// Initialisieren
					if (i == 0) {
						maxStation = station;
						minTimeDiff = calcMinTimeDiff(station);
					}

					// Minimalwert suchen
					if (i > 0) {
						long diff = calcMinTimeDiff(station);

						if (diff < minTimeDiff) {
							// if (calcMinTimeDiff(station) >= 0) {
							maxStation = station;
							minTimeDiff = diff;
							// }
						}

					}

					// if (station.getAnlage().getName().contains("ATC Wellenzelle HART Fertigung"))
					// {
					// logger.info("minTimeDiff: " + minTimeDiff);
					// logger.info("maxStation: " + maxStation.getName());
					// logger.info("");
					// }

					i++;

				}

			}

		}

		// if (anlage.getName().contains("Rundkneten"))
		// logger.info(maxStation.getName());

		// if (maxStation.getAnlage().getName().contains("ATC Wellenzelle HART
		// Fertigung")) {
		// logger.info("MaxStation: " + maxStation.getName());
		// logger.info("========================================");
		//
		// }

		return maxStation;

	}

	public static Station getMaxIntervallStationStueck(List<Station> stationen) {

		int i = 0;

		float maxProzent = 0;

		Station maxStation = null;

		for (Station station : stationen) {

			if (station.isStatus() && !station.isTpm() && !station.isRobot()) {

				if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal()) {
					// Initialisieren
					if (i == 0) {
						maxStation = station;
						maxProzent = ProzentCalc.calcProzent(station);
					}

					// Maximalwert suchen
					if (ProzentCalc.calcProzent(station) > maxProzent) {
						maxStation = station;
						maxProzent = ProzentCalc.calcProzent(station);
					}
					i++;
				}

			}

		}
		return maxStation;

	}

	public static boolean isStationWarning(List<Station> stationen) {

		for (Station station : stationen) {

			if (station.isStatus() && !station.isTpm()) {

				float prozent = ProzentCalc.calcProzent(station);

				if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())
					if (prozent >= station.getWartungStueckWarnung() && prozent < station.getWartungStueckFehler()) {
						return true;
					}

				if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
					Date nextWarnungDate = null;
					Date nextWartungDate;
					Date lastWartungDate;

					if (station.getLastWartungDate() != null)
						lastWartungDate = station.getLastWartungDate();
					else
						lastWartungDate = station.getCreateDate();

					nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate, station.getIntervallDateUnit(),
							station.getWartungDateIntervall());

					nextWarnungDate = ProzentCalc.calcNextWarnungDate(station.getWarnungDateUnit(), lastWartungDate,
							nextWartungDate, station.getWartungDateWarnung());

					if (Calendar.getInstance().getTime().after(nextWarnungDate)
							&& Calendar.getInstance().getTime().before(nextWartungDate)) {
						return true;
					}

				}

			}

		}

		return false;

	}

	public static boolean isTPMStationWarning(List<Station> stationen) {

		for (Station station : stationen) {

			if (station.isTpm())
				if (station.isStatus()) {

					float prozent = ProzentCalc.calcProzent(station);

					if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())
						if (prozent >= station.getWartungStueckWarnung()
								&& prozent < station.getWartungStueckFehler()) {
							return true;
						}

					if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {
						Date nextWarnungDate = null;
						Date nextWartungDate;
						Date lastWartungDate;

						if (station.getLastWartungDate() != null)
							lastWartungDate = station.getLastWartungDate();
						else
							lastWartungDate = station.getCreateDate();

						nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate,
								station.getIntervallDateUnit(), station.getWartungDateIntervall());

						nextWarnungDate = ProzentCalc.calcNextWarnungDate(station.getWarnungDateUnit(), lastWartungDate,
								nextWartungDate, station.getWartungDateWarnung());

						if (Calendar.getInstance().getTime().after(nextWarnungDate)
								&& Calendar.getInstance().getTime().before(nextWartungDate)) {
							return true;
						}

					}

				}

		}

		return false;

	}

	public static boolean isStationFehler(List<Station> stationen) {

		for (Station station : stationen) {

			if (station.isStatus() && !station.isTpm()) {

				float prozent = ProzentCalc.calcProzent(station);

				if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())
					if (prozent >= station.getWartungStueckFehler()) {
						return true;
					}

				if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

					Date nextWartungDate;
					Date lastWartungDate;

					if (station.getLastWartungDate() != null)
						lastWartungDate = station.getLastWartungDate();
					else
						lastWartungDate = station.getCreateDate();

					nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate, station.getIntervallDateUnit(),
							station.getWartungDateIntervall());

					if (Calendar.getInstance().getTime().after(nextWartungDate)) {
						return true;
					}

				}

			}

		}

		return false;

	}

	public static int calcNextWartungStueck(Station station) {

		int produziert;

		produziert = (station.getAnlage().getAktuelleStueck() - station.getLastWartungStueckzahl());

		return station.getWartungStueckIntervall() - produziert;

	}

	public static boolean isTPMStationFehler(List<Station> stationen) {

		for (Station station : stationen) {

			if (station.isTpm())
				if (station.isStatus()) {

					float prozent = ProzentCalc.calcProzent(station);

					if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())
						if (prozent >= station.getWartungStueckFehler()) {
							return true;
						}

					if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

						Date nextWartungDate;
						Date lastWartungDate;

						if (station.getLastWartungDate() != null)
							lastWartungDate = station.getLastWartungDate();
						else
							lastWartungDate = station.getCreateDate();

						nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate,
								station.getIntervallDateUnit(), station.getWartungDateIntervall());

						if (Calendar.getInstance().getTime().after(nextWartungDate)) {
							return true;
						}

					}

				}

		}

		return false;

	}

	public static boolean isRobotStationFehler(List<Station> stationen) {

		for (Station station : stationen) {

			if (station.isRobot())
				if (station.isStatus()) {

					float prozent = ProzentCalc.calcProzent(station);

					if (station.getWartungArt() == EWartungArt.STUECKZAHL.ordinal())
						if (prozent >= station.getWartungStueckFehler()) {
							return true;
						}

					if (station.getWartungArt() == EWartungArt.TIME_INTERVALL.ordinal()) {

						Date nextWartungDate;
						Date lastWartungDate;

						if (station.getLastWartungDate() != null)
							lastWartungDate = station.getLastWartungDate();
						else
							lastWartungDate = station.getCreateDate();

						nextWartungDate = ProzentCalc.calcNextWartungDate(lastWartungDate,
								station.getIntervallDateUnit(), station.getWartungDateIntervall());

						if (Calendar.getInstance().getTime().after(nextWartungDate)) {
							return true;
						}

					}

				}

		}

		return false;

	}

	private static long calcMinTimeDiff(Station station) {

		long timeDiff = TimeUnit.DAYS.toMillis(500);

		Calendar calLastWartung = Calendar.getInstance();
		if (station.getLastWartungDate() != null)
			calLastWartung.setTime(station.getLastWartungDate());
		else
			calLastWartung.setTime(station.getCreateDate());

		if (station.getIntervallDateUnit() == EIntervallDateUnit.DAY.ordinal()) {
			calLastWartung.add(Calendar.DAY_OF_YEAR, station.getWartungDateIntervall());

		}
		if (station.getIntervallDateUnit() == EIntervallDateUnit.WEEK.ordinal()) {
			calLastWartung.add(Calendar.WEEK_OF_YEAR, station.getWartungDateIntervall());

		}
		if (station.getIntervallDateUnit() == EIntervallDateUnit.MONTH.ordinal()) {
			calLastWartung.add(Calendar.MONTH, station.getWartungDateIntervall());

		}
		if (station.getIntervallDateUnit() == EIntervallDateUnit.YEAR.ordinal()) {
			calLastWartung.add(Calendar.YEAR, station.getWartungDateIntervall());

		}

		Date dateIntervall = new Date();
		dateIntervall.setTime(calLastWartung.getTime().getTime());

		timeDiff = dateIntervall.getTime() - Calendar.getInstance().getTimeInMillis();

		return timeDiff;

	}

}
