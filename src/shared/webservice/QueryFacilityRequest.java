package shared.webservice;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import shared.Marshallable;

public class QueryFacilityRequest implements Marshallable {
	private int facilityId;
	private List<Timestamp> days = new ArrayList<Timestamp>();
	
	
	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public List<Timestamp> getDays() {
		return days;
	}

	public void setDays(List<Timestamp> listDays) {
		this.days = listDays;
	}
	
}
