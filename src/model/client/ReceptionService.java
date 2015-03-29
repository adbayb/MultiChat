package model.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import javafx.concurrent.Task;
import javafx.scene.control.TextArea;

//Runnable car la lecture est bloquante donc on le lance dans un thread à part pour ne pas bloquer notre GUI et autres:
public class ReceptionService extends Task<Void> {
	private BufferedReader socketIn;
	private TextArea windowChat;
	private Thread lectureThread;
	
	public ReceptionService(InputStream socketInputStream, TextArea windowChat) throws IOException {
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		this.socketIn = new BufferedReader(new InputStreamReader(socketInputStream));
		this.windowChat = windowChat;
		this.lectureThread = new Thread(this);
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

	@Override
	protected Void call() throws Exception {
		// TODO Auto-generated method stub
		String line = null;
		//Tant que notre thread n'est pas interrompu (stop est dépréciée), on continue à lire:
		while(true) {
			try {
				line = this.socketIn.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				//e.printStackTrace();
			}
			//Verifie si la connection est fermee. Si oui on sort de la boucle:
			if (line == null) { 
				this.windowChat.appendText("Connection closed by Server. Bye\n");
				break;
			}
			this.windowChat.appendText(line+"\n");
		}
		return null;
	}

}
