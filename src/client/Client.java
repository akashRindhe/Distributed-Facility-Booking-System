package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import server.Controller;
import shared.Marshallable;
import shared.model.Booking;
import shared.model.Facility;
import shared.service.MarshallingService;
import shared.webservice.*;

public class Client {
	
	private int clientPort, serverPort;
	private DatagramSocket socket;
	private Controller controller;
	private InetAddress serverAddress;
	private MarshallingService marshallingService;
	
	private List<Facility> facilities = new ArrayList<Facility>();
	
	public List<Facility> getFacilities() {
		return facilities;
	}

	Client (int clientPort, int serverPort, InetAddress address) {
		this.clientPort = clientPort;
		this.serverPort = serverPort;
		this.controller = new Controller();
		this.serverAddress = address;
		this.marshallingService = MarshallingService.getInstance();
	}
	
	public void start(Request request) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		socket = new DatagramSocket(clientPort);
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		try {
			socket.send(sendPacket);
			System.out.println("Waiting for response: " );
			socket.receive(receivePacket);
			Response response = marshallingService.unmarshal(receivePacket.getData(), Response.class);
			System.out.println("Response: " + response.getData());
		} catch (IOException e) {
			throw e;
		}
	}
	
	public void finish() {
		this.socket.close();
	}
	
	public void sendRequest(Request request) throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
	}
	
	public void sendGetFacilitiesRequest(GetFacilitiesRequest request) throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
	}
	
	public void sendQueryFacilityRequest(QueryFacilityRequest queryRequest) throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = marshallingService.marshal(queryRequest);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
	}
	
	public void sendBookFacilityRequest(BookFacilityRequest request) throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
	}
	
	public void sendChangeBookingRequest(ChangeBookingRequest request) throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
	}
	
	public void processResponse(Response response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte [1024];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		response = marshallingService.unmarshal(receivePacket.getData(), response.getClass());
		int error = response.getIsError();
		System.out.println("Error:" + error);		
	}
	
	public void processGetFacilitiesResponse(GetFacilitiesResponse response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte [1024];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		response = marshallingService.unmarshal(receivePacket.getData(), response.getClass());
		List<Facility> facilities = response.getFacilities();
		System.out.println("Size:" + facilities.size());
		
		//Display all facilities
		
		
	}
	public void processQueryFacilityResponse(QueryFacilityResponse response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte [1024];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		response = marshallingService.unmarshal(receivePacket.getData(), response.getClass());
		List<Booking> bookings = response.getBookings();
		
		//Display all bookings
		
		
	}
	
	public void processBookFacilityResponse(BookFacilityResponse response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte [1024];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		response = marshallingService.unmarshal(receivePacket.getData(), response.getClass());
		
		int confirmationId = response.getConfirmationId();
		System.out.println("Confirmation ID for booking: " + confirmationId);
	}
	
	public void processChangeBookingResponse(ChangeBookingResponse response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte [1024];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		response = marshallingService.unmarshal(receivePacket.getData(), response.getClass());
		
		int success = response.getSuccess();
		if (success == 1) 
			System.out.println("Booking changed successfully.");
		else
			System.out.println("Booking could not be changed.");
			
	}


}
