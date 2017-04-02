package server;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import server.filter.CallbackFilter;
import shared.model.Booking;
import shared.model.Facility;
import shared.webservice.BookFacilityRequest;
import shared.webservice.BookFacilityResponse;
import shared.webservice.CallbackResponse;
import shared.webservice.ChangeBookingRequest;
import shared.webservice.ChangeBookingResponse;
import shared.webservice.GetFacilitiesResponse;
import shared.webservice.ModifyDurationRequest;
import shared.webservice.ModifyDurationResponse;
import shared.webservice.QueryFacilityRequest;
import shared.webservice.QueryFacilityResponse;
import shared.webservice.Request;
import shared.webservice.Response;
import shared.webservice.TransferBookingRequest;
import shared.webservice.TransferBookingResponse;
import shared.webservice.Type;

/**
 * This class is response for appropriately processing a request to generate
 * the response based on the request type.
 */
public class Controller {
	
	private List<Facility> facilities;
	
	public Controller() {
		this.facilities = DatabaseAccess.fetchAllFacilities(); 
	}
	
	/**
	 * Dispatch request to the suitable function for processing
	 * based on its type.
	 */
	public Response processRequest(Request request) {
		switch (request.getRequestType()) {
		case Type.QUERY_FACILITY:
			return queryFacility(request);
			
		case Type.BOOK_FACILITY:
			return bookFacilty(request);
		
		case Type.CHANGE_BOOKING:
			return changeBooking(request);
			
		case Type.TRANSFER_BOOKING:
			return transferBooking(request);
			
		case Type.MODIFY_DURATION:
			return modifyDuration(request);
			
		case Type.GET_FACILITIES:
			return getFacilities(request);
		
		case Type.CALLBACK:
			CallbackResponse responseData = new CallbackResponse();
			responseData.setAcknowledgement("Callback registered successfully");
			return new Response(responseData);
			
		default:
			return new Response("An unknown error has occurred");
		}
	}
	
	/**
	 * Fetches bookings for a facility for the specified days.
	 */
	private Response queryFacility(Request request) {
		QueryFacilityRequest data = 
				(QueryFacilityRequest) request.getRequestData();
		if (!Utility.doesFacilityExist(facilities, data.getFacilityName())) {
			return new Response("Facility does not exist");
		}
		List<Booking> bookingList = new LinkedList<>();
		Iterator<Timestamp> iter = data.getDays().iterator();
		
		while (iter.hasNext()) {
			List<Booking> temp = 
					DatabaseAccess.fetchBookings(
							Utility.facilityNameToId(facilities, data.getFacilityName()), 
							new Date(iter.next().getTime()));
			bookingList.addAll(temp);
		}
		QueryFacilityResponse responseData = new QueryFacilityResponse();
		responseData.setBookings(bookingList);
		Response response = new Response(responseData);
		
		return response;
	}
	
	/**
	 * Book a facility for the specified time interval.
	 */
	private Response bookFacilty(Request request) {
		BookFacilityRequest data = 
				(BookFacilityRequest) request.getRequestData();
		if (DatabaseAccess.fetchUserById(data.getUserId()) == null) {
			return new Response("The given userId does not exist");
		}
		if (!Utility.doesFacilityExist(facilities, data.getFacilityName())) {
			return new Response("Facility does not exist");
		}
		List<Booking> temp = 
				DatabaseAccess.fetchBookings(
						Utility.facilityNameToId(facilities, data.getFacilityName()),
						data.getStartTime(), 
						data.getEndTime());
		// Check for clashes in timeslots.
		if (temp.size() > 0) {
			return new Response("The requested timeslot is unavailable");
		}
		
		BookFacilityResponse responseData = new BookFacilityResponse();
		responseData.setConfirmationId(
				DatabaseAccess.addBooking(Utility.getBookingFromRequest(data, facilities)));
		CallbackFilter.broadcastUpdate(Utility.facilityNameToId(facilities, data.getFacilityName()));
		return new Response(responseData);
	}
	
