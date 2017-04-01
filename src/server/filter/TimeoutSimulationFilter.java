package server.filter;

import java.net.DatagramPacket;

import server.Server;
import shared.webservice.Request;

public class TimeoutSimulationFilter implements Filter {
	
	@Override
	public boolean doFilter(Request request, DatagramPacket packet) {
		if (Server.getInstance().getRequestCount() % 3 == 1) {
			System.out.println("Simulating request failure");
			return false;
		}
		return true;
	}

}
