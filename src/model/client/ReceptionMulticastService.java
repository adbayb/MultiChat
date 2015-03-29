package model.client;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.MulticastSocket;

import javafx.scene.control.TextArea;

//Runnable car la lecture est bloquante donc on le lance dans un thread à part pour ne pas bloquer notre GUI et autres:
public class ReceptionMulticastService implements  Runnable {
	private MulticastSocket multicastSocket;
	private TextArea windowChat;
	private Thread lectureThread;
	
	/**
	 * 
	 * @brief Cree un flux permetttant de lire les messages envoyés par les autres clients
	 * @param MulticastSocket Socket ou va se connecter le flux
	 * @param windowChat Fenêtre JavaFX qui va permettre d'afficher le chat 
	 * @throws IOException
	 */
	public ReceptionMulticastService(MulticastSocket multicastSocket, TextArea windowChat){
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		this.multicastSocket = multicastSocket;
		this.windowChat = windowChat;
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
			//s.leaveGroup(InetAddress.getByName(group));
			//s.close();
		}
		
		return;
	}

	
	/**
	 * @brief le thread va écouter les messages envoyés du serveur
	 * 		  Lors de la reception d'un message, celui-ci est affiché sur 
	 * 		  la fenêtre JavaFX
	 */
	public void run() {
		byte[] dataIn = new byte[1024];
		DatagramPacket paquetIn = new DatagramPacket(dataIn, 0, dataIn.length);
		
	    while (!this.lectureThread.isInterrupted()) {
	    	if(dataIn != null) {
			    paquetIn.setData(dataIn);
			    if(paquetIn != null && paquetIn.getLength() > 0) {
					try {
						if(this.multicastSocket.isClosed()==false)
							this.multicastSocket.receive(paquetIn);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				    /*System.out.println("Received data from: " + paquetIn.getAddress().toString() +
						    ":" + paquetIn.getPort() + " size: " +
						    paquetIn.getLength()+"\n");*/
					this.windowChat.appendText(new String(dataIn,0,paquetIn.getLength())+"\n");
			    }
	    	}
	    }
	}

	public TextArea getWindowChat() {
		return windowChat;
	}

	public void setWindowChat(TextArea windowChat) {
		this.windowChat = windowChat;
	}
}

