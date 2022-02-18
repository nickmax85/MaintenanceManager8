package db;

import com.maintenance.db.service.Service;

public class DBTest {

	public static void main(String[] args) {
		new DBTest();

	}

	public DBTest() {

		System.out.println(Service.getInstance().getAllCalendarWartung());

	}

}
