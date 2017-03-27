package server;

import shared.webservice.Request;
import shared.webservice.Response;
import shared.webservice.Type;

public class Controller {
	
	public Response processRequest(Request request) {
		Response response;
		switch (request.getRequestType()) {
		case Type.QUERY_FACILITY:
			response = queryFacility(request);
			break;

		default:
			response = new Response("An unknown error has occurred");
			break;
		}
		return response;
	}
	
	private Response queryFacility(Request request) {
		return null;
	}
}
