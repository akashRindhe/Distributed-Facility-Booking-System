package shared.webservice;

import shared.Marshallable;

public class TransferBookingRequest implements Marshallable {
	private int bookingId;
	private String newUserId;
	
	public int getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public String getNewUserId() {
		return newUserId;
	}
	
	public void setNewUserId(String newUserId) {
		this.newUserId = newUserId;
	}
}
