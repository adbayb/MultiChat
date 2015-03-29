package controller.client;

import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class CloseClientListener implements EventHandler<WindowEvent> {
	private AppClient appClient;
	private boolean isMulticast;
	
	/**
	 * @brief Gestion de la fermeture d'un client
	 * @param appClient Programme ou le client tourne
	 * @param isMulticast booleen indiquant si c'est une connexion multicast
	 */
	public CloseClientListener(AppClient appClient, boolean isMulticast) {
		this.appClient = appClient;
		this.isMulticast = isMulticast;
	}
	
	/**
	 * @brief Gestion de l'événement
	 */
	public void handle(WindowEvent event) {
		// TODO Auto-generated method stub
		System.out.println("Client Closed");
		if(this.isMulticast == false) {
			//On envoie un message au serveur pour lui spécifier que le client s'est déconnecté:
			this.appClient.getClient()
					.getEcriture().getSocketOut()
					.println("Client disconnected ["+
							this.appClient.getClient().getSocket().getInetAddress()+" "+
							this.appClient.getClient().getSocket().getPort()+"]"
							);
			this.appClient.getClient().stop();
		}
		else {
			this.appClient.getClientMulticast().stop();
		}
	}

}
