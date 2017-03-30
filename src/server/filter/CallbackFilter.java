package server.filter;

import java.io.IOException;
import java.net.DatagramPacket;
import java.sql.Timestamp;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import server.Controller;
import server.DatabaseAccess;
import server.Server;
import shared.model.Facility;
import shared.webservice.CallbackRequest;
import shared.webservice.QueryFacilityRequest;
import shared.webservice.Request;
import shared.webservice.Response;
import shared.webservice.Type;

public class CallbackFilter implements Filter {
	
	private static HashMap<Integer, LinkedList<MonitorWrapper>> map = new HashMap<>();
	
	@Override
	public boolean doFilter(Request request, DatagramPacket packet) {
		if (request.getRequestType() == Type.CALLBACK) {
			MonitorWrapper wrapper = new MonitorWrapper();
			wrapper.clientPacket = packet;
			wrapper.requestData = (CallbackRequest) request.getRequestData();
			if (map.containsKey(wrapper.requestData.getFacilityId())) {
				map.get(wrapper.requestData.getFacilityId()).add(wrapper);
			} else {
				LinkedList<MonitorWrapper> list = new LinkedList<>();
				list.add(wrapper);
				map.put(wrapper.requestData.getFacilityId(), list);
			}
		}
		return true;
	}

	public static void broadcastUpdate(int facilityId) {
		Request request = new Request();
		request.setRequestType(Type.QUERY_FACILITY);
		request.setRequestData(createQueryFacilityRequest(facilityId));
		Response response = (new Controller()).processRequest(request);
		List<MonitorWrapper> list = map.get(facilityId);
		if (list != null) {
			Iterator<MonitorWrapper> iter = list.iterator();
			while(iter.hasNext()) {
				MonitorWrapper wrapper = iter.next();
				Timestamp current = new Timestamp(System.currentTimeMillis());
				if (current.compareTo(wrapper.requestData.getMonitorStart()) > 0
						&& current.compareTo(wrapper.requestData.getMonitorEnd()) < 0) {
					try {
						Server
							.getInstance()
							.sendResponse(
									response, 
									wrapper.clientPacket.getAddress(), 
									wrapper.clientPacket.getPort());
					} catch (IllegalArgumentException | IllegalAccessException | IOException e) {
						System.out.println("Error broadcasting to client: "
								+ wrapper.clientPacket.getAddress().getHostAddress());
						e.printStackTrace();
					}
				}
			}
		}
	}
	
	private static QueryFacilityRequest createQueryFacilityRequest(int facilityId) {
		QueryFacilityRequest req = new QueryFacilityRequest();
		Facility facility = DatabaseAccess.fetchFacilityById(facilityId);
		req.setFacilityId(facility.getId());
		Calendar cal = Calendar.getInstance();
		List<Timestamp> timeList = new LinkedList<>();
		for (int i = 0; i < 7; i++) {
			timeList.add(new Timestamp(cal.getTimeInMillis()));
			cal.add(Calendar.DATE, 1);
		}
		req.setDays(timeList);
		return req;
	}
	
	private class MonitorWrapper {
		DatagramPacket clientPacket;
		CallbackRequest requestData;
	}
}
