package com.adibsarr.multichat.controller.client;

import com.adibsarr.multichat.model.client.Client;
import com.adibsarr.multichat.model.client.ClientMulticast;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;

public class InputClientListener implements EventHandler<ActionEvent> {
	private TextField saisie;
	private TextArea windowChat;
	private Client client;
	private ClientMulticast clientMulticast;

	/**
	 * @brief Classe qui va permettre de detecter l'envoi d'un message par le client
	 * @param client Client connecté à la socket
	 * @param clientMulticast Client dans le cas d'une connection multicast
	 * @param windowChat Fenêtre Affichant l'ensemble des messages du chat
	 * @param saisie champ ou l'utilisateur a saisi son message
	 */
	public InputClientListener(Client client, ClientMulticast clientMulticast, TextArea windowChat, TextField saisie) {
		this.client = client;
		this.clientMulticast = clientMulticast;
		this.saisie = saisie;
		this.windowChat = windowChat;
	}
	
	/**
	 * @brief Gestion de l'évenement : Affichage du message et envoi à la socket (pour le broadcast)
	 */
	public void handle(ActionEvent arg0) {
		if(this.saisie.getText() != null) {
			if(this.client != null) {
				if(this.saisie.getText().contains("/nick ") == false)
					this.windowChat.appendText("Me : "+this.saisie.getText()+"\n");
				//On attache à notre socket client pour l'envoyer au serveur:
				this.client.getEcriture().updateSaisie(saisie);
			}
			else if(this.client == null) {
				this.clientMulticast.getEcriture().updateSaisie(saisie);
			}
			else {
				this.windowChat.appendText("Error: Unconnected to Server\n");
			}
			
			//Nettoyage de la saisie:
			this.saisie.clear();
		}
	}

}
