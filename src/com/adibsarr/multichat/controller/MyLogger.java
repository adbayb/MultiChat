package com.adibsarr.multichat.controller;

import java.io.IOException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.xml.XMLLayout;

public class MyLogger {
	public static Logger logRoot = Logger.getRootLogger();
	private static boolean consoleMsg = false;	
	private static ConsoleAppender ca;
	FileAppender fa;
	
	/**
	 * @brief On initialise le logger selon les besoins de l'utilisateur (debug ou fichier)
	 * @param infile booleen indiquant si on veut logger dans un fichier ou pas (console)
	 */
	public static void init(boolean infile){
		if(!infile){
			/**
			 * Initialisation du log dans la console
			 */
			ca = new ConsoleAppender();
			ca.setLayout(new SimpleLayout());
			ca.activateOptions();
			logRoot.addAppender(ca);
			logRoot.setLevel(Level.ERROR);				
		}
		else{
			try {
				/**
				 * Initialisation du fichier de log (log.txt à la racine du workspace)
				 */
				FileAppender fa = new FileAppender(new XMLLayout(), "log.txt");
				fa.setName("FichierLog");
				logRoot.addAppender(fa);
			} catch (SecurityException e) {
				// TODO Auto-generated catch block
				MyLogger.errorMessage(e.getMessage());
			} catch (IOException e) {
				// TODO Auto-generated catch block
				MyLogger.errorMessage(e.getMessage());
			}				
		}

	}		
	
	/**
	 * @brief Log un message d'erreur
	 * @param message
	 */
	public static final void errorMessage(String message){
		logRoot = Logger.getLogger("MultiChat Logger");
		logRoot.error(message);
	}

	public static boolean isConsoleMsg() {
		return consoleMsg;
	}

	public static void setConsoleMsg(boolean consoleMsg) {
		MyLogger.consoleMsg = consoleMsg;
	}
}
