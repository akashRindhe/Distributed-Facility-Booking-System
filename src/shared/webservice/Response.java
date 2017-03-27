package shared.webservice;

import shared.Marshallable;

public class Response implements Marshallable {
	private int type;
	private int isError;
	private String errorString;
	private Marshallable data;
	
	public int getType() {
		return type;
	}
	
	public void setType(int type) {
		this.type = type;
	}
	
	public int getIsError() {
		return isError;
	}
	
	public void setIsError(int isError) {
		this.isError = isError;
	}
	
	public String getErrorString() {
		return errorString;
	}
	
	public void setErrorString(String errorString) {
		this.errorString = errorString;
	}
	
	public Marshallable getData() {
		return data;
	}
	
	public void setData(Marshallable data) {
		this.data = data;
	}
}
