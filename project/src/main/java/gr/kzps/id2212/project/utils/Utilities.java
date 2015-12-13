package gr.kzps.id2212.project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Utilities {
	private DateFormat formatter;
	
	public Utilities() {
		formatter = new SimpleDateFormat("yyyy-MM-dd'T'");
	}
	
	public Date parseDate(String date) {
		try {
			Date parsedDate = formatter.parse(date);
			
			return parsedDate;
		} catch (ParseException ex) {
			ex.printStackTrace();
			return null;
		}
	}
}
