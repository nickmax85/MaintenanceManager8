package date;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.concurrent.TimeUnit;

public class DateTest {

	public static void main(String[] args) {
		new DateTest();

	}

	public DateTest() {

		long millis = TimeUnit.DAYS.toMillis(2);

		Date dt1 = Calendar.getInstance().getTime();
		SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df.format(dt1.getTime()));
		System.out.println(dt1);
		System.out.println();

		Date dt2 = Calendar.getInstance().getTime();
		SimpleDateFormat df2 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df2.format(dt2.getTime()));
		System.out.println(dt2);

		System.out.println();

		Date dt3 = Calendar.getInstance().getTime();
		dt3.setTime(dt2.getTime() - millis);

		SimpleDateFormat df3 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		System.out.println(df3.format(dt3.getTime()));
		System.out.println(dt3);

		String text = null;
		text += "Nächste Wartung ist fällig am : " + df.format(dt3);
		text += "\n";
		text += "Diese Nachricht wurde an folgende Adressen versendet: " + "13453542546";
		
		System.out.println(text);

	}

	private void printDifference(Calendar today, Calendar past) {

		long difference = today.getTimeInMillis() - past.getTimeInMillis();
		int days = (int) (difference / (1000 * 60 * 60 * 24));
		int hours = (int) (difference / (1000 * 60 * 60) % 24);
		int minutes = (int) (difference / (1000 * 60) % 60);
		int seconds = (int) (difference / 1000 % 60);
		int millis = (int) (difference % 1000);
		System.out.println("Difference: " + days + " days, " + hours + " hours, " + minutes + " minutes, " + seconds
				+ " seconds and " + millis + " milliseonds");
	}

}
