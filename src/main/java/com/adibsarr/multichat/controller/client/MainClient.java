package com.adibsarr.multichat.controller.client;

import com.adibsarr.multichat.view.client.Gui;

import javafx.application.Application;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MainClient extends Application {
	private static int PORT = 10000;
	private static String HOST = "localhost"; 
	private static boolean ISMULTICAST = false;
	
	/**
	 * @brief Lancement dez l'application JavaFX
	 */
	public void start(Stage primaryStage) throws Exception {
		//Partie client:
		TextArea windowChat = new TextArea();
		ListView<String> listUserOnline =new ListView<String>();
		//Read Only pour notre textarea:
		windowChat.setEditable(false);
		AppClient appClient = new AppClient(HOST,PORT,windowChat,listUserOnline,ISMULTICAST);
		//Exécution du client:
		appClient.execute();
		if(windowChat != null) {
			Gui gui = new Gui(appClient,windowChat, listUserOnline);
			gui.launch(primaryStage);
		}
		//Nettoyage socket après fermeture du client:
		primaryStage.setOnCloseRequest(new CloseClientListener(appClient,ISMULTICAST));
	}

	public static int getPORT() {
		return PORT;
	}

	public static void setPORT(int pORT) {
		PORT = pORT;
	}

	public static String getHOST() {
		return HOST;
	}

	public static void setHOST(String hOST) {
		HOST = hOST;
	}

	public static boolean isISMULTICAST() {
		return ISMULTICAST;
	}

	public static void setISMULTICAST(boolean iSMULTICAST) {
		ISMULTICAST = iSMULTICAST;
	}

}
