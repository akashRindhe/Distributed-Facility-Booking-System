package shared.webservice;

import shared.Marshallable;

public class QueryFacilityRequest implements Marshallable {
	private String facilityName;
	private int numDays;
	
	public String getFacilityName() {
		return facilityName;
	}
	
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}
	
	public int getNumDays() {
		return numDays;
	}
	
	public void setNumDays(int numDays) {
		this.numDays = numDays;
	}
}
