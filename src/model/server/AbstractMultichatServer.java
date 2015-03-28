package model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Vector;

public abstract class AbstractMultichatServer {
	private Vector<Service> servicesCli;
	
	AbstractMultichatServer(InetAddress address, Integer port) throws IOException {
		this.servicesCli = new Vector<Service>();
	}
	
	public Vector<Socket> getAutresClients() {
		Vector<Socket> autresClients = new Vector<Socket>();
		//todo: A modifier avec itérateur for( : ) :
		for(int i = 0; i < servicesCli.size(); i++)
			autresClients.add(servicesCli.get(i).getClient());
		return autresClients;
	}
	
	public Vector<Service> getService(){
		return servicesCli;
	}
}
