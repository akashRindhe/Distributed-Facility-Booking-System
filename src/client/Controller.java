package client;

import shared.Marshallable;
import shared.model.Booking;
import shared.webservice.*;

import java.sql.Timestamp;
import java.util.List;

public class Controller {
	
	public Request generateRequest(Marshallable marshallable) {
		Request request = new Request();
		request.setRequestId("0001");
		request.setRequestType(5);
		request.setRequestData(marshallable);
		return request;
	}
	
	public GetFacilitiesRequest generateFacilityRequest () {
		
		return new GetFacilitiesRequest();
		
	}
	
	public QueryFacilityRequest generateQueryRequest (String facilityName, List<Timestamp> days) {
		
		QueryFacilityRequest request = new QueryFacilityRequest();
		request.setFacilityName(facilityName);
		request.setDays(days);
		return request;
		
	}
	
	public BookFacilityRequest generateBookingRequest (String userId, int facilityId, Timestamp start, Timestamp end) {
		
		Booking booking = new Booking();
		booking.setId(0);
		booking.setUserId(userId);
		booking.setBookingStart(start);
		booking.setBookingStart(end);
		BookFacilityRequest request = new BookFacilityRequest(booking);
		return request;
		
	}

	public ChangeBookingRequest generateChangeRequest (int bookingId, int offsetMinutes) {
		
		ChangeBookingRequest request = new ChangeBookingRequest();
		request.setConfirmationId(bookingId);
		request.setOffset(offsetMinutes);
		return request;
		
	}

}
