package server;

import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

/**
 * This class takes in configuration parameters and starts the server.
 */
public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 4445;
		boolean atMostServer = false;
		boolean simulateErrors = false;
		
		OptionParser optParser = new OptionParser();
		optParser.accepts("port").withRequiredArg().ofType(Integer.class);
		optParser.accepts("atMostOnce").withRequiredArg().ofType(String.class);
		optParser.accepts("simulateErrors").withRequiredArg().ofType(String.class);
		
		OptionSet optSet = optParser.parse(args);
		if (optSet.has("port"))
			port = (int) optSet.valueOf("port");
		if (optSet.has("atMostOnce"))
			atMostServer = Boolean.parseBoolean((String) optSet.valueOf("atMost"));
		if (optSet.has("simulateErrors"))
			simulateErrors = Boolean.parseBoolean((String) optSet.valueOf("simulateErrors"));
		
		// Start the server with the configuration input by the user
		final Server server = new Server(port, atMostServer, simulateErrors);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		       server.finish();
		    }
		 });
		
		server.start();
	}

}
