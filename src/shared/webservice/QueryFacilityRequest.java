package shared.webservice;

import java.util.List;

import shared.Marshallable;

public class QueryFacilityRequest implements Marshallable {
	private String facilityName;
	private List<String> days;
	
	public String getFacilityName() {
		return facilityName;
	}
	
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public List<String> getDays() {
		return days;
	}

	public void setDays(List<String> days) {
		this.days = days;
	}
	
}
