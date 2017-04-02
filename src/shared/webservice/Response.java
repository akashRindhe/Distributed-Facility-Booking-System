package shared.webservice;

import shared.Marshallable;

/**
 * This class encapsulates the data that is sent from the server to the client.
 */
public class Response implements Marshallable {
	// This flag determines if an error has occurred.
	private int isError;
	
	// This object contains the data for a particular request type. This field
	// is of type ErrorData if an error occurs.
	private Marshallable data;
	
	public Response () {
		
	}
	
	public Response(Marshallable data) {
		this.data = data;
		this.isError = 0;
	}
	
	public Response(String errorMessage) {
		ErrorData errorData = new ErrorData();
		errorData.setErrorType(errorMessage);
		this.isError = 1;
		this.data = errorData;
	}
	
	public int getIsError() {
		return isError;
	}
	
	public void setIsError(int isError) {
		this.isError = isError;
	}
	
	public Marshallable getData() {
		return data;
	}
	
	public void setData(Marshallable data) {
		this.data = data;
	}
}
