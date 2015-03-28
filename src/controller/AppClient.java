package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import model.client.Client;

public class AppClient {
	private String hostname;
	private int port;
	private Client client;
	
	public AppClient(String hostname, int port) {
		this.hostname = hostname;
		this.port = port;
		this.client = null;
	}
	
	public void execute() {
		try {
			InetAddress serverAddr = InetAddress.getByName(this.hostname);
			Socket socket = new Socket(serverAddr, this.port);
			this.client = new Client(socket);
			this.client.lancer();
		} catch (IOException e) {
				System.err.println("Error Connexion to Server [" + this.hostname + "] " +  e);			
		}
		
		return;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}
}
