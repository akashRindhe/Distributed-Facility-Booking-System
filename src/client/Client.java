package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.time.LocalTime;
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
	
	/**
	 * 
	 * @return the list of facilities 
	 */
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
	
	/**
	 * Starts the client and sends a request object to the server, and then processes the response
	 * @param request - Request object containing the request object of the GetFacilities service
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	
	public void start(Request request) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		socket = new DatagramSocket(clientPort);
		byte[] sendBuf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(sendBuf, sendBuf.length, this.serverAddress, this.serverPort);
		byte[] recBuf = new byte[10000];
		DatagramPacket receivePacket = new DatagramPacket(recBuf, recBuf.length);
		try {
			socket.send(sendPacket);
			System.out.println("Waiting for response: " );
			System.out.println("sendPacket length:" + sendPacket.getLength());
			socket.receive(receivePacket);
			System.out.println("receivePacket length:" + receivePacket.getLength());
			Response response = marshallingService.unmarshal(receivePacket.getData(), Response.class);
			processGetFacilitiesResponse(response);
			
			System.out.println("Response Class: " + response.getData().getClass());
			
		} catch (IOException e) {
			throw e;
		}
	}
	
	/**
	 * Closes the client socket.
	 */
	public void finish() {
		this.socket.close();
	}
	
	/**
	 * Sends a request object to the server
	 * @param request - Request object containing the request type and the request object for service to be invoked
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 */
	public void sendRequest(Request request) throws IllegalArgumentException, IllegalAccessException, IOException {
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
	}
	
	/**
	 * Waits until a server response is obtained at the socket
	 * @return a response object sent by the server
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public Response receiveResponse() throws IllegalArgumentException, IllegalAccessException, IOException, InstantiationException, ClassNotFoundException {
		byte[] buf = new byte[10000];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		System.out.println("receivePacket length:" +receivePacket.getLength());
		Response response = marshallingService.unmarshal(receivePacket.getData(), Response.class);
		return response;
	}
	
	/**
	 * 
	 * @param response
	 */
	public void processGetFacilitiesResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		} 
		else {
			GetFacilitiesResponse facilitiesResponse = (GetFacilitiesResponse) response.getData();
			facilities = facilitiesResponse.getFacilities();
			System.out.println("Size:" + facilities.size());
			
			//Display all facilities
			System.out.println("ID     |    Facility Name ");
			System.out.println("--------------------------");
			for(int i = 0; i<facilities.size(); i++)
				System.out.println(facilities.get(i).getId() + "  |      " + facilities.get(i).getName());
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	public void processQueryFacilityResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			QueryFacilityResponse queryResponse = (QueryFacilityResponse) response.getData();
			List<Booking> bookings = queryResponse.getBookings();
			
			//Display all bookings
			System.out.println("Booking ID|  User ID  |   Facility ID  |   From  |    To    ");
			System.out.println("------------------------------------------------------------");
			for(int i = 0; i<bookings.size(); i++)
				System.out.println(bookings.get(i).getId() + "        |" + bookings.get(i).getFacilityId()+  "|" +bookings.get(i).getUserId() +  "|" +bookings.get(i).getBookingStart().toString() + "|" +bookings.get(i).getBookingEnd().toString() );	
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	public void processBookFacilityResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			BookFacilityResponse queryResponse = (BookFacilityResponse) response.getData();
			int confirmationId = queryResponse.getConfirmationId();
			System.out.println("Confirmation ID for booking: " + confirmationId);
		}
	}
	
	/**
	 * 
	 * @param response
	 */
	public void processChangeBookingResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			ChangeBookingResponse changeResponse = (ChangeBookingResponse) response.getData();
			String ack = changeResponse.getAcknowledgement();
			System.out.println("Server acknowledgement :" + ack);
		}
	}
	
	/**
	 * 
	 * @param response
	 */
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
	
	/**
	 * 
	 * @param response
	 */
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

	public void processCallbackResponse(Response response, Timestamp end) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {

		if (response.getIsError() == 1) {
			System.out.println("An error occured. " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			CallbackResponse monitorResponse = (CallbackResponse) response.getData();
			String ack = monitorResponse.getAcknowledgement();
			System.out.println("Server acknowledgement :" + ack);
			processCallbackUpdateResponse(end);
		}
		
	}

	private void processCallbackUpdateResponse(Timestamp end) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
		while(LocalDateTime.now().compareTo(end.toLocalDateTime()) < 0 ) {
			Response response = receiveResponse();
			CallbackUpdateResponse monitorUpdateResponse = (CallbackUpdateResponse) response.getData();
			List<Booking> bookings = monitorUpdateResponse.getBookingList();
			System.out.println("Following bookings were made: ");
			System.out.println("Booking ID|  User ID  |   Facility ID  |   From  |    To    ");
			System.out.println("------------------------------------------------------------");
			for(int i = 0; i<bookings.size(); i++)
				System.out.println(bookings.get(i).getId() + "        |" + bookings.get(i).getFacilityId()+  "|" +bookings.get(i).getUserId() +  "|" +bookings.get(i).getBookingStart().toString() + "|" +bookings.get(i).getBookingEnd().toString() );				
		}
		
		
	}
}
