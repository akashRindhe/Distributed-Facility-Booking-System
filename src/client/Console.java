package client;

import java.util.Scanner;
import joptsimple.OptionParser;

public class Console {
	Scanner scanner = new Scanner(System.in);
	
	public Scanner getScanner() {
		return scanner;
	}
	
	public OptionParser getParser() {
		OptionParser optParser = new OptionParser();
		optParser.accepts("ip").withRequiredArg().required().ofType(String.class);
		optParser.accepts("clientPort").withRequiredArg().required().ofType(Integer.class);
		optParser.accepts("serverPort").withRequiredArg().required().ofType(Integer.class);
		optParser.accepts("userId").withRequiredArg().required().ofType(String.class);
		return optParser;
	}
	
	public void displayMenu() {
		System.out.println("Services Provided");
		System.out.println("1. Query Facility Availability");
		System.out.println("2. Book Facility");
		System.out.println("3. Change Booking");
		System.out.println("4. Monitor ");
		System.out.println("5. An idempotent service");
		System.out.println("6. A non-idempotent service");
		System.out.println("7. Quit");
		System.out.print("Enter an option: ");
	}
	
}
