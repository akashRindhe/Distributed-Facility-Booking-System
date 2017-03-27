package shared.webservice;

import java.security.Timestamp;

import shared.Marshallable;

public class BookFacilityRequest implements Marshallable {
	private String facilityName;
	private Timestamp startTime;
	private Timestamp endTime;
	
	public Timestamp getEndTime() {
		return endTime;
	}

	public void setEndTime(Timestamp endTime) {
		this.endTime = endTime;
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
}
