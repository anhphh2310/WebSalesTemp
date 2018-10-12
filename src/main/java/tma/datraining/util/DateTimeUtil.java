package tma.datraining.util;

import org.joda.time.DateTime;
import org.joda.time.DateTimeZone;

public class DateTimeUtil {

	private static final DateTimeZone TIMEZONE = DateTimeZone.forID("Asia/Saigon");
	
	public static DateTime getCurrent() {
		return DateTime.now().withZone(TIMEZONE);
	}
	
	public static int getQuarter(DateTime dateTime) {
		dateTime.withZone(TIMEZONE);
		return (dateTime.getMonthOfYear()/3)+1;
	}
	
	public static int getMonth(DateTime dateTime) {
		dateTime.withZone(TIMEZONE);
		return dateTime.getMonthOfYear();
	}
	
	public static int getYear(DateTime dateTime) {
		dateTime.withZone(TIMEZONE);
		return dateTime.getYear();
	}
	
}
