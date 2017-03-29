package shared.webservice;

import shared.Marshallable;

public class Response implements Marshallable {
	private int isError;
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
