package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.filter.AtMostOnceFilter;
import server.filter.CallbackFilter;
import server.filter.FilterService;
import shared.service.MarshallingService;
import shared.webservice.Request;
import shared.webservice.Response;

public class Server {
	
	private static Server instance;
	
	private int port;
	private DatagramSocket socket;
	private FilterService filterService;
	private Controller controller;
	private History history;
	private boolean isAtMostOnce;
	
	Server(int port, boolean isAtMostOnce) {
		this.port = port;
		this.filterService = new FilterService();
		this.controller = new Controller();
		this.isAtMostOnce = isAtMostOnce;
		if (isAtMostOnce) {
			this.filterService.addFilter(new AtMostOnceFilter());
			this.history = new History();
		}
		this.filterService.addFilter(new CallbackFilter());
		instance = this;
	}
	
	public void start() throws IOException, ClassNotFoundException {
		socket = new DatagramSocket(port);
		System.out.println("Server started on port " + port);
		while (true) {
			try {
				System.out.println("Waiting to receive packet");
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
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
		System.out.println("Processing request of type: " + request.getRequestType());
		if (filterService.performFiltering(request, packet)) {
			Response response;
			try {
				response = controller.processRequest(request);
				if (isAtMostOnce) {
					history.put(request, response);
				}
				sendResponse(response, packet.getAddress(), packet.getPort());
			} catch (IllegalArgumentException | IllegalAccessException e) {
				e.printStackTrace();
				response = new Response("An unknown error has occurred");
				sendResponse(response, packet.getAddress(), packet.getPort());
			}
		}
	}
	
	public void sendResponse(
			Response response, InetAddress address, int port) 
					throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = MarshallingService.getInstance().marshal(response);
		DatagramPacket packet = 
				new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
		System.out.println("Response sent to " + address.getHostAddress() + ":" + port);
	}
	
	public History getHistory() {
		return history;
	}
	
	public static Server getInstance() {
		return instance;
	}
}
