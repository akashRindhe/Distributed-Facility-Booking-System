package server;

import java.util.HashMap;

import shared.webservice.Request;
import shared.webservice.Response;

/**
 * This class maintains a history of requests using a HashMap.
 * This is applicable only when the server is started with
 * AtMostOnce invocation semantics.
 */
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
