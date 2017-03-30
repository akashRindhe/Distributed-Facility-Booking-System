package server.filter;

import java.net.DatagramPacket;

import shared.webservice.Request;

public interface Filter {
	boolean doFilter(Request request, DatagramPacket packet);
}
