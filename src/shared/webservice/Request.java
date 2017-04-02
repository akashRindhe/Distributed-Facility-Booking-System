package shared.webservice;

import shared.Marshallable;

/**
 * This class encapsulates the data that is sent from the client to the server.
 */
public class Request implements Marshallable {
	private int requestType;
	private String requestId;
	
	// This object contains the data for a particular request type. For
	// example, if the request type is GET_FACILITIES, the type of this
	// object would be GetFacilitiesRequest.
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
