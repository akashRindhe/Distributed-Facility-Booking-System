package client.utility;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import shared.DayOfWeek;

public class TimestampGenerator {
	
	public static Timestamp generateQueryDate(String dayOfWeek) {
		dayOfWeek=dayOfWeek.toUpperCase();
		Date todayDate= new java.util.Date();
		Calendar calendarToday = Calendar.getInstance();
		
		calendarToday.setTime(todayDate);
		int dateOfMonthFinal, monthFinal, yearFinal;
		int daysInYear;
		int dayOfMonthToday, monthToday, yearToday, dayOfWeekToday, offsetDays;
		dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
		monthToday = calendarToday.get(Calendar.MONTH)+1;
		yearToday = calendarToday.get(Calendar.YEAR);
		dayOfWeekToday = calendarToday.get(Calendar.DAY_OF_WEEK);
		
		dateOfMonthFinal = dayOfMonthToday;
		monthFinal = monthToday;
		yearFinal = yearToday;
		offsetDays = DayOfWeek.valueOf(dayOfWeek).getValue() - dayOfWeekToday;
		
		if (offsetDays < 0)
			offsetDays = 7 + offsetDays;
		
		daysInYear= 365;
		
		if ((yearToday % 4) == 0)
			daysInYear = 366;
		
		switch (monthToday) {
			case 2: if ((yearToday % 4) == 0) {
						if (dayOfMonthToday+offsetDays > 29) {
							dateOfMonthFinal = offsetDays - (29 - dayOfMonthToday);
							monthFinal += 1;
						}
						else
							dateOfMonthFinal = dayOfMonthToday + offsetDays;
					}
					else {
						if (dayOfMonthToday+offsetDays > 28) {
							dateOfMonthFinal = offsetDays - (28 - dayOfMonthToday);
							monthFinal += 1;
						}
						else
							dateOfMonthFinal = dayOfMonthToday + offsetDays;
					}
					break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:if (dayOfMonthToday+offsetDays > 31) {
						dateOfMonthFinal = offsetDays - (31 - dayOfMonthToday);
						monthFinal += 1;
					}
					else
						dateOfMonthFinal = dayOfMonthToday + offsetDays;
					break;
					
			case 4:
			case 6:
			case 9:
			case 11:if (dayOfMonthToday+offsetDays > 30) {
						dateOfMonthFinal = offsetDays - (30 - dayOfMonthToday);
						monthFinal += 1;
					}
					else
						dateOfMonthFinal = dayOfMonthToday + offsetDays;
					break;	
		}
		
		if (calendarToday.get(Calendar.DAY_OF_YEAR) == daysInYear && dateOfMonthFinal != dayOfMonthToday)
			yearFinal+=1;
		
		Date dateFinal;
		Timestamp timestamp;
		Calendar calendarFinal;
		calendarFinal = Calendar.getInstance();
		calendarFinal.set(Calendar.DAY_OF_MONTH, dateOfMonthFinal);
		calendarFinal.set(Calendar.MONTH, monthFinal-1);
		calendarFinal.set(Calendar.YEAR, yearFinal);
		calendarFinal.set(Calendar.HOUR_OF_DAY, 0);
		calendarFinal.set(Calendar.MINUTE, 0);
		calendarFinal.set(Calendar.SECOND, 0);
		calendarFinal.set(Calendar.MILLISECOND, 0);
		dateFinal = calendarFinal.getTime();
		timestamp = new Timestamp(dateFinal.getTime());
		return timestamp;
	}
	
	public static List<Timestamp> generateBookingTimestamp(String dayOfWeek, LocalTime timeStart, LocalTime timeEnd) {
		List<Timestamp> bookingTimeStamps = new ArrayList<Timestamp>(2);
		dayOfWeek=dayOfWeek.toUpperCase();
		Date todayDate= new java.util.Date();
		Calendar calendarToday = Calendar.getInstance();
		
		calendarToday.setTime(todayDate);
		int dateOfMonthFinal, monthFinal, yearFinal;
		int daysInYear;
		int dayOfMonthToday, monthToday, yearToday, dayOfWeekToday, offsetDays;
		dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
		monthToday = calendarToday.get(Calendar.MONTH)+1;
		yearToday = calendarToday.get(Calendar.YEAR);
		dayOfWeekToday = calendarToday.get(Calendar.DAY_OF_WEEK);
		
		dateOfMonthFinal = dayOfMonthToday;
		monthFinal = monthToday;
		yearFinal = yearToday;
		offsetDays = DayOfWeek.valueOf(dayOfWeek).getValue() - dayOfWeekToday;
		
		if (offsetDays < 0)
			offsetDays = 7 + offsetDays;
		
		daysInYear= 365;
		
		if ((yearToday % 4) == 0)
			daysInYear = 366;
		
		switch (monthToday) {
			case 2: if ((yearToday % 4) == 0) {
						if (dayOfMonthToday+offsetDays > 29) {
							dateOfMonthFinal = offsetDays - (29 - dayOfMonthToday);
							monthFinal += 1;
						}
						else
							dateOfMonthFinal = dayOfMonthToday + offsetDays;
					}
					else {
						if (dayOfMonthToday+offsetDays > 28) {
							dateOfMonthFinal = offsetDays - (28 - dayOfMonthToday);
							monthFinal += 1;
						}
						else
							dateOfMonthFinal = dayOfMonthToday + offsetDays;
					}
					break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:if (dayOfMonthToday+offsetDays > 31) {
						dateOfMonthFinal = offsetDays - (31 - dayOfMonthToday);
						monthFinal += 1;
					}
					else
						dateOfMonthFinal = dayOfMonthToday + offsetDays;
					break;
					
			case 4:
			case 6:
			case 9:
			case 11:if (dayOfMonthToday+offsetDays > 30) {
						dateOfMonthFinal = offsetDays - (30 - dayOfMonthToday);
						monthFinal += 1;
					}
					else
						dateOfMonthFinal = dayOfMonthToday + offsetDays;
					break;	
		}
		
		if (calendarToday.get(Calendar.DAY_OF_YEAR) == daysInYear && dateOfMonthFinal != dayOfMonthToday)
			yearFinal+=1;
		
		Date dateFinal;
		Timestamp timestamp;
		Calendar calendarFinal;
		calendarFinal = Calendar.getInstance();
		calendarFinal.set(Calendar.DAY_OF_MONTH, dateOfMonthFinal);
		calendarFinal.set(Calendar.MONTH, monthFinal-1);
		calendarFinal.set(Calendar.YEAR, yearFinal);
		if (time == null) {
			calendarFinal.set(Calendar.HOUR_OF_DAY, 0);
			calendarFinal.set(Calendar.MINUTE, 0);
		}
		else {
			calendarFinal.set(Calendar.HOUR_OF_DAY, time.getHour());
			calendarFinal.set(Calendar.MINUTE, time.getMinute());
		}
		calendarFinal.set(Calendar.SECOND, 0);
		calendarFinal.set(Calendar.MILLISECOND, 0);
		dateFinal = calendarFinal.getTime();
		timestamp = new Timestamp(dateFinal.getTime());
		
		return bookingTimeStamps;
	}
	
	public static void main(String[] args) {
		Timestamp t1 = TimestampGenerator.generateQueryDate("Monday");
		System.out.println(t1.toString());
	}
}
