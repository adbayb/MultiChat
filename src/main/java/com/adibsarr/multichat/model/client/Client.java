package com.adibsarr.multichat.model.client;

import java.io.IOException;
import java.net.Socket;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

import com.adibsarr.multichat.controller.MyLogger;

public class Client {
	//Un client est définit par les services de lecture et Ecriture 
	private ReceptionService lecture;
	private EmissionService ecriture;
	
	private Socket socket;
	
	/**
	 * @brief Initialisation du client qui va être connecté à une socket
	 * 		  et associé à une fenêtre JavaFX
	 * @param socket Socket connectée au client
	 * @param windowChat Fenêtre JavaFX représentant le chat
	 * @param listUserOnline Buddy List Vue
	 * @throws IOException
	 */
	public Client(Socket socket, TextArea windowChat, ListView<String> listUserOnline) throws IOException {
		//Un client est également définit par une fenêtre de données contenant l'ensemble des Output/Input: textarea
		this.socket = socket;
		//Lecture implémente Runnable car la lecture est bloquante:
		//socket permettant la lecture des messages venant du serveur dédié:
		lecture = new ReceptionService(this.socket.getInputStream(),windowChat, listUserOnline);
		//socket Client permettant l'envoi de messages au serveur dédié
		ecriture = new EmissionService(this.socket.getOutputStream());		
	}
	
	/**
	 * @brief Lancement de l'écoute du client 
	 * 		  (un thread va constamment écouter le messages envoyés par le serveur)
	 */
	public void launch() {
		//l'écriture sera reçu via un listener (cf AddInputClient.java):
		lecture.lancer();			
	}
	
	/**
	 * @brief Arrêt de l'écoute
	 * @throws IOException
	 */
	public void stop(){
		try {
			this.socket.shutdownOutput();
			this.socket.shutdownInput();
			this.socket.close();
			this.lecture.stop();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		System.exit(0);
	}

	public ReceptionService getLecture() {
		return lecture;
	}

	public void setLecture(ReceptionService lecture) {
		this.lecture = lecture;
	}

	public EmissionService getEcriture() {
		return ecriture;
	}

	public void setEcriture(EmissionService ecriture) {
		this.ecriture = ecriture;
	}

	public Socket getSocket() {
		return socket;
	}

	public void setSocket(Socket socket) {
		this.socket = socket;
	}
}
