package shared.model;

import java.util.*;

public class Request {
	private int requestID;
	private String ipAddress;
	private int portNo;
	private String serviceName;
	private int noOfArgs;
	private Map<String, String> arguements;
	
	
	public int getRequestID() {
		return requestID;
	}
	public void setRequestID(int requestID) {
		this.requestID = requestID;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPortNo() {
		return portNo;
	}
	public void setPortNo(int portNo) {
		this.portNo = portNo;
	}
	public String getServiceName() {
		return serviceName;
	}
	public void setServiceName(String serviceName) {
		this.serviceName = serviceName;
	}
	public int getNoOfArgs() {
		return noOfArgs;
	}
	public void setNoOfArgs(int noOfArgs) {
		this.noOfArgs = noOfArgs;
	}
	public Map<String, String> getArguements() {
		return arguements;
	}
	public void setArguements(Map<String, String> arguements) {
		this.arguements = arguements;
	}
	
}
