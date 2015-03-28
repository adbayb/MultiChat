package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import controller.Main.MyLogger;
import model.server.Server;
import model.server.ServerNIO;

public class AppServer {
	private int port;
	private Server serveur;
	private InetAddress serverAddr;
	
	public AppServer(int port, InetAddress serverAddr){
		this.port = port;
		this.serveur = null;
		try {
			if(serverAddr == null){
				serverAddr = InetAddress.getLocalHost();
			}
			else{
				this.serverAddr = serverAddr;
			}
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			MyLogger.errorMessage(e.getMessage());
		}
	}
	
	public void execute() {
		try {
			//Serveur configuré Sans NIO:
			this.serveur = new Server(serverAddr, this.port);
			serveur.start();
		} catch (IOException e) {
			MyLogger.errorMessage("Error Server Creation on Port [" + this.port + "] " +  e);
		}
		
		return;
	}
	
	public void executeNIO() {
		try {
			//Serveur configuré Avec NIO:
			ServerNIO serveurNIO = new ServerNIO(serverAddr, this.port);
			serveurNIO.start();
		} catch (IOException e) {
			MyLogger.errorMessage("Error Server Creation on Port [" + this.port + "] " +  e);
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
