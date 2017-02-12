package shared.model;

public interface Marshallable {
	byte[] toByteArray();
	Marshallable fromByteArray();
}
