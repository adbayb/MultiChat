package controller;

import java.io.IOException;
import java.net.InetAddress;

import model.server.Server;
import model.server.ServerNIO;

public class AppServer {
	private int port;
	private Server serveur;
	
	public AppServer(int port) {
		this.port = port;
		this.serveur = null;
	}
	
	public void execute() {
		try {
			InetAddress serverAddr = InetAddress.getLocalHost();
			//Serveur configuré Sans NIO:
			this.serveur = new Server(serverAddr, this.port);
			serveur.start();
		} catch (IOException e) {
			System.err.println("Error Server Creation on Port [" + this.port + "] " +  e);			
		}
		
		return;
	}
	
	public void executeNIO() {
		try {
			InetAddress serverAddr = InetAddress.getLocalHost();
			//Serveur configuré Avec NIO:
			ServerNIO serveurNIO = new ServerNIO(serverAddr, this.port);
			serveurNIO.start();
		} catch (IOException e) {
			System.err.println("Error Server Creation on Port [" + this.port + "] " +  e);			
		}
		
		return;
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public Server getServeur() {
		return serveur;
	}

	public void setServeur(Server serveur) {
		this.serveur = serveur;
	}
}
