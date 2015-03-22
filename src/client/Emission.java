package client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

public class Emission implements Runnable{

	PrintWriter sout;
	private IHM IHM;
	
	public Emission(OutputStream outputStream) throws IOException{
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT		
		sout = new PrintWriter (outputStream, true);
		IHM = new IHM();
	}
	
	public void lancer() {
		(new Thread(this)).start();			
	}	

	@SuppressWarnings("static-access")
	public void run() {		
		String line;
		sout.println("*** Un client" /*+ user.getLogin()*/ + " vient de se connecter***");
		while(true){
			System.out.flush();
			line = IHM.saisie();
			// envoie au serveur
			sout.println("client>> " + line);
		}
	}
}
