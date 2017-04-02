package client;

import java.util.Scanner;
import joptsimple.OptionParser;

/**
 * Supporting functions to accept user arguments while launching the Client and display on the console
 * @author shuvamnandi
 */
public class Console {
	Scanner scanner = new Scanner(System.in);
	
	public Scanner getScanner() {
		return scanner;
	}
	
	/**
	 * @return OptionParser object which requires server IP address, clientPort, serverPort, and userId to be specified 
	 * and maxRetires as an optional argument
	 */
	public OptionParser getParser() {
		OptionParser optParser = new OptionParser();
		optParser.accepts("ip").withRequiredArg().required().ofType(String.class);
		optParser.accepts("clientPort").withRequiredArg().required().ofType(Integer.class);
		optParser.accepts("serverPort").withRequiredArg().required().ofType(Integer.class);
		optParser.accepts("userId").withRequiredArg().required().ofType(String.class);
		optParser.accepts("maxRetries").withRequiredArg().ofType(Integer.class);
		return optParser;
	}
	
	/**
	 * Displays the menu of services provided
	 */
	public void displayMenu() {
		System.out.println("Services Provided");
		System.out.println("1. Query Facility Availability");
		System.out.println("2. Book Facility");
		System.out.println("3. Change Booking");
		System.out.println("4. Monitor Facility ");
		System.out.println("5. Transfer Booking to User");
		System.out.println("6. Modify Booking Duration");
		System.out.println("7. Quit");
		System.out.print("Enter an option: ");
	}
	
}
