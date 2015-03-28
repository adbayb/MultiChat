package model.client;

import java.util.Scanner;

public class IHM {
	private Scanner clavier;
	
	public IHM(){
		clavier = new Scanner(System.in);
	}
	
	public String saisie(){
		return clavier.nextLine();
	}

	public String saisie(String message){
		afficher(message, true);
		return saisie();
	}
	
	public void afficher(String message, boolean sautDeLigne){
		if(sautDeLigne)
			System.out.println(message);
		else
			System.out.print(message);
	}
}
