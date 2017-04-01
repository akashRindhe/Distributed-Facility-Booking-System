package server.filter;

import java.io.IOException;
import java.net.DatagramPacket;

import server.History;
import server.Server;
import shared.webservice.Request;

public class AtMostOnceFilter implements Filter {

	@Override
	public boolean doFilter(Request request, DatagramPacket packet) {
		History hist = Server.getInstance().getHistory();
		if (hist.contains(request)) {
			try {
				Server
					.getInstance()
					.sendResponse(hist.getResponse(request), packet.getAddress(), packet.getPort());
			} catch (IllegalArgumentException | IllegalAccessException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			return false;
		}
		return true;
	}

}
