package com.adibsarr.multichat.model.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;

//Runnable car la lecture est bloquante donc on le lance dans un thread à part pour ne pas bloquer notre GUI et autres:
public class ReceptionService implements  Runnable {
	private BufferedReader socketIn;
	private TextArea windowChat;
	private ListView<String> listUserOnline;
	private Thread lectureThread;
	private String receivedLine;
	
	/**
	 * @brief Cree un flux permetttant de lire les messages venant du serveur se trouvant sur la machine host au port PORT
	 * @param socketInputStream Socket ou va se connecter le flux
	 * @param windowChat Fenêtre JavaFX qui va permettre d'afficher le chat 
	 * @param listUserOnline Gestion liste Buddy
	 * @throws IOException
	 */
	public ReceptionService(InputStream socketInputStream, TextArea windowChat, ListView<String> listUserOnline) throws IOException {
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		this.socketIn = new BufferedReader(new InputStreamReader(socketInputStream));
		this.windowChat = windowChat;
		this.listUserOnline = listUserOnline;
		this.lectureThread = new Thread(this);
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
			//Platform.runLater bug fixé depuis Java1.8u40 donc inutile ici:
			if(receivedLine == null) { 
				windowChat.appendText("Connection closed by Server. Bye\n");
				try {
					this.stop();
					System.exit(0);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			else {
				/*if(receivedLine.contains("/buddyList ") == false) {
					windowChat.appendText(receivedLine+"\n");
				}
				else {//BuddyList
					ObservableList<String> bufferUserOnline = FXCollections.observableArrayList();
					listUserOnline.setItems(null);
					bufferUserOnline.clear();
					String[] splitUserOnline = (receivedLine.substring("/buddyList ".length(), receivedLine.length()-1)).split(",");
					for(String userOnline : splitUserOnline) {
						bufferUserOnline.add(userOnline);
					}
					listUserOnline.setItems(bufferUserOnline);
				}*/
				//Pour gérer notre BuddyList de façon optimale et permettre l'inclusion dans une listView via Platform.runLater(), il faut faire un thread à part entière côté serveur qui envoie des 
				//requêtes aux clients chaque seconde par exemple pour détecter les clients connecté. Puis faire envoie des infos clients co à tous les clients.
				windowChat.appendText(receivedLine+"\n");
			}
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

	public Thread getLectureThread() {
		return lectureThread;
	}

	public void setLectureThread(Thread lectureThread) {
		this.lectureThread = lectureThread;
	}

	public ListView<String> getListUserOnline() {
		return listUserOnline;
	}

	public void setListUserOnline(ListView<String> listUserOnline) {
		this.listUserOnline = listUserOnline;
	}

}
