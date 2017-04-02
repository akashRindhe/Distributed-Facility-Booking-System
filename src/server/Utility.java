package server;

import java.util.List;

import shared.model.Booking;
import shared.model.Facility;
import shared.webservice.BookFacilityRequest;
import shared.webservice.Type;

/**
 * This class implements utility functions used by other classes.
 */
public class Utility {
	
	/**
	 * Maps requestType to its appropriate String representation.
	 * Used for logging purposes.
	 */
	public static String getRequestTypeString(int requestType) {
		switch (requestType) {
		case Type.QUERY_FACILITY:
			return "QUERY_FACILITY";
			
		case Type.BOOK_FACILITY:
			return "BOOK_FACILITY";
		
		case Type.CHANGE_BOOKING:
			return "CHANGE_BOOKING";
			
		case Type.TRANSFER_BOOKING:
			return "TRANSFER_BOOKING";
			
		case Type.MODIFY_DURATION:
			return "MODIFY_BOOKING";
			
		case Type.GET_FACILITIES:
			return "GET_FACILITIES";
		
		case Type.CALLBACK:
			return "CALLBACK";
			
		default:
			return "UNKNOWN";
		}
	}
	
	/**
	 * @param facilities - List of all facilities
	 * @param facilityName - Given facility name
	 * @return whether the given facilityName exists in facilities
	 */
	public static boolean doesFacilityExist(List<Facility> facilities, String facilityName) {
		for (Facility facility : facilities) {
			if (facility.getName().equals(facilityName)) {
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Get facilityId of the given facilityName
	 */
	public static int facilityNameToId(List<Facility> facilities, String facilityName) {
		for (Facility facility : facilities) {
			if (facility.getName().equals(facilityName)) {
				return facility.getId();
			}
		}
		return -1;
	}

	/**
	 * Create a Booking object based on the content of a
	 * BookingFacilityRequest object.
	 */
	public static Booking getBookingFromRequest(
			BookFacilityRequest data, List<Facility> facilities) {
		Booking booking = new Booking();
		booking.setUserId(data.getUserId());
		booking.setFacilityId(facilityNameToId(facilities, data.getFacilityName()));
		booking.setBookingStart(data.getStartTime());
		booking.setBookingEnd(data.getEndTime());
		return booking;
	}
}
