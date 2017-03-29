package shared.webservice;

import java.util.List;

import shared.DayOfWeek;
import shared.Marshallable;

public class QueryFacilityRequest implements Marshallable {
	private String facilityName;
	private List<DayOfWeek> days;
	
	public String getFacilityName() {
		return facilityName;
	}
	
	public void setFacilityName(String facilityName) {
		this.facilityName = facilityName;
	}

	public List<DayOfWeek> getDays() {
		return days;
	}

	public void setDays(List<DayOfWeek> listDays) {
		this.days = listDays;
	}
	
}
