package client;

import shared.webservice.*;

import java.sql.Timestamp;
import java.util.List;

public class Controller {
	
	public QueryFacilityRequest generateQueryRequest (String facilityName, List<Timestamp> days) {
		
		QueryFacilityRequest request = new QueryFacilityRequest();
		request.setFacilityName(facilityName);
		request.setDays(days);
		return request;
		
	}
	
	public BookFacilityRequest generateBookingRequest (String facilityName, Timestamp start, Timestamp end) {
		
		BookFacilityRequest request = new BookFacilityRequest();
		request.setFacilityName(facilityName);
		request.setStartTimeStamp(start);
		request.setEndTimeStamp(end);
		return request;
		
	}

	public ChangeBookingRequest generateChangeRequest (int bookingId, int offsetMinutes) {
		
		ChangeBookingRequest request = new ChangeBookingRequest();
		request.setConfirmationId(bookingId);
		request.setOffset(offsetMinutes);
		return request;
		
	}

}
