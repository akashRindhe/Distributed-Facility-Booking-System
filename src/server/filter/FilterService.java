package server.filter;

import java.net.DatagramPacket;
import java.util.Iterator;
import java.util.LinkedList;

import shared.webservice.Request;

/**
 * This class implements the filtering logic.
 */
public class FilterService {
	// List of filters the server has added.
	private LinkedList<Filter> filters;
	
	public FilterService() {
		filters = new LinkedList<>();
	}
	
	/**
	 * This method sequentially runs filters in the order in which they were
	 * added to the list. The filtering process takes place in the form of a
	 * pipeline. A request is blocked if it is rejected by any of the filters.
	 */
	public boolean performFiltering(Request request, DatagramPacket packet) {
		Iterator<Filter> iter = filters.iterator();
		boolean filterSuccess = true;
		
		while(iter.hasNext()) {
			filterSuccess = iter.next().doFilter(request, packet);
			if (!filterSuccess) {
				break;
			}
		}
		
		return filterSuccess;
	}
	
	public void addFilter(Filter filter) {
		filters.add(filter);
	}
}
