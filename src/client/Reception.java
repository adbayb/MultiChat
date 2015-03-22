package client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

public class Reception implements  Runnable{

	BufferedReader sin;
	private IHM IHM;;
	
	public Reception(InputStream inputStream) throws IOException{
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT		
		sin = new BufferedReader (new InputStreamReader(inputStream));
		IHM = new IHM();
	}
	public void lancer() {
		(new Thread(this)).start();			
	}
	
	public void run() {
		// Informe l'utilisateur de la connection
		//IHM.afficher("Connecté au serveur " + lecture.getInetAddress() + ":" + lecture.getPort(), true);
		
		String line = null;
		while(true){
			try {
				line = sin.readLine();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			// Verifie si la connection est fermee.
			// Si oui on sort de la boucle
			if (line == null) { 
				IHM.afficher("Connection ferme par le serveur.", true); 
				break;
			}
			// Ecrit la ligne envoyee par le serveur
			IHM.afficher(line, true);
		}
	}

}
