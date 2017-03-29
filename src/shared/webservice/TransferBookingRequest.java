package shared.webservice;

import shared.Marshallable;

public class TransferBookingRequest implements Marshallable {
	private int bookingId;
	private int newUserId;
	
	public int getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getNewUserId() {
		return newUserId;
	}
	
	public void setNewUserId(int newUserId) {
		this.newUserId = newUserId;
	}
}
