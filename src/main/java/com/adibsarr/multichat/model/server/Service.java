package com.adibsarr.multichat.model.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Vector;

import com.adibsarr.multichat.controller.MyLogger;

final class Service implements Runnable{
	
	private static int compteur = 1;
	private int numero;
	private Socket socket;
	private Vector<Socket> autresClients;
	private String nickname = null;
	private boolean connected;
	
	private final Object lock = new Object();

	/**
	 * @brief Service qui va s'occuper s'un client
	 * @param socket Socket qui va être relié au client
	 */
	public Service(Socket socket){
		this.numero = compteur++;
		this.socket = socket;
		autresClients = new Vector<Socket>();
		connected = true;
		(new Thread(this)).start();
	}
	
	/**
	 * @brief Initialisation du service avec une liste représentant les autres socket
	 * @param affichage socket relié au client de se service
	 * @param autresClients autres socket qui sont chacunes reliées à un client
	 */
	public Service(Socket affichage, Vector<Socket> autresClients) {
		this.numero = compteur++;
		this.socket = affichage;
		this.autresClients = new Vector<Socket>();
		connected = true;
		for(int i = 0; i < autresClients.size(); i++){
			if(autresClients.get(i).getInetAddress() != affichage.getInetAddress())
				this.autresClients.add(autresClients.get(i));
		}
		
		(new Thread(this)).start();
	}	

	/**
	 * @brief gestion de l'envoi et la réception des messages clients
	 */
	public void run() {
		System.out.println("Nouveau client : " + this.socket.getInetAddress());
		try {BufferedReader in = new BufferedReader (new InputStreamReader(socket.getInputStream ( )));
		PrintWriter out = new PrintWriter (socket.getOutputStream (), true);
		out.println("Welcome to ADIB and SARR Tchat !!! [version 1.0] (" + socket.getInetAddress() + " port : " + socket.getLocalPort() + ")");
		out.flush();
		
		while (connected) {
			// lit la ligne
			String line = in.readLine();
			if(line != null){
				synchronized(lock){
					if(line.toLowerCase().contains("/nick ") == true) {
						String nickname = line.substring(line.indexOf("/nick ")+"/nick ".length(), line.length());
						this.nickname = nickname;
						System.out.println("Added Nickname: "+nickname);
					}
					else{
						diffusionMessage(line, out);
					}
				}				
			}
			else{
				deconnection();
			}
		}
	}
	catch (IOException e) {
		System.err.println("Fin du service " + this.numero + " : " + e);			
		deconnection();
	}
		deconnection();
	}
	
	/**
	 * @brief broadcast du message du client
	 * @param line
	 * @param out
	 */
	public void diffusionMessage(String line, PrintWriter out){		
		System.out.println("Service " + numero + " a recu : " + line);
		boolean socketFermee = false;	//variable permettant de signaler si on peut ecrire sur une socket
		String buddyList = this.getBuddyList();
		for (int i = 0; i < autresClients.size(); i++){
			socketFermee = false;
			try{
				out = new PrintWriter (autresClients.get(i).getOutputStream (), true);
			}catch(IOException e){
				/**
				 * Si une exception est lev?e sur cette socket, cela veut dire que le client
				 * précedement connecté à cette socket s'est deconnecté
				 */
				socketFermee = true;
				//On retire cette socket
				autresClients.removeElementAt(i);
				--i;	//on decremente i car tout les elements du vector reculent de 1
			}
			if (!socketFermee){				
				String msg = "";
				if(this.nickname == null){
					msg += "Guest" + autresClients.get(i).getInetAddress() 
					+ "-" + autresClients.get(i).getPort() + " said : " + line;
				}
				else{
					msg = this.nickname + " said : " + line;					
				}
				out.println(buddyList+"\n"+msg);
				out.flush();
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
			out.flush();
			deconnection();
		} catch (IOException e) {
			MyLogger.errorMessage(e.getMessage());
		}
	}
	
	public void deconnection(){		
		try{
			connected = false;
			PrintWriter out = new PrintWriter (socket.getOutputStream (), true);
			out.println("*** DECONNECTION ***");
			out.flush();
			socket.close();
		}catch(IOException e){
			MyLogger.errorMessage(e.getMessage());
		}
	}
	
	protected void finalize() throws Throwable {
		socket.close(); 
	}
	
	private String getBuddyList() {
		String msg = new String("/buddyList "+this.socket.getInetAddress()+ "-"+this.socket.getPort()+",");
		
		for (int i = 0; i < autresClients.size(); i++) {
			msg = msg+"Guest" + autresClients.get(i).getInetAddress() 
					+ "-" + autresClients.get(i).getPort()+",";
		}
		
		return msg;
	}

	public String getNickname() {
		return nickname;
	}

	public void setNickname(String nickname) {
		this.nickname = nickname;
	}

}
