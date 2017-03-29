package shared.webservice;

import java.util.ArrayList;
import java.util.List;

import shared.Marshallable;
import shared.model.Facility;

public class GetFacilitiesResponse implements Marshallable {
	private List<Facility> facilities = new ArrayList<Facility>();

	public List<Facility> getFacilities() {
		return facilities;
	}

	public void setFacilities(List<Facility> facilities) {
		this.facilities = facilities;
	}
}
