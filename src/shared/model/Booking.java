package shared.model;

import java.util.Date;

public class Booking implements DataModel {
	
	private int id;
	private int facilityId;
	private int userId;
	private Date bookingStart;
	private Date bookingEnd;

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

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public Date getBookingStart() {
		return bookingStart;
	}

	public void setBookingStart(Date bookingStart) {
		this.bookingStart = bookingStart;
	}

	public Date getBookingEnd() {
		return bookingEnd;
	}

	public void setBookingEnd(Date bookingEnd) {
		this.bookingEnd = bookingEnd;
	}

}
