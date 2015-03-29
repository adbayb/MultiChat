package model.client;

import java.io.IOException;
import java.net.Socket;

import javafx.scene.control.TextArea;

public class Client implements Runnable {
	//Un client est définit par les services de lecture et Ecriture 
	private ReceptionService lecture;
	private EmissionService ecriture;
	//Un client est également définit par une fenêtre de données contenant l'ensemble des Output/Input:
	private TextArea windowChat;
	private Socket socket;
	
	public Client(Socket socket, TextArea windowChat) throws IOException {
		this.socket = socket;
		//Lecture implémente Runnable car la lecture est bloquante:
		//socket permettant la lecture des messages venant du serveur dédié:
		lecture = new ReceptionService(this.socket.getInputStream(),windowChat);
		//socket Client permettant l'envoi de messages au serveur dédié
		ecriture = new EmissionService(this.socket.getOutputStream());		
	}
	
	public void stop() throws IOException {
		//this.lecture.stop();
		this.socket.shutdownOutput();
		this.socket.shutdownInput();
		this.socket.close();
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

	@Override
	public void run() {
		// TODO Auto-generated method stub
		//l'écriture sera reçu via un listener (cf AddInputClient.java):
		new Thread(this.lecture).start();
	}
	
}
