package controller.client;

import java.io.IOException;

import controller.MyLogger;
import javafx.event.EventHandler;
import javafx.stage.WindowEvent;

public class CloseClientListener implements EventHandler<WindowEvent> {
	private AppClient appClient;
	private boolean isMulticast;
	
	public CloseClientListener(AppClient appClient, boolean isMulticast) {
		this.appClient = appClient;
		this.isMulticast = isMulticast;
	}
	
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
			//On nettoie notre socket client et on le ferme:
			try {
				this.appClient.getClient().stop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				MyLogger.errorMessage(("Client disconnected"));
				//On quitte en cas d'exception (MyLogger ne quitte pas le programme directement après avoir loggué):
				System.exit(0);
			}
		}
		else {
			try {
				this.appClient.getClientMulticast().stop();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				MyLogger.errorMessage(("Client disconnected"));
				//On quitte en cas d'exception (MyLogger ne quitte pas le programme directement après avoir loggué):
				System.exit(0);
			}
		}
	}

}
