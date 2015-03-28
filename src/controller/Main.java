package controller;

import javafx.application.Application;
import javafx.stage.Stage;

//Application pour JAVAFX: GUI seulement pour client (cf package view):
public class Main extends Application {
	private final static int PORT = 10000;
	private final static String HOST = "localhost"; 
	
	public static void main(String[] args) {
		//TODO Niébé:
        //Gérer les cas GETOPT ici:
		
		//option -c pour lancer client:
		//new AppClient(HOST,PORT).execute();
		
		//option -s pour lancer serveur (par défaut sans NIO):
        //new AppServer(PORT).execute();
		//-s -n pour lancer un serveur NIO:
        new AppServer(PORT).executeNIO();
        
        //Option -p pour spécifier port...
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
}
