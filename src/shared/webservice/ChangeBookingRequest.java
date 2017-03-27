package shared.webservice;

import shared.Marshallable;

public class ChangeBookingRequest implements Marshallable {
	private int confirmationId;
	private int offset;
	
	public int getConfirmationId() {
		return confirmationId;
	}
	
	public void setConfirmationId(int confirmationId) {
		this.confirmationId = confirmationId;
	}
	
	public int getOffset() {
		return offset;
	}
	
	public void setOffset(int offset) {
		this.offset = offset;
	}
}
