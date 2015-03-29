package com.adibsarr.multichat.view.client;


import com.adibsarr.multichat.controller.client.AppClient;

import javafx.event.ActionEvent;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

/**
 * 
 * @author SARR Niébé / ADIB Ayoub
 *
 */
public class Gui {

	static private final int WIDTH = 640;

	static private final int HEIGHT = 480;
	
	private AppClient client;
	private TextField saisie;
	private TextArea windowChat;
	private ListView<String> viewUserOnline;
	
	/**
	 * @brief On rattache notre vue à notre controlleur client
	 * @param client
	 * @param windowChat
	 * @param listUserOnline
	 */
	public Gui(AppClient client, TextArea windowChat, ListView<String> listUserOnline) {
		this.client = client;
		this.saisie = new TextField();
		this.windowChat = windowChat;
		this.viewUserOnline = listUserOnline;
	}
	
	/**
	 * @brief Lancement de la fenêtre JavaFX
	 * @param fenetre
	 * @throws Exception
	 */
	public void launch(Stage fenetre) throws Exception {
		StackPane root = new StackPane();
		
		final Label label = new Label("Client JavaFX - SARR Niébé / ADIB Ayoub");
		label.setFont(new Font("Arial", 20));
		
		//HBox équivalent à BoxLayout avec X_AXIS dans Swing:
		HBox hbox = new HBox();
		HBox.setHgrow(this.saisie, Priority.ALWAYS);
		this.saisie.setMaxWidth(Double.MAX_VALUE);
		this.saisie.setPromptText("Input Text");
		Button button = new Button("Send");
		button.addEventHandler(ActionEvent.ACTION, this.client.getAddInputClient(this.windowChat, this.saisie));
		this.saisie.setOnAction(this.client.getAddInputClient(this.windowChat, this.saisie));
		hbox.getChildren().addAll(this.saisie,button);
		
		this.viewUserOnline.setPrefWidth(Double.MAX_VALUE);
		this.viewUserOnline.setPrefHeight(140);
		
		VBox vbox = new VBox();
		vbox.setSpacing(10);
		vbox.setPadding(new Insets(5, 0, 0, 1));
		//La fenêtre de chat prendra toute la largeur de fenetre:
		this.windowChat.setMaxWidth(Double.MAX_VALUE);
		//On alloue tout l'espace encore disponible à la fenêtre de chat:
		VBox.setVgrow(this.windowChat, Priority.ALWAYS);
		vbox.getChildren().addAll(label, this.windowChat, this.viewUserOnline, hbox);
		
		//Nous ajoutons tous nos éléments dans l'ordre dans notre super containeur root:
		root.getChildren().addAll(vbox);
		//on crée notre scène:
		fenetre.setScene(new Scene(root,WIDTH,HEIGHT));
		//on ajoute un titre à notre fenêtre
		fenetre.setTitle("MultiChat SARR-ADIB");
		//on affiche notre fenêtre:
		//fenetre.hide();
		fenetre.show();
	}
}
