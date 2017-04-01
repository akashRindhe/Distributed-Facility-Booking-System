package client;

import shared.Marshallable;
import shared.model.Booking;
import shared.webservice.*;

import java.sql.Timestamp;
import java.util.List;

public class Controller {
	private static int requestId = 0;
	public Request generateRequest(Marshallable marshallable, int type) {
		Request request = new Request();
		requestId++;
		request.setRequestId(Integer.toString(requestId));
		request.setRequestType(type);
		request.setRequestData(marshallable);
		return request;
	}
	
	public GetFacilitiesRequest generateFacilityRequest () {
		return new GetFacilitiesRequest();
		
	}
	
	public QueryFacilityRequest generateQueryRequest (int facilityId, List<Timestamp> days) {
		
		QueryFacilityRequest request = new QueryFacilityRequest();
		request.setFacilityId(facilityId);
		request.setDays(days);
		return request;
		
	}
	
	public BookFacilityRequest generateBookingRequest (String userId, int facilityId, Timestamp start, Timestamp end) {
		
		Booking booking = new Booking();
		booking.setId(0);
		booking.setUserId(userId);
		booking.setFacilityId(facilityId);
		booking.setBookingStart(start);
		booking.setBookingEnd(end);
		BookFacilityRequest request = new BookFacilityRequest(booking);
		return request;
		
	}

	public ChangeBookingRequest generateChangeRequest(int bookingId, int offsetMinutes) {
		
		ChangeBookingRequest request = new ChangeBookingRequest();
		request.setConfirmationId(bookingId);
		request.setOffset(offsetMinutes);
		return request;
		
	}

	public TransferBookingRequest generateTransferRequest(
			String transferUserId, int bookingId) {
		
		TransferBookingRequest request = new TransferBookingRequest();
		request.setNewUserId(transferUserId);
		request.setBookingId(bookingId);
		return request;
	}

	public ModifyDurationRequest generateModifyRequest(int bookingId,
			int changeDurationAmount) {
		
		ModifyDurationRequest request = new ModifyDurationRequest();
		request.setBookingId(bookingId);
		request.setOffset(changeDurationAmount);
		return request;
		
	}

	public CallbackRequest generateCallbackRequest(int facilityId,
			Timestamp monitorStart, Timestamp monitorEnd) {
		
		CallbackRequest request = new CallbackRequest();
		request.setFacilityId(facilityId);
		request.setMonitorStart(monitorStart);
		request.setMonitorEnd(monitorEnd);
		return request;
	}

}
