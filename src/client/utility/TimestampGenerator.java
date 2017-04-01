package client.utility;

import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import shared.DayOfWeek;

public class TimestampGenerator {
	
	public static int getDaysInYear(int year) {
		if ((year % 4) == 0)
			return 366;
		return 365;
	}
	
	public static int getBookingDayOfMonth(int dayOfMonthToday, int monthToday, int yearToday, int offsetBookingDay) {
		int bookingDayOfMonth = dayOfMonthToday;
		switch (monthToday) {
			case 2: if ((yearToday % 4) == 0) {
						if (dayOfMonthToday + offsetBookingDay > 29)
							bookingDayOfMonth = offsetBookingDay - (29 - dayOfMonthToday);
						else
							bookingDayOfMonth = dayOfMonthToday + offsetBookingDay;
					}
					else {
						if (dayOfMonthToday+offsetBookingDay > 28) 
							bookingDayOfMonth = offsetBookingDay - (28 - dayOfMonthToday);
						else
							bookingDayOfMonth = dayOfMonthToday + offsetBookingDay;
					}
					break;
			case 1:
			case 3:
			case 5:
			case 7:
			case 8:
			case 10:
			case 12:if (dayOfMonthToday+offsetBookingDay > 31)
						bookingDayOfMonth = offsetBookingDay - (31 - dayOfMonthToday);
					else
						bookingDayOfMonth = dayOfMonthToday + offsetBookingDay;
					break;
					
			case 4:
			case 6:
			case 9:
			case 11:if (dayOfMonthToday+offsetBookingDay > 30)
						bookingDayOfMonth = offsetBookingDay - (30 - dayOfMonthToday);
					else
						bookingDayOfMonth = dayOfMonthToday + offsetBookingDay;
					break;	
		}
		return bookingDayOfMonth;
	}
	
	public static Date getDateFinal (int dayOfMonth, int month, int year, 
			int hour, int minute) {
		Calendar calendarFinal = Calendar.getInstance();
		calendarFinal.set(Calendar.DAY_OF_MONTH, dayOfMonth);
		calendarFinal.set(Calendar.MONTH, month);
		calendarFinal.set(Calendar.YEAR, year);
		calendarFinal.set(Calendar.HOUR_OF_DAY, hour);
		calendarFinal.set(Calendar.MINUTE, minute);
		calendarFinal.set(Calendar.SECOND, 0);
		calendarFinal.set(Calendar.MILLISECOND, 0);
		Date dateFinal = calendarFinal.getTime();
		return dateFinal;
	}
	
	/**
	 * 
	 * @param dayOfWeek
	 * @return Timestamp containg Date for the dayOfWeek within 7 days from today
	 */
	public static Timestamp generateDate(String dayOfWeek) {
		int dayOfMonthFinal, monthFinal, yearFinal;
		int dayOfMonthToday, monthToday, yearToday, dayOfWeekToday, offsetDays;
		
		dayOfWeek = dayOfWeek.toUpperCase();
		Date todayDate = new java.util.Date();
		Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTime(todayDate);
		
		dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
		monthToday = calendarToday.get(Calendar.MONTH) + 1;
		yearToday = calendarToday.get(Calendar.YEAR);
		dayOfWeekToday = calendarToday.get(Calendar.DAY_OF_WEEK);
		
		dayOfMonthFinal = dayOfMonthToday;
		monthFinal = monthToday;
		yearFinal = yearToday;
		offsetDays = DayOfWeek.valueOf(dayOfWeek).getValue() - dayOfWeekToday;
		
		if (offsetDays < 0)
			offsetDays = 7 + offsetDays;
		
		dayOfMonthFinal = TimestampGenerator.getBookingDayOfMonth(dayOfMonthToday, monthToday, yearToday, offsetDays);
		
		if (dayOfMonthFinal < dayOfMonthToday) 
			monthFinal += 1;
			
		if (calendarToday.get(Calendar.DAY_OF_YEAR) == TimestampGenerator.getDaysInYear(yearToday) && dayOfMonthFinal != dayOfMonthToday)
			yearFinal += 1;
		
		Date dateFinal = TimestampGenerator.getDateFinal(dayOfMonthFinal, monthFinal-1, yearFinal, 0, 0);
		Timestamp timestamp = new Timestamp(dateFinal.getTime());
		return timestamp;
	}
	
	public static Timestamp generateDateWithTime(String dayOfWeek, int hour, int minutes, int seconds) {
		int dayOfMonthFinal, monthFinal, yearFinal;
		int dayOfMonthToday, monthToday, yearToday, dayOfWeekToday, offsetDays;
		
		dayOfWeek = dayOfWeek.toUpperCase();
		Date todayDate = new java.util.Date();
		Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTime(todayDate);
		
		dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
		monthToday = calendarToday.get(Calendar.MONTH) + 1;
		yearToday = calendarToday.get(Calendar.YEAR);
		dayOfWeekToday = calendarToday.get(Calendar.DAY_OF_WEEK);
		
		dayOfMonthFinal = dayOfMonthToday;
		monthFinal = monthToday;
		yearFinal = yearToday;
		offsetDays = DayOfWeek.valueOf(dayOfWeek).getValue() - dayOfWeekToday;
		
		if (offsetDays < 0)
			offsetDays = 7 + offsetDays;
		
		dayOfMonthFinal = TimestampGenerator.getBookingDayOfMonth(dayOfMonthToday, monthToday, yearToday, offsetDays);
		
		if (dayOfMonthFinal < dayOfMonthToday) 
			monthFinal += 1;
			
		if (calendarToday.get(Calendar.DAY_OF_YEAR) == TimestampGenerator.getDaysInYear(yearToday) && dayOfMonthFinal != dayOfMonthToday)
			yearFinal += 1;
		
		Date dateFinal = TimestampGenerator.getDateFinal(dayOfMonthFinal, monthFinal-1, yearFinal, hour, minutes);
		Timestamp timestamp = new Timestamp(dateFinal.getTime());
		return timestamp;
	}
	
