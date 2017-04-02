package server;

import java.io.IOException;

import joptsimple.OptionParser;
import joptsimple.OptionSet;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 4445;
		boolean atMostServer = false;
		boolean simulateErrors = false;
		
		OptionParser optParser = new OptionParser();
		optParser.accepts("port").withRequiredArg().ofType(Integer.class);
		optParser.accepts("atMost").withRequiredArg().ofType(String.class);
		optParser.accepts("simulateErrors").withRequiredArg().ofType(String.class);
		
		OptionSet optSet = optParser.parse(args);
		if (optSet.has("port"))
			port = (int) optSet.valueOf("port");
		if (optSet.has("atMost"))
			atMostServer = Boolean.parseBoolean((String) optSet.valueOf("atMost"));
		if (optSet.has("simulateErrors"))
			simulateErrors = Boolean.parseBoolean((String) optSet.valueOf("simulateErrors"));
		
		final Server server = new Server(port, atMostServer, simulateErrors);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		       server.finish();
		    }
		 });
		
		server.start();
	}

}
