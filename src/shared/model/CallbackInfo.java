package shared.model;

import java.util.Date;

public class CallbackInfo implements DataModel {
	
	private int id;
	private String address;
	private int portNumber;
	private int facilityId;
	private Date intervalStart;
	private Date intervalEnd;

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

	public Date getIntervalStart() {
		return intervalStart;
	}

	public void setIntervalStart(Date intervalStart) {
		this.intervalStart = intervalStart;
	}

	public Date getIntervalEnd() {
		return intervalEnd;
	}

	public void setIntervalEnd(Date intervalEnd) {
		this.intervalEnd = intervalEnd;
	}

}
