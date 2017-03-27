package shared.model;

import java.sql.Timestamp;
import shared.Marshallable;

public class Booking implements Marshallable {
	
	private int id;
	private int facilityId;
	private String userId;
	private Timestamp bookingStart;
	private Timestamp bookingEnd;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}

	public Timestamp getBookingStart() {
		return bookingStart;
	}

	public void setBookingStart(Timestamp bookingStart) {
		this.bookingStart = bookingStart;
	}

	public Timestamp getBookingEnd() {
		return bookingEnd;
	}

	public void setBookingEnd(Timestamp bookingEnd) {
		this.bookingEnd = bookingEnd;
	}
}
