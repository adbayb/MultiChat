package controller.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import controller.MyLogger;
import model.server.Server;
import model.server.ServerNIO;

public class AppServer {
	private int port;
	private Server serveur;
	private InetAddress serverAddr;
	
	/**
	 * @brief Préparation de l'execution du serveur
	 * @param serverAddr Addresse ip qui va être associée au serveur
	 * @param port Port qui va être associé au serveur
	 */
	public AppServer(InetAddress serverAddr, int port) {
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
	
	/**
	 * @brief Execution du serveur classique
	 */
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

	/**
	 * @brief Execution du serveur NIO
	 */
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
