package shared.webservice;

import java.sql.Timestamp;

import shared.Marshallable;

public class BookFacilityRequest implements Marshallable {
	private String facilityName;
	private Timestamp startTimeStamp;
	private Timestamp endTimeStamp;
	
	public Timestamp getEndTimeStamp() {
		return endTimeStamp;
	}

	public void setEndTimeStamp(Timestamp endTime) {
		this.endTimeStamp = endTime;
	}

	public String getFacilityName() {
		return facilityName;
	}
	
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
	public Timestamp getStartTimeStamp() {
		return startTimeStamp;
	}
	
	public void setStartTimeStamp(Timestamp startTime) {
		this.startTimeStamp = startTime;
	}
}
