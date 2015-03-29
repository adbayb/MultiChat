package model.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.application.Platform;
import javafx.scene.control.TextArea;

//Runnable car la lecture est bloquante donc on le lance dans un thread à part pour ne pas bloquer notre GUI et autres:
public class ReceptionService implements  Runnable {
	private BufferedReader socketIn;
	private TextArea windowChat;
	private Thread lectureThread;
	private String receivedLine;
	
	/**
	 * @brief Cree un flux permetttant de lire les messages venant du serveur se trouvant sur la machine host au port PORT
	 * @param socketInputStream Socket ou va se connecter le flux
	 * @param windowChat Fenêtre JavaFX qui va permettre d'afficher le chat 
	 * @throws IOException
	 */
	public ReceptionService(InputStream socketInputStream, TextArea windowChat){
		this.socketIn = new BufferedReader(new InputStreamReader(socketInputStream));
		this.windowChat = windowChat;
		this.lectureThread = new Thread(this);
		receivedLine = null;
	}
	
	/**
	 * @brief Lancement du thread qui va "écouter" sur la socket
	 */
	public void lancer() {
		if(this.lectureThread != null)
			this.lectureThread.start();
		
		return;
	}
	
	/**
	 * @brief Arrêt du thread
	 * @throws IOException
	 */
	public void stop() throws IOException {
		if(this.lectureThread != null) {
			this.lectureThread.interrupt();
		}
		
		return;
	}
	
	/**
	 * @brief le thread va écouter les messages envoyés du serveur
	 * 		  Lors de la reception d'un message, celui-ci est affiché sur 
	 * 		  la fenêtre JavaFX
	 */
	public void run() {
		receivedLine = null;
		//Tant que notre thread n'est pas interrompu (stop est dépréciée), on continue à lire:
		while(!this.lectureThread.isInterrupted()) {
			try {
				receivedLine = this.socketIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			//Verifie si la connection est fermee. Si oui on sort de la boucle:
			//Ajout du service Platform.runLater car modification d'un élément GUI JavaFX or de son thread application:
			//Platform.runLater permet donc d'ajouter les modifications graphique depuis un autre thread dans le thread JAVAFX Application
			//de manière thread safe:
			Platform.runLater(new Runnable() {

				public void run() {
					// TODO Auto-generated method stub
					if(receivedLine == null) { 
						windowChat.appendText("Connection closed by Server. Bye\n");
						try {
							//On quitte le client:
							stop();
						} catch (IOException e) {
							// TODO Auto-generated catch block
							e.printStackTrace();
						}
						Platform.exit();
					}
					else {
						windowChat.appendText(receivedLine+"\n");
					}
				}
				
			});
		}
	}

	public BufferedReader getSocketIn() {
		return socketIn;
	}

	public void setSocketIn(BufferedReader socketIn) {
		this.socketIn = socketIn;
	}

	public TextArea getWindowChat() {
		return windowChat;
	}

	public void setWindowChat(TextArea windowChat) {
		this.windowChat = windowChat;
	}

}
