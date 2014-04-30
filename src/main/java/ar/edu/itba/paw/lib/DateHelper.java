package ar.edu.itba.paw.lib;

import org.joda.time.DateTime;

public class DateHelper {

	public static String getDateString(DateTime date){
		return date.getDayOfMonth() + "/" + date.getMonthOfYear() + "/" + date.getYear();
	}
	
	public static DateTime getDate(String dateString){
		int day = Integer.parseInt(dateString.substring(0, dateString.indexOf("/")));
		// +1 to skip the '/' char
		dateString = dateString.substring(dateString.indexOf("/") + 1);
		int month = Integer.parseInt(dateString.substring(0, dateString.indexOf("/")));
		// +1 to skip the '/' char
		dateString = dateString.substring(dateString.indexOf("/") + 1);
		int year = Integer.parseInt(dateString);

		return getDate(day, month, year);
	}
	
	public static DateTime getDate(int day, int month, int year){
		return new DateTime(year, month, day, 0, 0);
	}
	
	public static DateTime getNormalizedDate(DateTime date){
		return new DateTime(date.getYear(), date.getMonthOfYear(), date.getDayOfMonth(), 0, 0);
	}
	
	
	public static DateTime today(){
		return getNormalizedDate(DateTime.now());
	}
	
}
