package server;
import java.sql.Date;
import java.sql.Timestamp;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import shared.model.Booking;
import shared.webservice.BookFacilityRequest;
import shared.webservice.BookFacilityResponse;
import shared.webservice.QueryFacilityRequest;
import shared.webservice.QueryFacilityResponse;
import shared.webservice.Request;
import shared.webservice.Response;
import shared.webservice.Type;

public class Controller {
	
	public Response processRequest(Request request) {
		switch (request.getRequestType()) {
		case Type.QUERY_FACILITY:
			return queryFacility(request);
			
		case Type.BOOK_FACILITY:
			return bookFacilty(request);

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
							data.getFacilityName(), 
							new Date(iter.next().getTime()));
			bookingList.addAll(temp);
		}
		
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
		return new Response(responseData);
	}
}
