package shared.webservice;

import java.sql.Timestamp;

import shared.Marshallable;

public class CallbackRequest implements Marshallable {
	private String facilityName;
	private Timestamp monitorStart;
	private Timestamp monitorEnd;
	
	public String getFacilityId() {
		return facilityName;
	}
	
	public void setFacilityId(String facilityName) {
		this.facilityName = facilityName;
	}

	public Timestamp getMonitorStart() {
		return monitorStart;
	}

	public void setMonitorStart(Timestamp monitorStart) {
		this.monitorStart = monitorStart;
	}

	public Timestamp getMonitorEnd() {
		return monitorEnd;
	}

	public void setMonitorEnd(Timestamp monitorEnd) {
		this.monitorEnd = monitorEnd;
	}
}
