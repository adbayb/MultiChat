package serveur;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.nio.charset.Charset;

public class ServiceNIO {
	private Charset charset;
	
	ServiceNIO() {
		this.charset = Charset.forName("UTF-8");
	}
	
	public void serviceAccept(Selector selecteur, SelectionKey key) throws IOException {
		//Les Channel sont reliés à une socket lors de leur création, ces objets supportent les connections non bloquantes.
		//On crée
		//On crée un channel d'acceptation à chaque connexion via key.channel (Returns the channel for which this key was created.):
		ServerSocketChannel serveurChannel = (ServerSocketChannel)key.channel();
		SocketChannel socketChannel = serveurChannel.accept();
		if(socketChannel != null) {
			socketChannel.configureBlocking(false);
			//TODO: remanier, à refaire:
			String address = (new StringBuilder( socketChannel.socket().getInetAddress().toString() )).append(":").append( socketChannel.socket().getPort() ).toString();
			socketChannel.register(selecteur, SelectionKey.OP_READ, address);
			socketChannel.write(this.charset.newEncoder().encode(CharBuffer.wrap("Hello\n")));
			//buffer.rewind();
			System.out.println("Connexion Acceptée: "+address);
		}
	}
	
	public void serviceLecture(Selector selecteur, SelectionKey key) throws IOException {
		//récupération des channels clients acceptés:
		String msgRecuString = new String();
		ByteBuffer msgRecuBuffer = ByteBuffer.allocate(200);
		SocketChannel socketChannel = (SocketChannel)key.channel();
		if(socketChannel != null) {
			//Vérification si notre channel socket en lecture ne contient pas EOF, auquel 
			//cas cela signifie que le socket client associé au channel a finit sa connexion avec le serveur (exit client):
			if(socketChannel.read(msgRecuBuffer) != -1) {
				//System.out.println(msgRecuBuffer.toString());
				//on reset le curseur de lecture à 0 pour permettre le décodage des données reçues en UTF-8:
				msgRecuBuffer.rewind();
				msgRecuString = this.charset.newDecoder().decode(msgRecuBuffer).toString();
				//Affichage message client côté serveur:
				System.out.println(msgRecuString);
				//Diffusion message aux autres clients:
				diffusionMessage(selecteur, msgRecuString+"\n");
			}
			else {
				//on ferme le channel associé au socket du client qui a quitté:
				key.channel().close();
				//on annule l'enregistrement associé à la clé du channel tout juste fermé:
				key.cancel();
			}
		}
	}
	
	private void diffusionMessage(Selector selecteur, String msg) throws IOException {
		ByteBuffer msgBuffer = ByteBuffer.wrap(msg.getBytes());
		if(msgBuffer != null) {
			for(SelectionKey sKey : selecteur.keys()) {
				if(sKey.isValid() && sKey.channel() instanceof SocketChannel) {
					SocketChannel socketChannel = (SocketChannel)sKey.channel();
					if(socketChannel != null) {
						socketChannel.write(msgBuffer);
					}
					msgBuffer.rewind();
				}
			}
		}
	}
}