	/**
	 * 
	 * @param dayOfWeek
	 * @param timeStart
	 * @param timeEnd
	 * @return List<Timestamp> containing timestamps corresponding to starting and ending times of the requested booking
	 */
	public static List<Timestamp> generateBookingTimestamp(String dayOfWeek, String start, String end) {
		DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
		LocalTime timeStart = LocalTime.from(timeFormatter.parse(start));
		LocalTime timeEnd = LocalTime.from(timeFormatter.parse(end));
		
		int compare = timeEnd.compareTo(timeStart);
		
		if (compare < 0) {
			System.err.println("End time earlier than Start Time. Please provide valid inputs");
			return null;
		}
		if ((timeStart.getMinute() != 0 && timeStart.getMinute() != 30) || (timeEnd.getMinute() != 0 && timeEnd.getMinute() != 30)) {
			System.err.println("Booking available for 30 min intervals. Please provide valid inputs");
			return null;
		}
		int dayOfMonthFinal, monthFinal, yearFinal;
		int dayOfMonthToday, monthToday, yearToday, dayOfWeekToday, offsetDays;
		
		dayOfWeek = dayOfWeek.toUpperCase();
		Date todayDate = new java.util.Date();
		Calendar calendarToday = Calendar.getInstance();
		calendarToday.setTime(todayDate);
		
		dayOfMonthToday = calendarToday.get(Calendar.DAY_OF_MONTH);
		monthToday = calendarToday.get(Calendar.MONTH) + 1;
		yearToday = calendarToday.get(Calendar.YEAR);
		dayOfWeekToday = calendarToday.get(Calendar.DAY_OF_WEEK);
		
		dayOfMonthFinal = dayOfMonthToday;
		monthFinal = monthToday;
		yearFinal = yearToday;
		offsetDays = DayOfWeek.valueOf(dayOfWeek).getValue() - dayOfWeekToday;
		
		if (offsetDays < 0)
			offsetDays = 7 + offsetDays;
		
		dayOfMonthFinal = TimestampGenerator.getBookingDayOfMonth(dayOfMonthToday, monthToday, yearToday, offsetDays);
		
		if (dayOfMonthFinal < dayOfMonthToday) 
			monthFinal += 1;
			
		if (calendarToday.get(Calendar.DAY_OF_YEAR) == TimestampGenerator.getDaysInYear(yearToday) && dayOfMonthFinal != dayOfMonthToday)
			yearFinal += 1;
		
		if (dayOfMonthFinal == dayOfMonthToday) {
			LocalTime nowTime = LocalTime.now(); 
			if (nowTime.compareTo(timeStart) > 0 || nowTime.compareTo(timeEnd) > 0) {
				System.err.println("Entered time is in the past. Please provide booking time in the future...");
				return null;
			}
		}
		List<Timestamp> bookingTimeStamps = new ArrayList<Timestamp>();
		Date bookingStart = TimestampGenerator.getDateFinal(dayOfMonthFinal, monthFinal-1, yearFinal, timeStart.getHour(), timeStart.getMinute());
		Date bookingEnd = TimestampGenerator.getDateFinal(dayOfMonthFinal, monthFinal-1, yearFinal, timeEnd.getHour(), timeEnd.getMinute());
		Timestamp timestampStart = new Timestamp(bookingStart.getTime());
		Timestamp timestampEnd = new Timestamp(bookingEnd.getTime());
		System.out.println(timestampStart.toString());
		System.out.println(timestampEnd.toString());
		bookingTimeStamps.add(timestampStart);
		bookingTimeStamps.add(timestampEnd);
		return bookingTimeStamps;
	}
	
	public static void main(String[] args) throws NullPointerException {
		Timestamp t1 = TimestampGenerator.generateDate("Thursday");
		System.out.println(t1.toString());
		System.out.print("Enter booking day: ");
		Scanner sc = new Scanner(System.in);
		String bookingDay = sc.next();
		System.out.print("Enter start time for booking (HH:mm): ");
		String start = sc.next();
		System.out.print("Enter end time for booking (HH:mm): ");
		String end = sc.next();
		try {
			List<Timestamp> tS = TimestampGenerator.generateBookingTimestamp(bookingDay, start, end);
			System.out.println("Start: " + tS.get(0));
			System.out.println("End: " + tS.get(1));
		}
		catch (NullPointerException e) {
			e.printStackTrace();
		}
		
	}
}
