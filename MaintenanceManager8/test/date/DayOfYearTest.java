package date;

import java.util.Calendar;
import java.util.concurrent.TimeUnit;

public class DayOfYearTest {

	public static void main(String[] args) {
		new DayOfYearTest();

	}

	public DayOfYearTest() {

		Calendar cal1 = Calendar.getInstance();
		Calendar cal2 = Calendar.getInstance();

		// Datum addieren
		cal2.add(Calendar.DAY_OF_YEAR, 365);

		System.out.println("Calendar 1: " + cal1.getTime());
		System.out.println("Calendar 2: " + cal2.getTime());

		// Datum Differenz ermitteln
		long daysMillis = cal2.getTimeInMillis() - cal1.getTimeInMillis();
		System.out.println(daysMillis);

		Calendar cal3 = Calendar.getInstance();
		cal3.setTimeInMillis(daysMillis);

		System.out.println("Tage: " + cal2.get(Calendar.DAY_OF_YEAR));

		long days = TimeUnit.MILLISECONDS.toDays(daysMillis);
		System.out.println("Tage: " + days);

	}
}
