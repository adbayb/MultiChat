package com.adibsarr.multichat.model.server;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;

import com.adibsarr.multichat.controller.MyLogger;

public class ServerNIO extends AbstractMultichatServer implements MultichatServer, Runnable {
	private ServerSocketChannel serveurSocketChannel;
	private Selector selector;
	private ServiceNIO serviceNIO;
	
	/**
	 * @brief Cree un serveur TCP - objet de la classe ServerSocket
	 * 	      Puis lance le thread du serveur.
	 * @param address
	 * @param port
	 * @throws IOException
	 */
	public ServerNIO(InetAddress address, Integer port) throws IOException {
		super(address,port);
		
		//Initialisation de notre selector et de notre channel non bloquant associé à notre serveur:
		//Ouverture d'un socket serveur et attachement de ce dernier à notre channel serveurSocketChannel:
		this.serveurSocketChannel = ServerSocketChannel.open();
		this.serveurSocketChannel.configureBlocking(false);
		this.serveurSocketChannel.bind(new InetSocketAddress(port));
		
		this.selector = Selector.open();
		this.serveurSocketChannel.register(this.selector, SelectionKey.OP_ACCEPT);
		
		this.serviceNIO = new ServiceNIO();
	}

	public void run() {
		// TODO Auto-generated method stub
		System.out.println("Lancement du serveur NIO");
		try {
			//tant que le channel associé à notre serveur est actif, on peut communiquer:
			while(this.serveurSocketChannel.isOpen()) {
				//Mode sélection dans notre liste de clés:
				this.selector.select();
				Iterator<SelectionKey> it = this.selector.selectedKeys().iterator();
				while(it.hasNext()) {
					SelectionKey sKey = (SelectionKey)it.next();
					//on supprime l'entrée car un channel est assoié à une opération et une fois traité, elle est supprimée:
					it.remove();
					
					if(sKey.isValid()) {
						//Si la clé est associé à un état "en acceptation", le socket en cours est celui de notre serveur (socket d'écoute)
						//Appel à la fonction acceptation des connexions clients (qui créera les channels au sockets entrants et leur associera le statut "en lecture" (Readable)):
						if(sKey.isAcceptable()) {
							this.serviceNIO.serviceAccept(this.selector, sKey);
							System.out.println(this.serviceNIO.diffusionBuddyList(this.selector));
						}
						if(sKey.isReadable()) {
							this.serviceNIO.serviceLecture(this.selector, sKey);
						}
					}
				}
			}
			//Nettoyage channel serveur et socket serveur:
			stop();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	/**
	 * @brief Lance le thread du serveur
	 */
	public void start() {
		// TODO Auto-generated method stub
		(new Thread(this)).start();	
	}
	
	/**
	 * @brief Stoppe le serveur et tous les services
	 */
	public void stop() {
		// TODO Auto-generated method stub
		try { 
			this.selector.close();
			this.serveurSocketChannel.close();
		} catch (IOException e) {
			MyLogger.errorMessage(e.getMessage());
		}
	}
}
