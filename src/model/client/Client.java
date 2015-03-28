package model.client;

import java.io.IOException;
import java.net.Socket;

public class Client implements Runnable{
	
	private Reception lecture;
	private Emission ecriture;
	private IHM IHM;
	
	public Client(Socket s) throws IOException{
		setIHM(new IHM());
		lecture = new Reception(s.getInputStream());	//socket permettant la lecture des messages venant du serveur d�di�
		// Cree les threads pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		ecriture = new Emission(s.getOutputStream());	//socket permettant l'envoi de messages au serveur d�di�		
	}
	
	@Override
	public void run() {	
		lecture.lancer();
		ecriture.lancer();
	}	
	
	public void lancer() {
		(new Thread(this)).start();			
	}

	public IHM getIHM() {
		return IHM;
	}

	public void setIHM(IHM iHM) {
		IHM = iHM;
	}
	
}
