package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;

import shared.model.Booking;
import shared.model.Facility;
import shared.service.MarshallingService;
import shared.webservice.*;

public class Client {
	
	private int clientPort, serverPort;
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private MarshallingService marshallingService;
	
	private List<Facility> facilities;
	
	public List<Facility> getFacilities() {
		return facilities;
	}

	Client (int clientPort, int serverPort, InetAddress address) {
		this.clientPort = clientPort;
		this.serverPort = serverPort;
		this.serverAddress = address;
		this.marshallingService = MarshallingService.getInstance();
		this.facilities = new ArrayList<Facility>();
	}
	
	public void start(Request request) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		socket = new DatagramSocket(clientPort);
		byte[] sendBuf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, this.serverAddress, this.serverPort);
		byte[] recBuf = new byte[10000];
		DatagramPacket receivePacket = new DatagramPacket(recBuf, recBuf.length);
		try {
			socket.send(sendPacket);
			//System.out.println("Waiting for response: " );
			//System.out.println("sendPacket length:" + sendPacket.getLength());
			socket.receive(receivePacket);
			System.out.println("receivePacket length:" + receivePacket.getLength());
			Response response = marshallingService.unmarshal(receivePacket.getData(), Response.class);
			processGetFacilitiesResponse(response);
			
			System.out.println("Response Class: " + response.getData().getClass());
			
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
	
	public Response receiveResponse() throws IllegalArgumentException, IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte[10000];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		System.out.println("receivePacket length:" +receivePacket.getLength());
		Response response = marshallingService.unmarshal(receivePacket.getData(), Response.class);
		return response;
	}
	
	public void processGetFacilitiesResponse(Response response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		GetFacilitiesResponse facilitiesResponse = (GetFacilitiesResponse) response.getData();
		facilities = facilitiesResponse.getFacilities();
		System.out.println("Size:" + facilities.size());
		
		//Display all facilities
		System.out.println("ID     |    Facility Name ");
		System.out.println("--------------------------");
		for(int i = 0; i<facilities.size(); i++)
			System.out.println(facilities.get(i).getId() + "  |      " + facilities.get(i).getName());
	}
	public void processQueryFacilityResponse(Response response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + response.getData());
		}
		else {
			QueryFacilityResponse queryResponse = (QueryFacilityResponse) response.getData();
			List<Booking> bookings = queryResponse.getBookings();
			
			//Display all bookings
			System.out.println("Booking ID|  User ID  |   Facility ID  |   From  |    To    ");
			for(int i = 0; i<bookings.size(); i++)
				System.out.println(bookings.get(i).getId() + "        |" + bookings.get(i).getFacilityId()+  "|" +bookings.get(i).getUserId() +  "|" +bookings.get(i).getBookingStart().toString() + "|" +bookings.get(i).getBookingEnd().toString() );	
		}
	}
	
	public void processBookFacilityResponse(Response response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			BookFacilityResponse queryResponse = (BookFacilityResponse) response.getData();
			int confirmationId = queryResponse.getConfirmationId();
			System.out.println("Confirmation ID for booking: " + confirmationId);
		}
	}
	
	public void processChangeBookingResponse(Response response) 
			throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			ChangeBookingResponse changeResponse = (ChangeBookingResponse) response.getData();
			String ack = changeResponse.getAcknowledgement();
			System.out.println("Server acknowledgement :" + ack);
		}
	}

	public void processTransferBookingResponse(Response response) {
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			TransferBookingResponse transferResponse = (TransferBookingResponse) response.getData();
			String ack = transferResponse.getAcknowledgement();
			System.out.println("Server acknowledgement :" +  ack);
		}
	}

	public void processModifyDurationResponse(Response response) {
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			ModifyDurationResponse modifyResponse = (ModifyDurationResponse) response.getData();
			String ack = modifyResponse.getAcknowledgement();
			System.out.println("Server acknowledgement :" + ack);
		}
	}
}
