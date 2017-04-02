package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketTimeoutException;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import client.utility.TimestampGenerator;
import shared.model.Booking;
import shared.model.Facility;
import shared.service.MarshallingService;
import shared.webservice.*;

/**
 * Client 
 * @author shuvamnandi
 */
public class Client {
	private static final int TIMEOUT = 3000;
	private static final int MAX_RETRY = 3;
	
	private int clientPort, serverPort;
	private DatagramSocket socket;
	private InetAddress serverAddress;
	private MarshallingService marshallingService;
	
	private List<Facility> facilities;
	
	/**
	 * 
	 * @return the list of all facilities
	 */
	public List<Facility> getFacilities() {
		return facilities;
	}
	
	
	/**
	 * Sets the port numbers for the client and server, and the server's IP address for the communications
	 * @param clientPort
	 * @param serverPort
	 * @param address
	 */
	Client (int clientPort, int serverPort, InetAddress address) {
		this.clientPort = clientPort;
		this.serverPort = serverPort;
		this.serverAddress = address;
		this.marshallingService = MarshallingService.getInstance();
		this.facilities = new ArrayList<Facility>();
	}
	
	/**
	 * Starts the client and sends a request object to the server, processes the response thereafter
	 * @param request - Request object containing the request object of the GetFacilities service
	 * @throws IOException
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public void start(Request request) throws IOException, IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException {
		socket = new DatagramSocket(clientPort);
		try {
			Response response = sendRequest(request);
			processGetFacilitiesResponse(response);
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
	 * Sends a request object by calling the private function _sendRequest to the server
	 * @param request - Request object containing the request type and the request data for service to be invoked
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 * @throws InstantiationException
	 */
	public Response sendRequest(Request request) 
			throws IllegalArgumentException, IllegalAccessException, 
	IOException, InstantiationException, ClassNotFoundException {
		return _sendRequest(request, 0);
	}
	
	/**
	 * Sends a request object to the server and receives a response accordingly. The timeout of the socket is set as 
	 * TIMEOUT, upon its expiry another request is sent MAX_RETRIES - 1 times.
	 * @param request
	 * @param attempt
	 * @return response received from the server, for the service request sent
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	private Response _sendRequest(Request request, int attempt) 
			throws IllegalArgumentException, IllegalAccessException, IOException, 
			InstantiationException, ClassNotFoundException {
		byte[] buf = marshallingService.marshal(request);
		DatagramPacket sendPacket = new DatagramPacket(buf, buf.length, this.serverAddress, this.serverPort);
		socket.send(sendPacket);
		
		Response response = null;
		try {
			socket.setSoTimeout(TIMEOUT);
			response = receiveResponse();
		} catch (SocketTimeoutException e) {
			if (attempt == MAX_RETRY - 1) {
				throw e;
			}
			System.out.println("Request timeout. Retrying...");
			response = _sendRequest(request, ++attempt);
		} finally {
			socket.setSoTimeout(0);
		}
		return response;
	}
	
	/**
	 * Waits until a server response is obtained at the socket. It is a blocking call which waits 
	 * until a packet is received.
	 * @return a response object sent by the server
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 * @throws IOException
	 * @throws InstantiationException
	 * @throws ClassNotFoundException
	 */
	public Response receiveResponse() 
			throws IllegalArgumentException, IllegalAccessException, IOException, 
			InstantiationException, ClassNotFoundException {
		byte[] buf = new byte[10000];
		DatagramPacket receivePacket = new DatagramPacket(buf, buf.length);
		socket.receive(receivePacket);
		Response response = marshallingService.unmarshal(receivePacket.getData(), Response.class);
		return response;
	}
	
