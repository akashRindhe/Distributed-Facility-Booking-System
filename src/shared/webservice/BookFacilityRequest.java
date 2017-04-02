package shared.webservice;

import java.sql.Timestamp;

import shared.Marshallable;

public class BookFacilityRequest implements Marshallable {
	private String facilityName;
	private Timestamp startTime;
	private Timestamp endTime;
	private String userId;
	
	public BookFacilityRequest(){
		
	}

	public String getFacilityName() {
		return facilityName;
	}

	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public Timestamp getStartTime() {
		return startTime;
	}

	public void setStartTime(Timestamp startTime) {
		this.startTime = startTime;
	}

	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
	}

	public String getUserId() {
		return userId;
	}

	public void setUserId(String userId) {
		this.userId = userId;
	}
}
