package serveur;

import java.io.IOException;
import java.net.InetAddress;

public class AppServeur {
	private final static int PORT = 1000;

	public static void main(String[] args) {
		try {
			InetAddress serAddress = InetAddress.getLocalHost();
			Server serveur = new Server(serAddress, PORT);
			serveur.lancer();
		} catch (IOException e) {
				System.err.println("Pb lors de la création du serveur : " +  e);			
		}
	}
}
