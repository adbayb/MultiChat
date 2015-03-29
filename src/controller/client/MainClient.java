package controller.client;

import view.client.Gui;
import javafx.application.Application;
import javafx.scene.control.TextArea;
import javafx.stage.Stage;

public class MainClient extends Application {
	private static int PORT = 10000;
	private static String HOST = "localhost"; 
	private static boolean ISMULTICAST = false;
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		//Partie client:
		
		TextArea windowChat = new TextArea();
		//Read Only pour notre textarea:
		windowChat.setEditable(false);
		AppClient appClient = new AppClient(HOST,PORT,windowChat,ISMULTICAST);
		//Exécution du client:
		appClient.execute();
		if(windowChat != null) {
			Gui gui = new Gui(appClient,windowChat);
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
