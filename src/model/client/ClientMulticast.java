package model.client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.MulticastSocket;

import javafx.scene.control.TextArea;

public class ClientMulticast {
	//Un client est définit par les services de lecture et Ecriture 
	private ReceptionMulticastService lecture;
	private EmissionMulticastService ecriture;
	//Un client est également définit par une fenêtre de données contenant l'ensemble des Output/Input:
	
	private MulticastSocket multicastSocket;
	
	public ClientMulticast(String hostname, int port, TextArea windowChat) throws IOException {
		//Un client est également définit par une fenêtre de données contenant l'ensemble des Output/Input: (textarea).
		//Lecture implémente Runnable car la lecture est bloquante:
		//socket permettant la lecture des messages venant du serveur dédié:
		InetAddress address = InetAddress.getByName(hostname);
		this.multicastSocket = new MulticastSocket(port);
		this.multicastSocket.joinGroup(address);
		this.multicastSocket.setTimeToLive(32);
		lecture = new ReceptionMulticastService(this.multicastSocket, windowChat);
		//socket Client permettant l'envoi de messages au serveur dédié
		ecriture = new EmissionMulticastService(this.multicastSocket, address, port);		
	}
	
	public void launch() {
		//l'écriture sera reçu via un listener (cf AddInputClient.java):
		lecture.lancer();			
	}
	
	public void stop() throws IOException {
		this.lecture.stop();
		//Inutile car l'input associé au socket est déjà fermé par le thread: 
		//le socket est alors automatiquement fermé lorsqu'un de ces flux est fermé:
		//this.multicastSocket.disconnect();
		//this.multicastSocket.close();
		System.exit(0);
	}

	public ReceptionMulticastService getLecture() {
		return lecture;
	}

	public void setLecture(ReceptionMulticastService lecture) {
		this.lecture = lecture;
	}

	public EmissionMulticastService getEcriture() {
		return ecriture;
	}

	public void setEcriture(EmissionMulticastService ecriture) {
		this.ecriture = ecriture;
	}

	public MulticastSocket getMulticastSocket() {
		return multicastSocket;
	}

	public void setMulticastSocket(MulticastSocket multicastSocket) {
		this.multicastSocket = multicastSocket;
	}
}
