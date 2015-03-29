package com.adibsarr.multichat.controller.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import com.adibsarr.multichat.controller.MyLogger;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

import com.adibsarr.multichat.model.client.ClientMulticast;
import com.adibsarr.multichat.model.client.Client;

public class AppClient {
	private String hostname;
	private int port;
	private Client client;
	private Socket socket;
	private boolean isMulticast;
	private ClientMulticast clientMulticast;
	
	/**
	 * @brief Gestion du client qui va être connecté à un serveur
	 * @param hostname Addresse ip ou est connecté le serveur
	 * @param port Port ou est conencté le serveur
	 * @param windowChat Fenêtre JavaFX montrant l'ensemble des messages
	 * @param listUserOnline ListView<String> Gestion de la buddy List
	 * @param isMulticast Booleen indiquant so on se trouve dans une conenxion multicast
	 */
	public AppClient(String hostname, int port, TextArea windowChat, ListView<String> listUserOnline, boolean isMulticast) {
		this.hostname = hostname;
		this.port = port;
		this.isMulticast = isMulticast;
		
		try {
			if(isMulticast == false) {
				InetAddress serverAddr = InetAddress.getByName(this.hostname);
				this.socket = new Socket(serverAddr, this.port);
				this.client = new Client(this.socket, windowChat, listUserOnline);
			}
			else {
				//Adresse virtuelle!: exemple: 228.45.56.1 et port 1777
				this.clientMulticast = new ClientMulticast(hostname,port,windowChat);
			}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MyLogger.errorMessage(("Error Connexion to Server [" + this.hostname + "] " +  e));
			//On quitte en cas d'exception (MyLogger ne quitte pas le programme directement après avoir loggué):
			System.exit(0);
		}
		//System.out.println("Client Created");
	}
	
	public void execute() {
		if(this.isMulticast == false) {
			this.client.launch();
		}
		else {
			this.clientMulticast.launch();
		}
		
		return;
	}
	
	/**
	 * @brief Gestion des input client (listener button)
	 * @param windowChat Fenêtre JavaFX montrant l'ensemble des messages
	 * @param saisie Champ ou l'utilisateur a rentré son message
	 * @return
	 */
	public InputClientListener getAddInputClient(TextArea windowChat, TextField saisie) {
		if(this.isMulticast == false) 
			return new InputClientListener(this.client, null, windowChat, saisie);
		
		return new InputClientListener(null, this.clientMulticast, windowChat, saisie);
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

	public Client getClient() {
		return client;
	}

	public void setClient(Client client) {
		this.client = client;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}

	public boolean isMulticast() {
		return isMulticast;
	}

	public void setMulticast(boolean isMulticast) {
		this.isMulticast = isMulticast;
	}

	public ClientMulticast getClientMulticast() {
		return clientMulticast;
	}

	public void setClientMulticast(ClientMulticast clientMulticast) {
		this.clientMulticast = clientMulticast;
	}
}
