package shared.webservice;

import java.sql.Timestamp;

import shared.Marshallable;

public class CallbackRequest implements Marshallable {
	private int facilityId;
	private Timestamp monitorStart;
	private Timestamp monitorEnd;
	
	public int getFacilityId() {
		return facilityId;
	}
	
	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
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
