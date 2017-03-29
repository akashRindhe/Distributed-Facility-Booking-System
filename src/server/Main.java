package server;

import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException, ClassNotFoundException {
		int port = 4445;
		final Server server = new Server(port);
		
		Runtime.getRuntime().addShutdownHook(new Thread() {
		    public void run() {
		       server.finish();
		    }
		 });
		
		server.start();
	}

}
