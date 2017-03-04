package shared.model;

public class User implements DataModel{
	
	private int id;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

}
