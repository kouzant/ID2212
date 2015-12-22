package gr.kzps.id2212.project.utils;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * General utilities
 * @author Antonis Kouzoupis
 *
 */
public class Utilities {
	private DateFormat formatter;

	public Utilities() {
		formatter = new SimpleDateFormat("yyyy-MM-dd'T'");
	}

	/**
	 * Parse ISO-8601 UTC string in Date
	 * 
	 * @param date Date is String
	 * @return Date 
	 * @throws ParseException
	 */
	public Date parseDate(String date) throws ParseException {
		Date parsedDate = formatter.parse(date);

		return parsedDate;
	}
}
