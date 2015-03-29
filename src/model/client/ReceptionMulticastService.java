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
	
	public ReceptionMulticastService(MulticastSocket multicastSocket, TextArea windowChat) throws IOException {
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		this.multicastSocket = multicastSocket;
		this.windowChat = windowChat;
		this.lectureThread = new Thread(this);
	}
	
	public void lancer() {
		if(this.lectureThread != null)
			this.lectureThread.start();
		
		return;
	}
	
	public void stop() throws IOException {
		if(this.lectureThread != null) {
			this.lectureThread.interrupt();
			//s.leaveGroup(InetAddress.getByName(group));
			//s.close();
		}
		
		return;
	}
	
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

