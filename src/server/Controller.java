package server;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import server.filter.CallbackFilter;
import shared.model.Booking;
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

public class Controller {
	
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
	
	private Response queryFacility(Request request) {
		QueryFacilityRequest data = 
				(QueryFacilityRequest) request.getRequestData();
		List<Booking> bookingList = new LinkedList<>();
		Iterator<Timestamp> iter = data.getDays().iterator();
		
		while (iter.hasNext()) {
			List<Booking> temp = 
					DatabaseAccess.fetchBookings(
							data.getFacilityId(), 
							new Date(iter.next().getTime()));
			bookingList.addAll(temp);
		}
		//Need to check if Facility Name exists or not
		QueryFacilityResponse responseData = new QueryFacilityResponse();
		responseData.setBookings(bookingList);
		Response response = new Response(responseData);
		
		return response;
	}
	
	private Response bookFacilty(Request request) {
		BookFacilityRequest data = 
				(BookFacilityRequest) request.getRequestData();
		List<Booking> temp = 
				DatabaseAccess.fetchBookings(
						data.getBooking().getFacilityId(),
						data.getBooking().getBookingStart(), 
						data.getBooking().getBookingEnd());
		if (temp.size() > 0) {
			return new Response("The requested timeslot is unavailable");
		}
		
		BookFacilityResponse responseData = new BookFacilityResponse();
		responseData.setConfirmationId(DatabaseAccess.addBooking(data.getBooking()));
		CallbackFilter.broadcastUpdate(data.getBooking().getFacilityId());
		return new Response(responseData);
	}
	
	private Response changeBooking(Request request){
		ChangeBookingRequest data = 
				(ChangeBookingRequest) request.getRequestData();
		Booking booking = 
				DatabaseAccess.fetchBookingById(
						data.getConfirmationId());
		Timestamp bookStart = new Timestamp(booking.getBookingStart().getTime() + data.getOffset()*60*1000); 
		Timestamp bookEnd   = new Timestamp(booking.getBookingEnd().getTime() + data.getOffset()*60*1000);
		return changeBooking(booking, bookStart, bookEnd);
	}
	
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
		CallbackFilter.broadcastUpdate(booking.getFacilityId());
		return new Response(responseData);
	}

	private Response transferBooking(Request request) {
		TransferBookingRequest data =
				(TransferBookingRequest) request.getRequestData();
		DatabaseAccess
			.changeBookingUserId(
					data.getBookingId(), data.getNewUserId());
		TransferBookingResponse responseData = new TransferBookingResponse();
		responseData.setAcknowledgement("Booking transferred successfully");
		Booking booking = DatabaseAccess.fetchBookingById(data.getBookingId());
		CallbackFilter.broadcastUpdate(booking.getFacilityId());
		return new Response(responseData);
	}
	
	private Response modifyDuration(Request request) {
		ModifyDurationRequest data =
				(ModifyDurationRequest) request.getRequestData();
		Booking booking = DatabaseAccess.fetchBookingById(data.getBookingId());
		Timestamp bookEnd   = new Timestamp(booking.getBookingEnd().getTime() + data.getOffset()*60*1000);
		changeBooking(booking, booking.getBookingStart(), bookEnd);
		ModifyDurationResponse responseData = new ModifyDurationResponse();
		responseData.setAcknowledgement("Booking duration modified successfully");
		return new Response(responseData);
	}
	
	private Response getFacilities(Request request) {
		GetFacilitiesResponse responseData = new GetFacilitiesResponse();
		responseData.setFacilities(DatabaseAccess.fetchAllFacilities());
		
		return new Response(responseData);
	}
}
