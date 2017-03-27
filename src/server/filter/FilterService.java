package server.filter;

import java.util.Iterator;
import java.util.LinkedList;

import shared.webservice.Request;

public class FilterService {
	private LinkedList<Filter> filters;
	
	public FilterService() {
		filters = new LinkedList<>();
	}
	
	public boolean performFiltering(Request request) {
		Iterator<Filter> iter = filters.iterator();
		boolean filterSuccess = true;
		
		while(iter.hasNext()) {
			filterSuccess = iter.next().doFilter(request);
			if (!filterSuccess) {
				break;
			}
		}
		
		return filterSuccess;
	}
}
