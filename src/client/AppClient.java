package client;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;

public class AppClient {

	private final static int PORT = 1000;
	private final static String HOST = "localhost"; 
	
	public static void main(String[] args){
		try {
			InetAddress serAddress = InetAddress.getByName("127.0.0.1");
			Socket s = new Socket(serAddress, PORT);
			new Client(s).lancer();
		} catch (IOException e) {
				System.err.println("Pb lors de la connection au serveur : " +  e);			
		}

	}
}
