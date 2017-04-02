package client;

import shared.Marshallable;
import shared.webservice.*;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

public class Controller {
	public Request generateRequest(Marshallable marshallable, int type) {
		Request request = new Request();
		request.setRequestId(UUID.randomUUID().toString());
		request.setRequestType(type);
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
	
	public BookFacilityRequest generateBookingRequest (String facilityName, Timestamp start, Timestamp end, String userId) {
		
		BookFacilityRequest request = new BookFacilityRequest();
		request.setFacilityName(facilityName);
		request.setStartTime(start);
		request.setEndTime(end);
		request.setUserId(userId);
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

	public CallbackRequest generateCallbackRequest(String facilityName,
			Timestamp monitorStart, Timestamp monitorEnd) {
		
		CallbackRequest request = new CallbackRequest();
		request.setFacilityName(facilityName);
		request.setMonitorStart(monitorStart);
		request.setMonitorEnd(monitorEnd);
		return request;
	}

}
