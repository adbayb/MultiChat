package serveur;

import java.io.IOException;
import java.net.InetAddress;

public class AppServeur {
	private final static int PORT = 10001;

	public static void main(String[] args) {
		try {
			InetAddress serAddress = InetAddress.getLocalHost();
			
			//Sans NIO
			//Server serveur = new Server(serAddress, PORT);
			//serveur.start();
			
			//Avec NIO:
			ServerNIO serveurNIO = new ServerNIO(serAddress, PORT);
			serveurNIO.start();
		} catch (IOException e) {
				System.err.println("Pb lors de la création du serveur : " +  e);			
		}
	}
}
