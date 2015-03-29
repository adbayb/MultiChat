package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

public abstract class AbstractMultichatServer {
	private Vector<Service> servicesCli;
	
	/**
	 * @brief Classe abstraite représentant les principales données du serveur : l'addressse IP et le port
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	AbstractMultichatServer(InetAddress address, Integer port){
		this.servicesCli = new Vector<Service>();
		System.out.println("Server Initialized ["+address+":"+port+"]");
	}

	/**
	 * @brief Retourne tous les autres clients  connectés sur le chat
	 * @return la liste des autres socket connectées
	 */
	public Vector<Socket> getAutresClients() {
		Vector<Socket> autresClients = new Vector<Socket>();
		//todo: A modifier avec itérateur for( : ) :
		for(int i = 0; i < servicesCli.size(); i++)
			autresClients.add(servicesCli.get(i).getClient());
		return autresClients;
	}
	
	public Vector<Service> getService(){
		return servicesCli;
	}
}
