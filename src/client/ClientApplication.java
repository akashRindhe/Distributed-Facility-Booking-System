package client;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

import joptsimple.OptionParser;
import joptsimple.OptionSet;
import joptsimple.util.DateConverter;
public class ClientApplication {
	public void acceptsArguments() {
		
		
	}
	public static void main(String[] args) throws Exception {
		//System.out.println("");
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
			switch (choice) {
			case 1: System.out.print("Enter facility ID: ");
					String facilityId = sc.next();
					System.out.print("Enter number of days: ");
					int length = sc.nextInt();
					List<String> listDays = new ArrayList<String>(length); 
					for (int i=0; i<length; i++) {
						System.out.print("Enter day "+ (i+1) + ": ");
						listDays.add(sc.next());
					}
					System.out.println("Querying for facility");
					break;
			case 2: System.out.println("Book Facility");
					break;
			case 3: System.out.println("Change Booking");
					break;
			case 4: System.out.println("Monitor Facility");
					break;
			case 5: System.out.println("Call an idempotent service");
					break;
			case 6: System.out.println("Call an non-idempotent service");
					break;
			default:System.out.println("Exiting...");
			
			}
			
		} while (choice <7 && choice > 0);
		
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
