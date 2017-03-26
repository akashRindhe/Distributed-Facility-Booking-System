package shared.model;

import java.sql.Timestamp;

public class CallbackInfo implements DataModel {
	
	private int id;
	private String address;
	private int portNumber;
	private int facilityId;
	private Timestamp intervalStart;
	private Timestamp intervalEnd;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getAddress() {
		return address;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public int getPortNumber() {
		return portNumber;
	}

	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}

	public int getFacilityId() {
		return facilityId;
	}

	public void setFacilityId(int facilityId) {
		this.facilityId = facilityId;
	}

	public Timestamp getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(Timestamp intervalStart) {
		this.intervalStart = intervalStart;
	}

	public Timestamp getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(Timestamp intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

}
