package server;

import java.util.HashMap;

import shared.webservice.Request;
import shared.webservice.Response;

public class History {
	
	private HashMap<String, Response> history = new HashMap<>();
	
	public boolean contains(Request request) {
		return history.containsKey(request.getRequestId());
	}
	
	public Response getResponse(Request request) {
		return history.get(request.getRequestId());
	}
	
	public void put(Request request, Response response) {
		history.put(request.getRequestId(), response);
	}
}
