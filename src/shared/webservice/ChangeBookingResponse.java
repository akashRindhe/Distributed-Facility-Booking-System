package shared.webservice;

import shared.Marshallable;

public class ChangeBookingResponse implements Marshallable {
	private String acknowledgement;
	int success;

	public int getSuccess() {
		return success;
	}

	public void setSuccess(int success) {
		this.success = success;
	}
	
	public String getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}
	
}
