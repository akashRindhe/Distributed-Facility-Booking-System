package server;

import java.util.List;

import shared.model.Booking;
import shared.model.Facility;
import shared.webservice.BookFacilityRequest;

public class Utility {
	
	public static boolean doesFacilityExist(List<Facility> facilities, String facilityName) {
		for (Facility facility : facilities) {
			if (facility.getName().equals(facilityName)) {
				return true;
			}
		}
		return false;
	}
	
	public static int facilityNameToId(List<Facility> facilities, String facilityName) {
		for (Facility facility : facilities) {
			if (facility.getName().equals(facilityName)) {
				return facility.getId();
			}
		}
		return -1;
	}

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
