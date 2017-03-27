package server.filter;

import shared.webservice.Request;

public class AtMostOnceFilter implements Filter {

	@Override
	public boolean doFilter(Request request) {
		// add code here
		return true;
	}

}
