package shared.webservice;

import java.util.ArrayList;
import java.util.List;

import shared.Marshallable;
import shared.model.Booking;

public class QueryFacilityResponse implements Marshallable {
	private List<Booking> bookings = new ArrayList<Booking>();

	public List<Booking> getBookings() {
		return bookings;
	}

	public void setBookings(List<Booking> bookings) {
		this.bookings = bookings;
	}
}
