package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import controller.MyLogger;

public class Server extends AbstractMultichatServer implements MultichatServer, Runnable {
	private ServerSocket serveurSocket;
	private InetSocketAddress serveurIPSocket;
	
	// 
	// 
	/**
	 * @brief Cree un serveur TCP (objet de la classe ServerSocket), 
	 * 		  puis lance le thread du serveur. 
	 * @param address L'adresse IP qui va être associée au serveur
	 * @param port Le port qui va être associé au serveur
	 * @throws IOException
	 */
	public Server(InetAddress address, Integer port) throws IOException {
		super(address,port);
		//ServerSocket bind automatiquement sur le port et l'addresse du serveur:
		//this.serveurSocket = new ServerSocket(port);
		this.serveurSocket = new ServerSocket();
		this.serveurIPSocket = new InetSocketAddress(address, port);
	}

	/**
	 * @brief Le serveur ecoute et accepte les connections.
	 * 		  pour chaque connection, il cree un Service, qui va la traiter.
	 */
	public void run() {
		try {
			System.out.println("Lancement du serveur");
			this.serveurSocket.bind(this.serveurIPSocket);
			while(true){
				if (getService().isEmpty())
					getService().add(new Service(serveurSocket.accept()));
				else{
					Socket newSocket1 = serveurSocket.accept();	//On accepte la connection de la socket
					for (int i = 0; i < getService().size(); i++)
						getService().get(i).nouveauClient(newSocket1);	//On utilise une socket pour connecter le nouveau client aux autres clients existants
					getService().add(new Service(newSocket1, getAutresClients()));	//On stocke le service
				}
			}
		}
		catch (IOException e) { 
			try {this.serveurSocket.close();} catch (IOException e1) {MyLogger.errorMessage(e1.getMessage());}
			MyLogger.errorMessage("Pb sur le port d'écoute :"+e);
		}
		stop();
	}

	/**
	 * @brief Retourne tous les autres clients  connectés sur le chat
	 * @return la liste des autres socket connectées
	 */
	public Vector<Socket> getAutresClients() {
		Vector<Socket> autresClients = new Vector<Socket>();
		//todo: A modifier avec itérateur for( : ) :
		for(int i = 0; i < getService().size(); i++)
			autresClients.add(getService().get(i).getClient());
		return autresClients;
	}

	/**
	 * @brief Lance le thread du service qui va s'occuper d'un client
	 */
	public void start() {
		(new Thread(this)).start();		
	}
	
	/**
	 * @brief Stoppe le service
	 */
	public void stop() {
		try { 
			for(int i = 0; i < getService().size(); i++)
				getService().get(i).fermetureServeur();
			serveurSocket.close();
		} catch (IOException e) {
			MyLogger.errorMessage(e.getMessage());
		}
	}
}
