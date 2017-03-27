package shared.webservice;

import shared.Marshallable;

public class Request implements Marshallable {
	private int requestType;
	private String requestId;
	private Marshallable requestData;
	
	public int getRequestType() {
		return requestType;
	}
	
	public void setRequestType(int requestType) {
		this.requestType = requestType;
	}

	public String getRequestId() {
		return requestId;
	}

	public void setRequestId(String requestId) {
		this.requestId = requestId;
	}

	public Marshallable getRequestData() {
		return requestData;
	}

	public void setRequestData(Marshallable requestData) {
		this.requestData = requestData;
	}
}
