package server;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.SerializationConfig.Feature;

import server.filter.AtMostOnceFilter;
import server.filter.CallbackFilter;
import server.filter.FilterService;
import server.filter.TimeoutSimulationFilter;
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
	private boolean simulateTimeout;
	private long requestCount;
	private ObjectMapper objectMapper;
	
	Server(int port, boolean isAtMostOnce, boolean simulateErrors) {
		this.port = port;
		this.filterService = new FilterService();
		this.controller = new Controller();
		this.isAtMostOnce = isAtMostOnce;
		this.simulateTimeout = simulateErrors;
		this.objectMapper = new ObjectMapper();
		objectMapper.configure(Feature.FAIL_ON_EMPTY_BEANS, false);
		if (simulateErrors) {
			this.filterService.addFilter(new TimeoutSimulationFilter());
		}
		if (isAtMostOnce) {
			this.filterService.addFilter(new AtMostOnceFilter());
			this.history = new History();
		}
		this.filterService.addFilter(new CallbackFilter());
		instance = this;
	}
	
	/**
	 * Start the server with the input configuration.
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void start() throws IOException, ClassNotFoundException {
		// Bind socket to the correct port.
		socket = new DatagramSocket(port);
		System.out.println("Server started on port " + port);
		System.out.println("AtMostOnce invocation: " + isAtMostOnce);
		System.out.println("Is simulating errors: " + simulateTimeout);
		System.out.println();
		// The server keeps looping to receive packets from clients.
		while (true) {
			try {
				System.out.println("Waiting to receive packet");
				byte[] buf = new byte[1024];
				DatagramPacket packet = new DatagramPacket(buf, buf.length);
				socket.receive(packet);
				// Unmarshall request from the bytes in the received packet.
				Request request =
						MarshallingService
							.getInstance()
							.unmarshal(packet.getData(), Request.class);
				requestCount++;
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
	
	/**
	 * This method processes the input request and sends the appropriate
	 * response to the client. 
	 */
	private void processRequest(Request request, DatagramPacket packet) 
			throws IOException, IllegalArgumentException, IllegalAccessException {
		System.out.println("Processing request of type: " + Utility.getRequestTypeString(request.getRequestType()));
		try {
			System.out.println(objectMapper.writeValueAsString(request));
		} catch (JsonGenerationException | JsonMappingException e) {
			e.printStackTrace();
		}
		System.out.println();
		// Calls the filterService to decide if this request should
		// be filtered out. Only proceed with the processing of the
		// request if filtering succeeds.
		if (filterService.performFiltering(request, packet)) {
			Response response;
			try {
				// Dispatch request to the Controller for processing to
				// generate the response.
				response = controller.processRequest(request);
				if (isAtMostOnce) {
					// Maintain request history for AtMostOnce server.
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
	
	/**
	 * Send the response to the client.
	 * @param response - Response to send.
	 * @param address - IP Address of the client.
	 * @param port - Port of the client
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void sendResponse(
			Response response, InetAddress address, int port) 
					throws IllegalArgumentException, IllegalAccessException, IOException {
		// If the server is started up in simulateErrors mode, then simulate the
		// failure of every second request. In this case, we simulate the scenario
		// of the request reaching the server but the response being lost before
		// reaching the client.
		if (simulateTimeout && requestCount % 3 == 2) {
			System.out.println("Simulate response failure");
			System.out.println();
			return;
		}
		// Marshal the response object to generate a byte array.
		byte[] buf = MarshallingService.getInstance().marshal(response);
		DatagramPacket packet = 
				new DatagramPacket(buf, buf.length, address, port);
		socket.send(packet);
		System.out.println("Response sent to " + address.getHostAddress() + ":" + port);
		try {
			System.out.println(objectMapper.writeValueAsString(response));
		} catch (JsonGenerationException | JsonMappingException e) {
			e.printStackTrace();
		}
		System.out.println();
	}
	
	public History getHistory() {
		return history;
	}
	
	public long getRequestCount() {
		return requestCount;
	}
	
	public static Server getInstance() {
		return instance;
	}
}
