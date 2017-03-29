package shared.webservice;

import java.sql.Timestamp;

import shared.Marshallable;

public class ModifyDurationRequest implements Marshallable {
	private int bookingId;
	private Timestamp start;
	private Timestamp end;
	
	public int getBookingId() {
		return bookingId;
	}
	
	public void setBookingId(int bookingId) {
		this.bookingId = bookingId;
	}
	
	public Timestamp getStart() {
		return start;
	}
	
	public void setStart(Timestamp start) {
		this.start = start;
	}
	
	public Timestamp getEnd() {
		return end;
	}
	
	public void setEnd(Timestamp end) {
		this.end = end;
	}
}
