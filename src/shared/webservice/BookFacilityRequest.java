package shared.webservice;

import shared.Marshallable;
import shared.model.Booking;

public class BookFacilityRequest implements Marshallable {
	private Booking booking;
	
	public BookFacilityRequest(){
		
	}
	
	public BookFacilityRequest(Booking booking) {
		this.booking = booking;
	}

	public Booking getBooking() {
		return booking;
	}

	public void setBooking(Booking booking) {
		this.booking = booking;
	}
	
	
}
