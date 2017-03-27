package shared.model;

import shared.Marshallable;

public class User implements Marshallable{
	
	private String id;
	private String userName;
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}
}
