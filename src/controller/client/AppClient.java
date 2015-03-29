package controller.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

import controller.MyLogger;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import model.client.Client;

public class AppClient {
	private String hostname;
	private int port;
	private Client client;
	//Représente le cadre visuel Chat Output/Input de notre application Client:
	private TextArea windowChat;
	private Socket socket;
	
	public AppClient(String hostname, int port, TextArea windowChat) {
		this.hostname = hostname;
		this.port = port;
		
		try {
			InetAddress serverAddr = InetAddress.getByName(this.hostname);
			this.socket = new Socket(serverAddr, this.port);
			this.client = new Client(this.socket, windowChat);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			MyLogger.errorMessage(("Error Connexion to Server [" + this.hostname + "] " +  e));
			//On quitte en cas d'exception (MyLogger ne quitte pas le programme directement après avoir loggué):
			System.exit(0);
		}
		//System.out.println("Client Created");
	}
	
	public void execute() {
		this.client.launch();
		
		return;
	}
	
	//Gestion des input client (listener button):
	public InputClientListener getAddInputClient(TextArea windowChat, TextField saisie) {
		return new InputClientListener(this.client, windowChat, saisie);
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

	public TextArea getWindowChat() {
		return windowChat;
	}

	public void setWindowChat(TextArea windowChat) {
		this.windowChat = windowChat;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
