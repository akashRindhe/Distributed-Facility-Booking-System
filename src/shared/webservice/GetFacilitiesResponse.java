package shared.webservice;

import java.util.List;

import shared.Marshallable;
import shared.model.Facility;

public class GetFacilitiesResponse implements Marshallable {
	private List<Facility> facilities;

	public List<Facility> getBookings() {
		return facilities;
	}

	public void setBookings(List<Facility> facilities) {
		this.facilities = facilities;
	}
}