	/**
	 * Offset the booking period by the specified amount.
	 * Note, this does not change the duration of the booking, it shifts it
	 * by the given offset.
	 */
	private Response changeBooking(Request request){
		ChangeBookingRequest data = 
				(ChangeBookingRequest) request.getRequestData();
		Booking booking = 
				DatabaseAccess.fetchBookingById(
						data.getConfirmationId());
		if (booking == null) {
			return new Response("Booking does not exist");
		}
		Timestamp bookStart = new Timestamp(booking.getBookingStart().getTime() + data.getOffset()*60*1000); 
		Timestamp bookEnd   = new Timestamp(booking.getBookingEnd().getTime() + data.getOffset()*60*1000);
		return changeBooking(booking, bookStart, bookEnd);
	}
	
	/**
	 * Helper method to set the booking start and end times to the given start
	 * and end times.
	 */
	private Response changeBooking(
			Booking booking, Timestamp bookStart, Timestamp bookEnd) {
		List<Booking> temp =
				DatabaseAccess.fetchBookings(
						booking.getFacilityId(),
						bookStart,
						bookEnd);
		
		for (Booking storedBooking : temp)
		{
			if (storedBooking.getId() != booking.getId())
				return new Response("The requested alteration is not possible due to clash");
		}
			
		booking.setBookingStart(bookStart);
		booking.setBookingEnd(bookEnd);
		ChangeBookingResponse responseData = new ChangeBookingResponse();
		DatabaseAccess.changeBooking(booking);
		responseData.setAcknowledgement("Your booking has been successfully altered in the system");
		// Broadcast this update to clients monitoring this facility.
		CallbackFilter.broadcastUpdate(booking.getFacilityId());
		return new Response(responseData);
	}

	/**
	 * Transfer an existing booking to a different user.
	 */
	private Response transferBooking(Request request) {
		TransferBookingRequest data =
				(TransferBookingRequest) request.getRequestData();
		if (DatabaseAccess.fetchUserById(data.getNewUserId()) == null) {
			return new Response("The userId the facility is being transferred to does not exist");
		}
		if (DatabaseAccess.fetchBookingById(data.getBookingId()) == null) {
			return new Response("Booking does not exist");
		}
		DatabaseAccess
			.changeBookingUserId(
					data.getBookingId(), data.getNewUserId());
		TransferBookingResponse responseData = new TransferBookingResponse();
		responseData.setAcknowledgement("Booking transferred successfully");
		Booking booking = DatabaseAccess.fetchBookingById(data.getBookingId());
		// Broadcast this update to clients monitoring this facility.
		CallbackFilter.broadcastUpdate(booking.getFacilityId());
		return new Response(responseData);
	}
	
	/**
	 * Shifts the end time of a booking by the specified offset amount.
	 */
	private Response modifyDuration(Request request) {
		ModifyDurationRequest data =
				(ModifyDurationRequest) request.getRequestData();
		Booking booking = DatabaseAccess.fetchBookingById(data.getBookingId());
		if (booking == null) {
			return new Response("Booking does not exist");
		}
		Timestamp bookEnd   = new Timestamp(booking.getBookingEnd().getTime() + data.getOffset()*60*1000);
		changeBooking(booking, booking.getBookingStart(), bookEnd);
		ModifyDurationResponse responseData = new ModifyDurationResponse();
		responseData.setAcknowledgement("Booking duration modified successfully");
		// Broadcast this update to clients monitoring this facility.
		CallbackFilter.broadcastUpdate(booking.getFacilityId());
		return new Response(responseData);
	}
	
	/**
	 * Fetches a list of all the facilities.
	 */
	private Response getFacilities(Request request) {
		GetFacilitiesResponse responseData = new GetFacilitiesResponse();
		responseData.setFacilities(DatabaseAccess.fetchAllFacilities());
		
		return new Response(responseData);
	}
}
