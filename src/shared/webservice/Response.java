package shared.webservice;

import shared.Marshallable;

public class Response implements Marshallable {
	private int type;
	private int isError;
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
	
	public Marshallable getData() {
		return data;
	}
	
	public void setData(Marshallable data) {
		this.data = data;
	}
}
