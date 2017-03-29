package client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;

import server.Controller;

public class Client {
	
	private int port;
	private DatagramSocket socket;
	private Controller controller;
	
	Client (int port, InetAddress address) {
		this.port = port;
		this.controller = new Controller();
	}
	
	public void start() throws IOException {
		socket = new DatagramSocket(port);
		byte[] buf = new byte[1024];
		DatagramPacket packet = new DatagramPacket(buf, buf.length);
		
		while(true) {
			try {
				socket.send(packet);
				
			} catch (Exception e) {
				
			}
			
		}
	}
	
	public void finish() {
		this.socket.close();
	}

}
