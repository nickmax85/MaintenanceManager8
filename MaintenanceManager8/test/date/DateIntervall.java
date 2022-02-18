package date;

import java.time.LocalDate;
import java.time.Month;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Calendar;
import java.util.Date;

public class DateIntervall {

	public static void main(String[] args) {
		new DateIntervall();

	}

	public DateIntervall() {

		// int days = Days.daysBetween(date1, date2).getDays();

		LocalDate today = LocalDate.now();
		LocalDate birthday = LocalDate.of(1960, Month.JANUARY, 1);

		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.YEAR, 1);

		Date date = new Date(cal.getTimeInMillis());

		birthday = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

		long d = ChronoUnit.DAYS.between(today, birthday);
		System.out.println(d);
		//
		// LocalDate l = n
		//
		//
		//
		// long p2 = ChronoUnit.DAYS.between(cal,
		// Calendar.getInstance().getTime());

		System.out.println(cal.getTime());

		int days = 2;

		if (days > 7 && days <= 31) {

			System.out.println("Wochen: " + days / 7);

		}

		if (days > 31) {

			System.out.println("Monat: " + days / 7);

		}

	}

	public static int[] getDays() {

		int[] days = new int[31];

		for (int i = 0; i < 31; i++) {
			days[i] = i + 1;
			System.out.println(days[i]);
		}
		return days;

	}

	public static int[] getMonths() {

		int[] months = new int[12];

		for (int i = 1; i < 12; i++) {
			months[i] = i + 1;
			System.out.println(months[i]);
		}
		return months;

	}

	public static int[] getYears() {

		int[] years = new int[10];

		for (int i = 1; i < 10; i++) {
			years[i] = i + 1;
			System.out.println(years[i]);
		}
		return years;

	}

}
