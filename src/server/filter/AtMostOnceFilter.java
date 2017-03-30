package server.filter;

import java.net.DatagramPacket;

import shared.webservice.Request;

public class AtMostOnceFilter implements Filter {

	@Override
	public boolean doFilter(Request request, DatagramPacket packet) {
		// add code here
		return true;
	}

}
