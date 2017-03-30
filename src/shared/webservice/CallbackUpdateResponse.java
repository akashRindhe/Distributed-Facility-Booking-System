package shared.webservice;

import java.util.LinkedList;
import java.util.List;

import shared.model.Booking;

public class CallbackUpdateResponse {
	private List<Booking> bookingList = new LinkedList<>();

	public List<Booking> getBookingList() {
		return bookingList;
	}

	public void setBookingList(List<Booking> bookingList) {
		this.bookingList = bookingList;
	}
}
