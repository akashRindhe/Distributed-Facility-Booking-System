package shared.webservice;

import shared.Marshallable;

public class ChangeBookingResponse implements Marshallable {
	private String acknowledgement;
	
	public String getAcknowledgement() {
		return acknowledgement;
	}

	public void setAcknowledgement(String acknowledgement) {
		this.acknowledgement = acknowledgement;
	}
	
}
