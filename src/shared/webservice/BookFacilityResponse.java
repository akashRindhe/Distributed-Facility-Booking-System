package shared.webservice;

import shared.Marshallable;

public class BookFacilityResponse implements Marshallable {
	private int confirmationId;

	public int getConfirmationId() {
		return confirmationId;
	}

	public void setConfirmationId(int confirmationId) {
		this.confirmationId = confirmationId;
	}
}
