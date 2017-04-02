package server.filter;

import java.net.DatagramPacket;

import shared.webservice.Request;

/**
 * This interface specifies the structure of a Filter. A filter is responsible
 * for determining whether a particular request should be allowed to pass
 * through or not, that is, it filters out requests.
 */
public interface Filter {
	/**
	 * This executes the logic of a Filter. It returns true if the request
	 * should be allowed to pass through. It returns false if the request
	 * should be blocked.
	 */
	boolean doFilter(Request request, DatagramPacket packet);
}
