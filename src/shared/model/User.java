package shared.model;

public class User implements DataModel, Marshallable {
	
	private int id;
	private String email;

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	@Override
	public byte[] toByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Marshallable fromByteArray() {
		// TODO Auto-generated method stub
		return null;
	}

}
