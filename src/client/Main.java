package client;

import client.Console;
import client.utility.TimestampGenerator;

import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.DayOfWeek;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
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
			GetFacilitiesRequest getFacilityRequest = controller.generateFacilityRequest();
			
			Request request = controller.generateRequest(getFacilityRequest, Type.GET_FACILITIES);
			Response response = new Response();
			
			client.start(request);
			facilities = client.getFacilities();
			System.out.println("size of facilities: " + facilities.size());
			
			do {
				console.displayMenu();
				choice = sc.nextInt();
				sc.nextLine();
				String facilityName;
				int facilityId, offset;
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
								Timestamp day = TimestampGenerator.generateDate(queryDayOfWeek);
								System.out.println(day.toString());
								listDays.add(day);
							}
							
							QueryFacilityRequest queryRequest = controller.generateQueryRequest(facilityId, listDays);
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
							List<Timestamp> bookingTimestamp = TimestampGenerator.generateBookingTimestamp(bookingDay, start, end);
							System.out.println("Start: " + bookingTimestamp.get(0));
							System.out.println("End: " + bookingTimestamp.get(1));
							
							BookFacilityRequest bookingRequest = controller.generateBookingRequest(userId, facilityId, bookingTimestamp.get(0), bookingTimestamp.get(1) );
							request = controller.generateRequest(bookingRequest, Type.BOOK_FACILITY);
							client.sendRequest(request);
							System.out.println("Booking facility " + facilityName + " on " + bookingDay + " from " + start + " to " + end);
							response = client.receiveResponse();
							client.processBookFacilityResponse(response);
							break;
							
					case 3: System.out.print("Enter your booking ID: ");
							int bookingId = sc.nextInt();
							System.out.print("Enter offset for change (in minutes): ");
							offset = sc.nextInt();
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
							facilityName = sc.nextLine();
							facility = facilities.stream().filter(o -> o.getName().equals(facilityName)).findFirst();
							facilityId = facility.get().getId();
							System.out.print("Enter amount of time (in minutes) to monitor the facility: ");
							int monitorInterval = sc.nextInt();
							Date todayDate = new java.util.Date();
							Calendar calendarToday = Calendar.getInstance();
							calendarToday.setTime(todayDate);
							int dayOfWeekToday = calendarToday.get(Calendar.DAY_OF_WEEK);
							DayOfWeek dayOfWeek = DayOfWeek.of(dayOfWeekToday-1);
							System.out.println(dayOfWeek.toString());
							Timestamp monitorStart = TimestampGenerator.generateDateWithTime(dayOfWeek.toString(), calendarToday.getTime().getHours(), calendarToday.getTime().getMinutes(), 0);
							Timestamp monitorEnd = TimestampGenerator.generateDateWithTime((dayOfWeek.toString()), calendarToday.getTime().getHours(), calendarToday.getTime().getMinutes()+monitorInterval, 0);
							
							CallbackRequest monitorRequest = controller.generateCallbackRequest(facilityId, monitorStart, monitorEnd);
							request = controller.generateRequest(monitorRequest, Type.CALLBACK);
							client.sendRequest(request);
							System.out.println("Monitorig facility " + facilityName);
							response = client.receiveResponse();
							client.processCallbackResponse(response, monitorEnd);
							System.out.println("Monitoring facility " + facilityName);						
							break;
							
					case 5: System.out.print("Enter UserID to transfer booking to: ");
							String transferUserId = sc.next();
							System.out.print("Enter your booking ID: ");
							bookingId = sc.nextInt();
							
							TransferBookingRequest transferRequest = controller.generateTransferRequest(transferUserId, bookingId);
							request = controller.generateRequest(transferRequest, Type.TRANSFER_BOOKING);
							client.sendRequest(request);
							System.out.println("Transferring booking " + bookingId + " to " + transferUserId);
							response = client.receiveResponse();
							client.processTransferBookingResponse(response);
							break;
							
					case 6: System.out.println("Modify Booking duration");
							System.out.print("Enter your booking ID: ");
							bookingId = sc.nextInt();
							System.out.print("Enter time to increase/decrease duration (in minutes): ");
							offset = sc.nextInt();
							
							ModifyDurationRequest modifyRequest = controller.generateModifyRequest(bookingId, offset);
							request = controller.generateRequest(modifyRequest, Type.MODIFY_DURATION);
							client.sendRequest(request);
							System.out.println("Modify the duration of booking " + bookingId + " by " + offset);
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
			 System.exit(-1);
		}		
		finally {
			System.exit(0);
		}
	}

}
