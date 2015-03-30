package com.adibsarr.multichat.model.server;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.Charset;
import java.nio.charset.CharsetDecoder;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceNIO {
	private Charset charset;
	private CharsetDecoder charsetDecoder;
	//Map Thread Safe:
	private ConcurrentHashMap<SocketChannel, String> clientsNick;
	
	public ServiceNIO() {
		this.charset = Charset.forName("UTF-8");
		//Instanciation d'un décodeur attaché à notre encodage de caractères (UTF-8, ASCII...):
		this.charsetDecoder = this.charset.newDecoder();
		this.clientsNick = new ConcurrentHashMap<SocketChannel, String>();
	}
	
	public void serviceAccept(Selector selecteur, SelectionKey key) throws IOException {
		//Les Channel sont reliés à une socket lors de leur création, ces objets supportent les connections non bloquantes.
		//On crée un channel d'acceptation à chaque connexion via key.channel (Returns the channel for which this key was created):
		ServerSocketChannel serveurChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serveurChannel.accept();
		if(socketChannel != null) {
			socketChannel.configureBlocking(false);
			String address = new String(socketChannel.socket().getInetAddress().toString()+":"+socketChannel.socket().getPort());
			socketChannel.register(selecteur, SelectionKey.OP_READ, address);
			socketChannel.write(this.charset.newEncoder().encode(CharBuffer.wrap("Welcome to ADIB and SARR Tchat [version 1.0]\n")));
			this.clientsNick.put(socketChannel, "Guest" + socketChannel.socket().getInetAddress() 
											+ "-" + socketChannel.socket().getPort());
			//buffer.rewind();
			System.out.println("Connected Client Address: "+address);
		}
	}
	
	public void serviceLecture(Selector selecteur, SelectionKey key) throws IOException {
		//récupération des channels clients acceptés:
		String msgRecuString = new String();
		ByteBuffer msgRecuBuffer = ByteBuffer.allocate(256);
		SocketChannel socketChannel = (SocketChannel)key.channel();
		if(socketChannel != null) {
			//Vérification si notre channel socket en lecture ne contient pas EOF, auquel 
			//cas cela signifie que le socket client associé au channel a finit sa connexion avec le serveur (exit client):
			//Nous écrivons dans notre ByteBuffer les données reçues par notre socket client:
			if(socketChannel.read(msgRecuBuffer) != -1) {
				msgRecuString = this.convertirByteBuffer(msgRecuBuffer);
				//Log Serveur: Affichage message client côté serveur:
				System.out.print(msgRecuString);
				if(msgRecuString.toLowerCase().contains("/nick ") == true) {
					//On attache le pseudo au channel:
					//on ne garde que le pseudo dans la map (+suppression de "\n"):
					//String nickname = msgRecuString.replaceAll("(.*)/nick (.*)(\n)","$2");
					String nickname = msgRecuString.substring(msgRecuString.indexOf("/nick ")+"/nick ".length(), msgRecuString.length()-1);
					this.clientsNick.put(socketChannel, nickname);
					System.out.println("Added Nickname: "+nickname);
				}
				else {
					//Diffusion message aux autres clients:
					diffusionMessage(selecteur, msgRecuString, socketChannel);
				}
			}
			else {
				//on ferme le channel associé au socket du client qui a quitté:
				key.channel().close();
				//on annule l'enregistrement associé à la clé du channel tout juste fermé:
				key.cancel();
			}
		}
	}
	
	//Fonctions utilitaires (privées):
	
	private void diffusionMessage(Selector selecteur, String msg, SocketChannel excludeChannel) throws IOException {
		ByteBuffer msgBuffer = null;
		for(SelectionKey sKey : selecteur.keys()) {
			if(sKey.isValid() && sKey.channel() instanceof SocketChannel) {
				SocketChannel socketChannel = (SocketChannel)sKey.channel();
				if(socketChannel != null) {
					if(socketChannel.isRegistered()) {
						//Inutile de diffuser le message vers le client qui l'a composé:
						if(socketChannel != excludeChannel) {
							//this.diffuserNick(msg, excludeChannel): String :
							//Récupération du nickname client responsable de l'envoi (excludeChannel):
							String message = this.getBuddyList()+"\n"+this.diffuserNick(msg, excludeChannel);
							msgBuffer = ByteBuffer.wrap(message.getBytes());
							//Broadcast du message à tous les channels rattachés aux divers sockets clients connectés:
							socketChannel.write(msgBuffer);
							//Nettoyage du buffer:
							msgBuffer.clear();
						}
					}
				}
			}
		}
	}
	
	public String diffusionBuddyList(Selector selecteur) throws IOException {
		ByteBuffer buddyBuffer = null;
		for(SelectionKey sKey : selecteur.keys()) {
			if(sKey.isValid() && sKey.channel() instanceof SocketChannel) {
				SocketChannel socketChannel = (SocketChannel)sKey.channel();
				if(socketChannel != null) {
					buddyBuffer = ByteBuffer.wrap(this.getBuddyList().getBytes());
					socketChannel.write(buddyBuffer);
					buddyBuffer.clear();
				}
			}
		}
		
		return this.getBuddyList();
	}
	
	private String convertirByteBuffer(ByteBuffer bbf) throws CharacterCodingException {
		String str = new String();
		CharBuffer cbf = null;
		
		//flip permet de rendre notre buffer prêt pour la lecture après écriture dans ce dernier
		//(position est remis à 0 et limit à la position du dernier byte écrit):
		bbf.flip();
		//System.out.println(msgRecuBuffer.position());
		//System.out.println(msgRecuBuffer.limit());
		
		//String msgRecuString = new String(msgRecuBuffer.array(), this.charset);
		//Conversion de notre ByteBuffer en Buffer de caractères:
		cbf = this.charsetDecoder.decode(bbf);
		str = cbf.toString();
		
		//Nous nettoyons notre buffer pour permettre la réécriture pour le prochain socket:
		bbf.clear(); 
		
		return str;
	}
	
	private String diffuserNick(String msgBody, SocketChannel socketChannel) {
		String msg = null;
		
		//Récupération du nickname associé au socket du channel socketChannel depuis notre Map (get(key)):
		String nickname = this.clientsNick.get(socketChannel);
		//Si on a un nickname de défini, on le broadcast dans le message aux autres clients connectés:
		if(nickname != null) {
			msg = nickname + " said : " + msgBody;
			
		}
		else {//si pas de pseudonyme défini par l'utilisateur:
			//Création d'un pseudonyme avec pour id le numéro de port du socket côté client et son addresse:
			msg = "Guest" + socketChannel.socket().getInetAddress() 
					+ "-" + socketChannel.socket().getPort() + " said : " + msgBody;
		}
		
		return msg;
	}
	
	private String getBuddyList() {
		String msg = new String("/buddyList ");
		
		//Print all values stored in ConcurrentHashMap instance
		for(Entry<SocketChannel, String> e : this.clientsNick.entrySet())
		{
			//System.out.print(e.getValue()+",");
			msg = msg+e.getValue()+",";
		}
		
		return msg;
	}
}
