package server.filter;

import java.net.DatagramPacket;

import shared.webservice.Request;

public class TimeoutSimulationFilter implements Filter {
	
	private int requestCount = 0;
	
	@Override
	public boolean doFilter(Request request, DatagramPacket packet) {
		requestCount++;
		if (requestCount % 3 == 0) {
			requestCount = 0;			
			return true;
		}
		System.out.println("Simulating request failure");
		return false;
	}

}
