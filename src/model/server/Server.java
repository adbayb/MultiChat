package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import controller.Main.MyLogger;

public class Server extends AbstractMultichatServer implements MultichatServer, Runnable {
	private ServerSocket serveurSocket;
	private InetSocketAddress serveurIPSocket;
	
	// Cree un serveur TCP - objet de la classe ServerSocket
	// Puis lance le thread du serveur.
	public Server(InetAddress address, Integer port) throws IOException {
		super(address,port);
		//ServerSocket bind automatiquement sur le port et l'addresse du serveur:
		//this.serveurSocket = new ServerSocket(port);
		//this.serveurSocket = new ServerSocket(port);
		this.serveurSocket = new ServerSocket();
		this.serveurIPSocket = new InetSocketAddress(address, port);
	}

	// Le serveur ecoute et accepte les connections.
	// pour chaque connection, il cree un Service, 
	// qui va la traiter.
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

	public Vector<Socket> getAutresClients() {
		Vector<Socket> autresClients = new Vector<Socket>();
		//todo: A modifier avec itérateur for( : ) :
		for(int i = 0; i < getService().size(); i++)
			autresClients.add(getService().get(i).getClient());
		return autresClients;
	}
	
	/*protected void finalize() throws Throwable {
		try {
			this.serveurSocket.close();
		} 
		catch (IOException e1) {
			
		}
	}*/

	public void start() {
		(new Thread(this)).start();		
	}
	
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
