package shared.webservice;

import shared.Marshallable;

public class ErrorData implements Marshallable {
	private String errorType;

	public String getErrorType() {
		return errorType;
	}

	public void setErrorType(String errorType) {
		this.errorType = errorType;
	}
}
