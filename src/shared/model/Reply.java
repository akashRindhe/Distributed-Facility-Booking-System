package shared.model;

import java.util.*;

public class Reply {
	private int replyID;
	private String ipAddress;
	private int portNumber;
	private boolean isList;
	private String message;
	private ArrayList<Facility> availableTimings;
	
	public int getReplyID() {
		return replyID;
	}
	public void setReplyID(int replyID) {
		this.replyID = replyID;
	}
	public String getIpAddress() {
		return ipAddress;
	}
	public void setIpAddress(String ipAddress) {
		this.ipAddress = ipAddress;
	}
	public int getPortNumber() {
		return portNumber;
	}
	public void setPortNumber(int portNumber) {
		this.portNumber = portNumber;
	}
	public boolean isList() {
		return isList;
	}
	public void setList(boolean isList) {
		this.isList = isList;
	}
	public String getMessage() {
		return message;
	}
	public void setMessage(String message) {
		this.message = message;
	}
	public ArrayList<Facility> getAvailableTimings() {
		return availableTimings;
	}
	public void setAvailableTimings(ArrayList<Facility> availableTimings) {
		this.availableTimings = availableTimings;
	}
	
	
}
