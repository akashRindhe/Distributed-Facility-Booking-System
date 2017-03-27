package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import joptsimple.OptionParser;
public class ClientApplication {
	public static OptionParser getParser() {
		OptionParser optParser = new OptionParser();
		optParser.accepts("ip").withRequiredArg().required().ofType(String.class);
		optParser.accepts("port").withRequiredArg().required().ofType(Integer.class);
		optParser.accepts("userId").withRequiredArg().required().ofType(String.class);
		return optParser;
	}
	public static void main(String[] args) throws Exception {
		OptionParser optParser = ClientApplication.getParser();
		try{
			optParser.parse(args);
		}
		catch (Exception e){
			 System.err.println("Caught Exception: " + e.getMessage());
			 System.exit(-1);
		}
		Scanner sc = new Scanner(System.in);
		int choice;
		do {
			System.out.println("Services Provided");
			System.out.println("1. Query Facility Availability");
			System.out.println("2. Book Facility");
			System.out.println("3. Change Booking");
			System.out.println("4. Monitor ");
			System.out.println("5. An idempotent service");
			System.out.println("6. A non-idempotent service");
			System.out.println("7. Quit");
			System.out.print("Enter an option: ");
			choice = sc.nextInt();
			String facilityId;
			
			switch (choice) 
			{
			case 1: System.out.print("Enter facility ID: ");
					facilityId = sc.next();
					System.out.print("Enter number of days: ");
					int length = sc.nextInt();
					List<String> listDays = new ArrayList<String>(length); 
					for (int i=0; i<length; i++) {
						System.out.print("Enter day "+ (i+1) + ": ");
						listDays.add(sc.next());
					}
					//Generate QueryFacility request object
					System.out.println("Querying for facility " + facilityId);
					break;
			case 2: System.out.print("Enter facility ID: ");
					facilityId = sc.next();
					System.out.print("Enter booking day: ");
					String day = sc.next();
					System.out.print("Enter start time for booking: ");
					String startTime = sc.next();
					System.out.print("Enter end time for booking: ");
					String endTime = sc.next();
					//Generate BookFacility request object
					System.out.println("Booking facility " + facilityId + " on " + day + " from " + startTime + " to " + endTime);
					break;
			case 3: System.out.print("Enter booking ID: ");
					String bookingId = sc.next();
					System.out.print("Enter offset for change (in minutes): ");
					int offset = sc.nextInt();
					//Generate ChangeFacility request object
					System.out.println("Changin booking " + bookingId + " by " + offset + " minutes");
					break;
			case 4: System.out.print("Enter facility ID: ");
					facilityId = sc.next();
					System.out.print("Enter offset for change (in minutes): ");
					int interval = sc.nextInt();
					//Generate MonitorFacility request object
					System.out.println("Monitoring facility " + facilityId + " for " + interval + " days");
					break;
			case 5: System.out.println("Call an idempotent service");
					break;
			case 6: System.out.println("Call an non-idempotent service");
					break;
			default:System.out.println("Exiting...");
			}
		} while (choice <7 && choice > 0);
		System.exit(0);
		
		/*
		OptionParser optParserPrimary = new OptionParser();
		optParserPrimary.accepts("ip").withRequiredArg().required().ofType(String.class);
		optParserPrimary.accepts("port").withRequiredArg().required().ofType(Integer.class);
		optParserPrimary.accepts("service").withRequiredArg().required().ofType(String.class);
		optParserPrimary.accepts("numParams").withRequiredArg().required().ofType(Integer.class);
		optParserPrimary.accepts("days").withRequiredArg().ofType(String.class);
		optParserPrimary.accepts("").withRequiredArg().required().withValuesConvertedBy(DateConverter.datePattern( "dd/MM/yy" ));
		OptionSet optSetPri = optParserPrimary.parse(args);
		//String service = (String) optSetPri.valueOf("service");
		*/
	}

}
