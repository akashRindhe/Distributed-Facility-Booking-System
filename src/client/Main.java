package client;

import client.Console;
import client.Controller.*;
import client.utility.TimestampGenerator;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import shared.DayOfWeek;
import shared.model.Booking;
import shared.model.Facility;
import shared.webservice.*;

public class Main {
	
	public static void main(String[] args) throws Exception {
		System.out.println("Client terminal starting...");
		Console console = new Console();
		OptionParser optParser = console.getParser();
		OptionSet optSet = optParser.parse(args);
		int clientPort = (int) optSet.valueOf("clientPort");
		String ip = (String) optSet.valueOf("ip");
		int serverPort = (int) optSet.valueOf("serverPort");
		String userId = (String) optSet.valueOf("userId");
		final Client client = new Client(clientPort, serverPort, InetAddress.getByName(ip));
		System.out.println("Client launched!");
		List<Facility> facilities = new ArrayList<Facility>();
		
		try { 
			optParser.parse(args);
			Scanner sc = console.getScanner();
			Controller controller = new Controller();
			int choice;
			System.out.println("Sending Request");
			GetFacilitiesRequest getFacilityRequest = controller.generateFacilityRequest();
			
			Request request = controller.generateRequest(getFacilityRequest, Type.GET_FACILITIES);
			Response response = new Response();
			
			client.start(request);
			System.out.println("Processed getFacilityResponse");
			
			facilities = client.getFacilities();
			System.out.println("size of facilities: " + facilities.size());
			
			do {
				console.displayMenu();
				choice = sc.nextInt();
				sc.nextLine();
				String facilityName;
				int facilityId;
				Optional<Facility> facility;
				switch (choice)
				{
					case 1: System.out.print("Enter facility name: ");
							facilityName = sc.nextLine();
							facility = facilities.stream().filter(o -> o.getName().equals(facilityName)).findFirst();
							facilityId = facility.get().getId();
							System.out.print("Enter number of days (not more than 7) : ");
							int length = sc.nextInt();
							if (length > 7) {
								System.out.println("Number of days can not be more than 7)");
								System.exit(-1);
							}
							List<Timestamp> listDays = new ArrayList<Timestamp>(length); 
							for (int i=0; i<length; i++) {
								System.out.print("Enter day "+ (i+1) + ": ");
								String queryDayOfWeek = sc.next().toUpperCase();
								Timestamp day = TimestampGenerator.generateQueryDate(queryDayOfWeek);
								listDays.add(day);
							}
							QueryFacilityRequest queryRequest = controller.generateQueryRequest(facilityId, listDays);
							List<Timestamp> times = queryRequest.getDays();
							//for(int i=0;i<times.size();i++)
							//	System.out.println(times.get(i).toString());
							request = controller.generateRequest(queryRequest, Type.QUERY_FACILITY);
							client.sendRequest(request);
							
							System.out.println("Querying for facility " + facilityName);
							
							response = client.receiveResponse();
							client.processQueryFacilityResponse(response);
							
							break;
							
					case 2: System.out.print("Enter facility name: ");
							facilityName = sc.nextLine();
							facility = facilities.stream().filter(o -> o.getName().equals(facilityName)).findFirst();
							facilityId = facility.get().getId();
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
							Calendar calendarBookingStart, calendarBookingEnd;
							calendarBookingStart = Calendar.getInstance();
							calendarBookingStart.set(Calendar.DAY_OF_MONTH, bookingDate);
							calendarBookingStart.set(Calendar.MONTH, bookingMonth-1);
							calendarBookingStart.set(Calendar.YEAR, bookingYear);
							calendarBookingStart.set(Calendar.HOUR_OF_DAY, startTime.getHour());
							calendarBookingStart.set(Calendar.MINUTE, startTime.getMinute());
							calendarBookingStart.set(Calendar.SECOND, 0);
							calendarBookingStart.set(Calendar.MILLISECOND, 0);
							bookingStartDate = calendarBookingStart.getTime();
							if (bookingDate == dateToday) {
								LocalTime nowTime = LocalTime.now(); 
								if (nowTime.compareTo(startTime) > 0 || nowTime.compareTo(endTime) > 0) {
									System.err.println("Entered time is in the past. Please provide booking time in the future...");
									System.exit(-1);
								}
							}
							startTimeStamp = new Timestamp(bookingStartDate.getTime());
							
							calendarBookingEnd = Calendar.getInstance();
							calendarBookingEnd.set(Calendar.DAY_OF_MONTH, bookingDate);
							calendarBookingEnd.set(Calendar.MONTH, bookingMonth-1);
							calendarBookingEnd.set(Calendar.YEAR, bookingYear);
							calendarBookingEnd.set(Calendar.HOUR_OF_DAY, endTime.getHour());
							calendarBookingEnd.set(Calendar.MINUTE, endTime.getMinute());
							calendarBookingEnd.set(Calendar.SECOND, 0);
							calendarBookingEnd.set(Calendar.MILLISECOND, 0);
							
							bookingEndDate = calendarBookingEnd.getTime();
							endTimeStamp = new Timestamp(bookingEndDate.getTime());
							
							System.out.println("Start: " + startTimeStamp.toString());
							System.out.println("End: " + endTimeStamp.toString());
							
							BookFacilityRequest bookingRequest = controller.generateBookingRequest(userId, facilityId, startTimeStamp, endTimeStamp );
							request = controller.generateRequest(bookingRequest, Type.BOOK_FACILITY);
							client.sendRequest(request);
							System.out.println("Booking facility " + facilityName + " on " + bookingDay + ", " + bookingDate +"/" + bookingMonth + "/" + bookingYear +  " from " + start + " to " + end);
							response = client.receiveResponse();
							client.processBookFacilityResponse(response);
							break;
							
					case 3: System.out.print("Enter your booking ID: ");
							int bookingId = sc.nextInt();
							System.out.print("Enter offset for change (in minutes): ");
							int offset = sc.nextInt();
							if (( Math.abs(offset) % 30) == 0) {
								ChangeBookingRequest changeRequest = controller.generateChangeRequest(bookingId, offset);
								request = controller.generateRequest(changeRequest, Type.CHANGE_BOOKING);
								client.sendRequest(request);
								System.out.println("Changing booking " + bookingId + " by " + offset + " minutes");
								response = client.receiveResponse();
								client.processChangeBookingResponse(response);
							}
							else {
								System.err.println("Time slots for 30 minutes intervals. Please provide valid inputs.");
								System.exit(-1);
							}
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
							
					case 5: System.out.print("Enter UserID to transfer booking to: ");
							String transferUserId = sc.next();
							System.out.print("Enter your booking ID: ");
							bookingId = sc.nextInt();
							
							TransferBookingRequest transferRequest = controller.generateTransferRequest(transferUserId, bookingId);
							request = controller.generateRequest(transferRequest, Type.TRANSFER_BOOKING);
							client.sendRequest(request);
							//System.out.println("Changing booking " + bookingId + " by " + offset + " minutes");
							response = client.receiveResponse();
							client.processTransferBookingResponse(response);
							break;
							
					case 6: System.out.println("Modify Booking duration");
							System.out.print("Enter your booking ID: ");
							bookingId = sc.nextInt();
							System.out.print("Enter time to increase/decrease duration (in minutes): ");
							int changeDurationAmount = sc.nextInt();
							
							ModifyDurationRequest modifyRequest = controller.generateModifyRequest(bookingId, changeDurationAmount);
							request = controller.generateRequest(modifyRequest, Type.MODIFY_DURATION);
							client.sendRequest(request);
							//System.out.println("Changing booking " + bookingId + " by " + offset + " minutes");
							response = client.receiveResponse();
							client.processModifyDurationResponse(response);
							
							break;
							
					case 7: System.out.println("Exiting client application");
							client.finish();
							break;
							
					default:System.out.println("Invalid choice, please enter again...");
				}
			} while (choice!=7);
		}
		catch (Exception e){
			 System.err.println(e.getStackTrace() + " caused by " + e.getCause() + e.toString());
			 e.printStackTrace();
			 //System.err.println("Please provide valid inputs.");
			 System.exit(-1);
		}		
		finally {
			System.exit(0);
		}
	}

}
