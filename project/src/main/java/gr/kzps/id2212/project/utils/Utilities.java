package gr.kzps.id2212.project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
	public static Date parseDate(String date) {
		DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd'T'");
		try {
			Date parsedDate = formatter.parse(date);
			
			return parsedDate;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
