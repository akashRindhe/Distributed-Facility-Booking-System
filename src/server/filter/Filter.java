package server.filter;

import shared.webservice.Request;

public interface Filter {
	boolean doFilter(Request request);
}