	/**
	 * Retrieves the response data for the GetFacilitiesRequest from the response object, and displays it to the user.
	 * Displays the names and IDs of the facilities.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processGetFacilitiesResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		} 
		else {
			GetFacilitiesResponse facilitiesResponse = (GetFacilitiesResponse) response.getData();
			facilities = facilitiesResponse.getFacilities();
			System.out.println("Number of facilities " + facilities.size());
			
			//Display all facilities
			System.out.println("ID     |    Facility Name ");
			System.out.println("--------------------------");
			for(int i = 0; i < facilities.size(); i++)
				System.out.println(facilities.get(i).getId() + "  |      " + facilities.get(i).getName());
		}
	}
	
	/**
	 * Retrieves the response data for the QueryFacilitiesRequest from the response object, and displays it to the user.
	 * Displays the IDs of the facilities and their available time slots.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processQueryFacilityResponse(Response response, List<Timestamp> listDays) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			QueryFacilityResponse queryResponse = (QueryFacilityResponse) response.getData();
			List<Booking> bookings = queryResponse.getBookings();
			for (int i = 0; i < listDays.size(); i++) {
				Timestamp t = TimestampGenerator.generateDate(listDays.get(i).toLocalDateTime().getDayOfWeek().toString().toUpperCase());
				System.out.println(" Day      | Date                    | Free Times        ");
				System.out.print(listDays.get(i).toLocalDateTime().getDayOfWeek() + "     " + t.toString());
				if (bookings.size() == 0) {
					System.out.println(" 00:00 - 24:00");
				}
				else {
					for(int j = 0; j < bookings.size(); j++) {
						Timestamp bookingStart = bookings.get(j).getBookingStart();
						Timestamp bookingEnd = bookings.get(j).getBookingEnd();
						if (bookingStart.getDate() == t.getDate() && bookingStart.getMonth() == t.getMonth()) {
							System.out.println("Day: " + t.getDate() + " Month: " + (t.getMonth()+1));
							System.out.println(bookingStart.getHours() + ":" + bookingStart.getMinutes());
						}
						//System.out.println(bookings.get(j).getId() + "        |" + bookings.get(j).getFacilityId()+  "|" +bookings.get(j).getUserId() +  "|" +bookings.get(j).getBookingStart().toString() + "|" +bookings.get(j).getBookingEnd().toString() );	
					}
				}
			}
			
			if (bookings.size() > 0) {
				System.out.println("Booking ID|  User ID  |   Facility ID  |   From  |    To    ");
				System.out.println("------------------------------------------------------------");
			}
			else
				System.out.println("No bookings exist.");
			for(int i = 0; i < bookings.size(); i++)
				System.out.println(bookings.get(i).getId() + "        |" + bookings.get(i).getFacilityId()+  "|" +bookings.get(i).getUserId() +  "|" +bookings.get(i).getBookingStart().toString() + "|" +bookings.get(i).getBookingEnd().toString() );	
		}
	}
	
	/**
	 * Retrieves the response data for the BookFacilityRequest from the response object, and displays it to the user.
	 * Displays the confirmation ID of booking, if was successful.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processBookFacilityResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			BookFacilityResponse queryResponse = (BookFacilityResponse) response.getData();
			int confirmationId = queryResponse.getConfirmationId();
			System.out.println("Confirmation ID for booking: " + confirmationId);
		}
	}
	
	/**
	 * Retrieves the response data for the ChangeBookingRequest from the response object, and displays it to the user.
	 * Displays the server acknowledgment message if booking change was successful.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processChangeBookingResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			ChangeBookingResponse changeResponse = (ChangeBookingResponse) response.getData();
			String ack = changeResponse.getAcknowledgement();
			System.out.println("Server acknowledgement: " + ack);
		}
	}
	
	/**
	 * Retrieves the response data for the TransferBookingRequest from the response object, and displays it to the user.
	 * Displays the server acknowledgment message if booking transfer was successful.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processTransferBookingResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			TransferBookingResponse transferResponse = (TransferBookingResponse) response.getData();
			String ack = transferResponse.getAcknowledgement();
			System.out.println("Server acknowledgement: " +  ack);
		}
	}
	
	/**
	 * Retrieves the response data for the ModifyDurationRequest from the response object, and displays it to the user.
	 * Displays the server acknowledgment message if booking duration modification was successful.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processModifyDurationResponse(Response response) {
		
		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			ModifyDurationResponse modifyResponse = (ModifyDurationResponse) response.getData();
			String ack = modifyResponse.getAcknowledgement();
			System.out.println("Server acknowledgement: " + ack);
		}
	}

	/**
	 * Retrieves the response data for the CallbackRequest from the response object, and displays it to the user.
	 * Displays the server acknowledgment message if callback was registered successfullly.
	 * In case of an error, the error message is displayed.
	 * @param response
	 */
	public void processCallbackResponse(Response response, Timestamp end, int monitorInterval) throws IllegalArgumentException, IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {

		if (response.getIsError() == 1) {
			System.out.println("An error occured. Server message: " + ((ErrorData)response.getData()).getErrorType());
		}
		else {
			CallbackResponse monitorResponse = (CallbackResponse) response.getData();
			String ack = monitorResponse.getAcknowledgement();
			System.out.println("Server acknowledgement: " + ack);
			processCallbackUpdateResponse(end, monitorInterval);
		}
		
	}

	/**
	 * Retrieves the response data for the GetFacilitiesRequest from the response object, and displays it to the user.
	 * Displays server sent updates regarding any activity concerning the facility the callback was registered for.
	 * Keeps waiting for responses from the server, until the monitor interval expires. Timeout for the socket
	 * is set based on the end time of monitor interval and the current time.
	 * Upon expiry of the monitor interval, the timeout for socket is set as 0.
	 * @param response
	 */
	private void processCallbackUpdateResponse(Timestamp end, int monitorInterval) 
			throws IllegalArgumentException, IllegalAccessException, InstantiationException, 
			ClassNotFoundException, IOException {
		
		System.out.println("Waiting for responses..");
		long endTime = System.currentTimeMillis()+monitorInterval*60*1000;
		socket.setSoTimeout((int) (endTime-System.currentTimeMillis()));
		
		while(LocalDateTime.now().compareTo(end.toLocalDateTime()) < 0 ) {
			try {
				System.out.println("Time Now: " + LocalDateTime.now().toLocalTime().toString() +
						", Monitor Interval Ends At: " + end.toLocalDateTime().toString());
				Response response = receiveResponse();
				QueryFacilityResponse monitorUpdateResponse = (QueryFacilityResponse) response.getData();
				List<Booking> bookings = monitorUpdateResponse.getBookings();
				System.out.println("Time: " + LocalDateTime.now().toString() + ", following changes were made to facility: ");
				System.out.println("Booking ID|  User ID  |   Facility ID  |   From  |    To    ");
				System.out.println("------------------------------------------------------------");
				for(int i = 0; i < bookings.size(); i++)
					System.out.println(bookings.get(i).getId() + "        |" + bookings.get(i).getFacilityId()+  "|" +bookings.get(i).getUserId() +  "|" +bookings.get(i).getBookingStart().toString() + "|" +bookings.get(i).getBookingEnd().toString() );				
				
				//Update the timeout for the socket based on current time
				socket.setSoTimeout((int) (endTime-System.currentTimeMillis()));
				
			} catch (SocketTimeoutException e) {
					System.out.println("Time: " + LocalDateTime.now().toLocalTime().toString() + ": No more responses received from server. Monitor of "+ monitorInterval + " minutes expired!");
					socket.setSoTimeout(0);
			} 
		}
		System.out.println("Exiting monitor mode! \n");
	}
}
