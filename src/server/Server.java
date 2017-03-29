package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.filter.FilterService;
import shared.service.MarshallingService;
import shared.webservice.Request;
import shared.webservice.Response;

public class Server {
	
	private int port;
	private DatagramSocket socket;
	private FilterService filterService;
	private Controller controller;
	
	Server(int port) {
		this.port = port;
		this.filterService = new FilterService();
		this.controller = new Controller();
	}
	
	public void start() throws IOException, ClassNotFoundException {
		socket = new DatagramSocket(port);
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		while (true) {
			try {
				socket.receive(packet);
				Request request =
						MarshallingService
							.getInstance()
							.unmarshal(packet.getData(), Request.class);
				processRequest(request, packet);
			} catch (IOException e) {
				throw e;
			} catch (InstantiationException e) {
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
		}
	}
	
	public void finish() {
		socket.close();
	}
	
	private void processRequest(Request request, DatagramPacket packet) 
			throws IOException, IllegalArgumentException, IllegalAccessException {
		if (filterService.performFiltering(request)) {
			Response response;
			try {
				response = controller.processRequest(request);
				sendResponse(response, packet.getAddress(), packet.getPort());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				response = new Response("An unknown error has occurred");
				sendResponse(response, packet.getAddress(), packet.getPort());
			}
		}
	}
	
	private void sendResponse(
			Response response, InetAddress address, int port) 
					throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = MarshallingService.getInstance().marshal(response);
		DatagramPacket packet = 
				new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
	}

}
