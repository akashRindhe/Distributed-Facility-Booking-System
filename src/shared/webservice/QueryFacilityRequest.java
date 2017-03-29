package shared.webservice;

import java.sql.Timestamp;
import java.util.List;
import java.sql.Timestamp;

import shared.Marshallable;

public class QueryFacilityRequest implements Marshallable {
	private String facilityName;
	private List<Timestamp> days;
	
	public String getFacilityName() {
		return facilityName;
	}
	
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public List<Timestamp> getDays() {
		return days;
	}

	public void setDays(List<Timestamp> listDays) {
		this.days = listDays;
	}
	
}
