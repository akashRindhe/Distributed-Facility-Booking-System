package client;

import client.Console;
import client.Controller.*;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Scanner;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import server.Server;
import shared.DayOfWeek;
import shared.model.Booking;
import shared.service.MarshallingService;
import shared.webservice.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		Console console = new Console();
		OptionParser optParser = console.getParser();
		OptionSet optSet = optParser.parse(args);
		int port = (int) optSet.valueOf("port");
		String ip = (String) optSet.valueOf("ip");
		final Client client = new Client(port, InetAddress.getByName(ip));
		MarshallingService marshallService = MarshallingService.getInstance();
		byte[] marshalledRequest, marshalledResponse;
		
		try {
			optParser.parse(args);
			Scanner sc = console.getScanner();
			Controller controller = new Controller();
			int choice;
			do {
				console.displayMenu();
				choice = sc.nextInt();
				String facilityName;
				switch (choice) 
				{
					case 1: System.out.print("Enter facility name: ");
							facilityName = sc.next();
							System.out.print("Enter number of days (not more than 7) : ");
							int length = sc.nextInt();
							if (length > 7) {
								System.out.println("Number of days can not be more than 7)");
								System.exit(-1);
							}
							List<Timestamp> listDays = new ArrayList<Timestamp>(length); 
							for (int i=0; i<length; i++) {
								System.out.print("Enter day "+ (i+1) + ": ");
								String queryDay= sc.next().toUpperCase();
								//listDays.add(DayOfWeek.valueOf(queryDay).getValue());
							}
							
							QueryFacilityRequest queryRequest = controller.generateQueryRequest(facilityName, listDays);
							
							marshalledRequest = marshallService.marshal(queryRequest);
							System.out.println(marshalledRequest.getClass());
							System.out.println("Querying for facility " + facilityName);
							// Send this marshalledRequest via UDP to Server
							//marshalledResponse = null;
							// Receive marshalledResponse from Server
							QueryFacilityRequest unmarshalled = marshallService.unmarshal(marshalledRequest, QueryFacilityRequest.class);
							//System.out.println("Unmarshalled:" + unmarshalled.getClass());
							System.out.println("Unmarshalled:" + unmarshalled.getFacilityName());
							
							System.out.println("Unmarshalled:" + unmarshalled.getDays().get(0));
							//System.out.println("Unmarshalled:" + unmarshalled.getStartTimeStamp().toString());
							
							//QueryFacilityResponse queryResponse = marshallService.unmarshal(marshalledResponse, QueryFacilityResponse.class);
							//System.out.println("Unmarshalled:" + queryResponse.getClass());
							//List<Booking> bookings = queryResponse.getBookings();
							
							break;
							
					case 2: System.out.print("Enter facility name: ");
							facilityName = sc.next();
							System.out.print("Enter booking day: ");
							String bookingDay = sc.next();
							System.out.print("Enter start time for booking (HH:mm): ");
							String start = sc.next();
							System.out.print("Enter end time for booking (HH:mm): ");
							String end = sc.next();
							DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("HH:mm");
							LocalTime startTime = LocalTime.from(timeFormatter.parse(start));
							LocalTime endTime = LocalTime.from(timeFormatter.parse(end));
							int compare = endTime.compareTo(startTime);
							
							if (compare < 0) {
								System.err.println("End time earlier than Start Time. Please provide valid inputs");
								System.exit(-1);
							}
							if ((startTime.getMinute() != 0 && startTime.getMinute() != 30) || (endTime.getMinute() != 0 && endTime.getMinute() != 30)) {
								System.err.println("Booking available for 30 min intervals. Please provide valid inputs");
								System.exit(-1);
							}
							
							// From the input day, get date of the booking
							// From the input times, get start and end time for the booking
							// Set timestamp for the whole booking time
							Date todayDate= new java.util.Date();
							Calendar calendarToday = Calendar.getInstance();
							calendarToday.setTime(todayDate);
							int dateToday = calendarToday.get(Calendar.DAY_OF_MONTH);
							int monthToday = calendarToday.get(Calendar.MONTH)+1;
							int yearToday = calendarToday.get(Calendar.YEAR);
							int dayToday = calendarToday.get(Calendar.DAY_OF_WEEK);
							bookingDay=bookingDay.toUpperCase();
							int bookingDate = dateToday, bookingMonth = monthToday, bookingYear = yearToday;
							int offsetDays = DayOfWeek.valueOf(bookingDay).getValue()-dayToday;
							if (offsetDays < 0)
								offsetDays = 7 + offsetDays;
							int daysInYear= 365;
							if ((yearToday % 4) == 0)
								daysInYear = 366;
							switch (monthToday) {
								case 2: if ((yearToday % 4) == 0) {
											if (dateToday+offsetDays > 29) {
												bookingDate = offsetDays - (29 - dateToday);
												bookingMonth += 1;
											}
											else
												bookingDate = dateToday + offsetDays;
										}
										else {
											if (dateToday+offsetDays > 28) {
												bookingDate = offsetDays - (28 - dateToday);
												bookingMonth += 1;
											}
											else
												bookingDate = dateToday + offsetDays;
										}
										break;
								case 1:
								case 3:
								case 5:
								case 7:
								case 8:
								case 10:
								case 12:if (dateToday+offsetDays > 31) {
											bookingDate = offsetDays - (31 - dateToday);
											bookingMonth += 1;
										}
										else
											bookingDate = dateToday + offsetDays;
										break;
										
								case 4:
								case 6:
								case 9:
								case 11:if (dateToday+offsetDays > 30) {
											bookingDate = offsetDays - (30 - dateToday);
											bookingMonth += 1;
										}
										else
											bookingDate = dateToday + offsetDays;
										break;
								
							}
							
							if (calendarToday.get(Calendar.DAY_OF_YEAR) == daysInYear && bookingDate != dateToday)
								bookingYear+=1;
							
							Date bookingStartDate, bookingEndDate;
							Timestamp startTimeStamp, endTimeStamp;
							Calendar calendarBooking;
							calendarBooking = Calendar.getInstance();
							calendarBooking.set(Calendar.DAY_OF_MONTH, bookingDate);
							calendarBooking.set(Calendar.MONTH, bookingMonth-1);
							calendarBooking.set(Calendar.YEAR, bookingYear);
							calendarBooking.set(Calendar.HOUR_OF_DAY, startTime.getHour());
							calendarBooking.set(Calendar.MINUTE, startTime.getMinute());
							calendarBooking.set(Calendar.SECOND, 0);
							calendarBooking.set(Calendar.MILLISECOND, 0);
							bookingStartDate = calendarBooking.getTime();
							if (bookingDate == dateToday) {
								LocalTime nowTime = LocalTime.now(); 
								if (nowTime.compareTo(startTime) > 0 || nowTime.compareTo(endTime) > 0) {
									System.err.println("Entered time is in the past. Please provide booking time in the future...");
									System.exit(-1);
								}
							}
							startTimeStamp = new Timestamp(bookingStartDate.getTime());
							
							calendarBooking.set(Calendar.HOUR_OF_DAY, endTime.getHour());
							calendarBooking.set(Calendar.MINUTE, endTime.getMinute());
							bookingEndDate = calendarBooking.getTime();
							endTimeStamp = new Timestamp(bookingEndDate.getTime());
							
							System.out.println("Start: " + startTimeStamp.toString());
							System.out.println("End: " + endTimeStamp.toString());
							
							//Generate BookFacility request object
							BookFacilityRequest bookingRequest = controller.generateBookingRequest(facilityName, startTimeStamp, endTimeStamp );
					
							marshalledRequest = marshallService.marshal(bookingRequest);
							//System.out.println(marshalledRequest.getClass());
							System.out.println("Booking facility " + facilityName + " on " + bookingDay + ", " + bookingDate +"/" + bookingMonth + "/" + bookingYear +  " from " + start + " to " + end);
							// Send this marshalledRequest via UDP to Server
							//marshalledResponse = null;
							// Receive marshalledResponse from Server
							BookFacilityRequest unmarshalle1d = marshallService.unmarshal(marshalledRequest, BookFacilityRequest.class);
							System.out.println("Unmarshalled:" + unmarshalle1d.getClass());
							System.out.println("Unmarshalled:" + unmarshalle1d.getFacilityName());
							System.out.println("Unmarshalled:" + unmarshalle1d.getEndTimeStamp().toString());
							System.out.println("Unmarshalled:" + unmarshalle1d.getStartTimeStamp().toString());
							
							//BookFacilityResponse bookResponse = marshallService.unmarshal(marshalledResponse, BookFacilityResponse.class);
							//System.out.println("Unmarshalled:" + bookResponse.getClass());
							//int confirmationId = bookResponse.getConfirmationId();
							//System.out.println("Booking successful. Confirmation ID: " + confirmationId);
							break;
							
					case 3: System.out.print("Enter booking name: ");
							int bookingId = sc.nextInt();
							System.out.print("Enter offset for change (in minutes): ");
							int offset = sc.nextInt();
							
							//Generate ChangeFacility request object
							ChangeBookingRequest changeRequest = controller.generateChangeRequest(bookingId, offset);
							
							marshalledRequest = marshallService.marshal(changeRequest);
							//System.out.println(marshalledRequest.getClass());
							System.out.println("Changing booking " + bookingId + " by " + offset + " minutes");
							// Send this marshalledRequest via UDP to Server
							
							// Receive marshalledResponse from Server
							marshalledResponse = null;
							
							ChangeBookingResponse changeResponse = marshallService.unmarshal(marshalledResponse, ChangeBookingResponse.class);
							System.out.println("Unmarshalled:" + changeResponse.getClass());
							int successChange = changeResponse.getSuccess(); 
							if (successChange == 1)
								System.out.println("Change booking successful.");
							else 
								System.out.println("Change booking unsuccessful");
							break;
							
					case 4: System.out.print("Enter facility name: ");
							facilityName = sc.next();
							System.out.print("Enter day to monitor: ");
							String monitorDay = sc.next();
							System.out.print("Enter start time for monitoring: ");
							String monitorsStartTime = sc.next();
							System.out.print("Enter end time for monitoring: ");
							String monitorEndTime = sc.next();
							//Generate MonitorFacility request object
							System.out.println("Monitoring facility " + facilityName + " on " + monitorDay + " from " + monitorsStartTime + " to " + monitorEndTime);						
							break;
							
					case 5: System.out.println("Call an idempotent service");
							break;
							
					case 6: System.out.println("Call an non-idempotent service");
							break;
							
					case 7: System.out.println("Exiting client application");
							break;
							
					default:System.out.println("Invalid choice, please enter again...");
				}
			} while (choice!=7);
		}
		catch (Exception e){
			 System.err.println("Caught Exception: " + e.getMessage() + "\n"+e.getStackTrace() + " caused by " + e.getCause());
			 //System.err.println("Please provide valid inputs.");
			 System.exit(-1);
		}		
		finally {
			System.exit(0);
		}
	}

}
