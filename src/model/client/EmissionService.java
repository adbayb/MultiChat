package model.client;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;

import javafx.scene.control.TextField;

public class EmissionService {
	PrintWriter socketOut;
	private TextField saisie;
	
	/**
	 * @brief Créé un flux permettant d'écrire sur la socket connectée au serveur
	 * @param outSocketStream flux d'écrire connecté à la socket reliée au serveur
	 * @throws IOException
	 */
	public EmissionService(OutputStream outSocketStream){
		// Cree une socket pour communiquer avec le service se trouvant sur la
		// machine host au port PORT
		this.socketOut = new PrintWriter(outSocketStream, true);
		this.saisie = null;
	}
	
	/**
	 * @brief Envoi d'un message saisi à la socket
	 * @param saisie message sasi sur la fenêtre JavaFX
	 * @return true si la socket est encore connectée, false sinon
	 */
	public boolean updateSaisie(TextField saisie) {
		if(saisie != null) {
			this.saisie = saisie;
			//on met à jour notre flux de sortie socket:
			if(this.socketOut != null)
				this.socketOut.println(this.saisie.getText());
			else 
				return false;
			
			return true;
		}
		
		return false;
	}

	public PrintWriter getSocketOut() {
		return socketOut;
	}

	public void setSocketOut(PrintWriter socketOut) {
		this.socketOut = socketOut;
	}

	public TextField getSaisie() {
		return saisie;
	}

	public void setSaisie(TextField saisie) {
		this.saisie = saisie;
	}
}
