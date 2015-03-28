package model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import controller.Main.MyLogger;

final class Service implements Runnable{
	
	private static int compteur = 1;
	private int numero;
	private Socket socket;
	private Vector<Socket> autresClients;
	String nickname = null;
	
	private final Object lock = new Object();

	public Service(Socket socket){
		this.numero = compteur++;
		this.socket = socket;
		autresClients = new Vector<Socket>();
		
		(new Thread(this)).start();
	}
	
	public Service(Socket affichage, Vector<Socket> autresClients) {
		this.numero = compteur++;
		this.socket = affichage;
		this.autresClients = new Vector<Socket>();
		for(int i = 0; i < autresClients.size(); i++){
			if(autresClients.get(i).getInetAddress() != affichage.getInetAddress())
				this.autresClients.add(autresClients.get(i));
		}
		
		(new Thread(this)).start();
	}	

	@Override
	public void run() {
		System.out.print("Nouveau client : " + this.socket.getInetAddress());
		try {BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream ( )));
		PrintWriter out = new PrintWriter (socket.getOutputStream (), true);
		out.println("Welcome to the chat !!! (" + socket.getInetAddress() + " port : " + socket.getLocalPort() + ")");
		
		boolean messageConnection = true;
		while (true) {
			// lit la ligne
			String line = in.readLine();
			synchronized(lock){
				//on recupere le contenu du message (sans le login et les chevrons) pour le traitement
				String contenu = line.substring(line.lastIndexOf(">") + 2);
				if(contenu.length() > 5){
					System.out.println(contenu.substring(0,4));
					if(contenu.substring(0,4).equals("nick")){
						String nickName = contenu.substring(5,contenu.length());
						this.nickname = nickName;
						out.println("Nickname created : " + nickName);
					}	
					else{								
						diffusionMessage(line, out, messageConnection);
						messageConnection = false;
					}
				}
				else{
					diffusionMessage(line, out, messageConnection);
					messageConnection = false;				
				}				
			}
		}
	}
	catch (IOException e) {
		System.err.println("Fin du service " + this.numero + " : " + e);
	}
		deconnection();
	}
	
	public void diffusionMessage(String line, PrintWriter out, boolean messageConnection){
		System.out.println("Service " + numero + " a recu " + line);
		boolean socketFermee = false;	//variable permettant de signaler si on peut ecrire sur une socket
		if (!messageConnection){
			if(this.nickname == null){
				out.println(line);
			}
			else{
				String name = line.substring(0, line.lastIndexOf(">") - 1);
				line = this.nickname + ">>" + line.substring(line.lastIndexOf(">") + 2);
				//line.replace(name, this.nickname);
				out.println(line);
			}
		}
		for (int i = 0; i < autresClients.size(); i++){
			socketFermee = false;
			try{
				out = new PrintWriter (autresClients.get(i).getOutputStream (), true);
			}catch(IOException e){
				/**
				 * Si une exception est lev�e sur cette socket, cela veut dire que le client
				 * précedement connecté à cette socket s'est deconnecté
				 */
				socketFermee = true;
				//On retire cette socket
				autresClients.removeElementAt(i);
				--i;	//on decremente i car tout les elements du vector reculent de 1
			}
			if (!socketFermee){
				if(this.nickname == null){
					out.println(line);
				}
				else{
					String name = line.substring(0, line.lastIndexOf(">") - 1);
					line = this.nickname + ">>" + line.substring(line.lastIndexOf(">") + 2);
					//line.replace(name, this.nickname);					
					out.println(line);
				}
			}
		}	
		
	}
	
	public Socket getClient(){
		return socket;
	}
	
	public Vector<Socket> getAutresClients(){
		return autresClients;
	}
	
	public void nouveauClient(Socket newSocket){
		autresClients.add(newSocket);
	}
	
	public void fermetureServeur(){
		try {
			PrintWriter out = new PrintWriter (socket.getOutputStream (), true);
			out.println("*** ATTENTION FERMETURE DU TCHAT ***");
			deconnection();
		} catch (IOException e) {
			MyLogger.errorMessage(e.getMessage());
		}
	}
	
	public void deconnection(){
		try{
			PrintWriter out = new PrintWriter (socket.getOutputStream (), true);
			out.println("*** DECONNECTION ***");
			socket.close();
		}catch(IOException e){
			MyLogger.errorMessage(e.getMessage());
		}
	}
	
	protected void finalize() throws Throwable {
		socket.close(); 
	}

}
