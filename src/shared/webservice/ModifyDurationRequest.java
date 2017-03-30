package shared.webservice;

import shared.Marshallable;

public class ModifyDurationRequest implements Marshallable {
	private int bookingId;
	private int offset;
	
	public int getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}

	public int getOffset() {
		return offset;
	}

	public void setOffset(int offset) {
		this.offset = offset;
	}
}
