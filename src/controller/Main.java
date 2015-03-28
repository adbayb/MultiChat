package controller;

import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;

import org.apache.log4j.ConsoleAppender;
import org.apache.log4j.FileAppender;
import org.apache.log4j.Level;
import org.apache.log4j.Logger;
import org.apache.log4j.SimpleLayout;
import org.apache.log4j.xml.XMLLayout;

import gnu.getopt.Getopt;
import javafx.application.Application;
import javafx.stage.Stage;

//Application pour JAVAFX: GUI seulement pour client (cf package view):
public class Main extends Application {
	private static int PORT = 10000;
	private static String HOST = "localhost"; 
	
	private final static String msgHelp = "Usage : java -jar target / multichat -0.0.1 - SNAPSHOT .jar [ OPTION ]...\n" +
			"-a, -- address = ADDR set the IP address\n"+
			"-d, --debug display error messages\n"+
			"-h, --help display help and quit\n"+
			"-m, -- multicast start the client en multicast mode\n"+
			"-n, --nio configure the server in NIO mode\n"+
			"-p, --port = PORT set the port\n"+
			"-s, -- server start the server\n";	
	
	public static void main(String[] args) {
		//TODO Niébé:
        //Gérer les cas GETOPT ici:
		boolean nio = false, server=false, client = false;
		
		Getopt g = new Getopt("MultiChat", args, "a:dhmnp:sc");
		int c;

		while ((c = g.getopt()) != -1){
			switch(c){
			case 'a':
				//set the ip address
				HOST = g.getOptarg();
				break;
			case 'd':
				//Mode debug (log dans la console)
				MyLogger.setConsoleMsg(true);
				break;
			case 'h':
				System.out.println(msgHelp);
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				System.exit(0);
			case 'm':
				//TODO Ayoub : Lancer le client en multicast quand ca sera devoloppé
				break;
			case 'n':
				nio = true;
				break;
			case 'p':
				//Option -p pour spécifier port
				PORT = Integer.parseInt(g.getOptarg()); 
				break;
			case 's':
				server = true;
				break;
			case 'c':
				//option -c pour lancer client:
				client = true;				
				break;
			case '?':
				break;
			default:
				System.out.print("getopt() returned " + c + "\n");
			}
		}		
		
		if(MyLogger.isConsoleMsg()){
			MyLogger.init(false);
		}
		else{
			MyLogger.init(true);
		}		
		
		if(client){
			new AppClient(HOST,PORT).execute();
		}		
		else if(server){
			if(!nio){
				//option -s pour lancer serveur (par défaut sans NIO):
				try {
					new AppServer(PORT, InetAddress.getByName(HOST)).execute();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					MyLogger.errorMessage(e.getMessage());
				}
				
			}
			else{	
				//-s -n pour lancer un serveur NIO:
				try {
					new AppServer(PORT, InetAddress.getByName(HOST)).executeNIO();
				} catch (UnknownHostException e) {
					// TODO Auto-generated catch block
					MyLogger.errorMessage(e.getMessage());
				}		
			}			
		}
    }

	@Override
	public void start(Stage primaryStage) throws Exception {
		// TODO Auto-generated method stub
		
	}
	
	public static final class MyLogger {
		
		public static Logger logRoot = Logger.getRootLogger();
		private static boolean consoleMsg = false;	
		private static ConsoleAppender ca;
		FileAppender fa;
		
		public static void init(boolean infile){
			if(!infile){
				ca = new ConsoleAppender();
				ca.setLayout(new SimpleLayout());
				ca.activateOptions();
				logRoot.addAppender(ca);
				logRoot.setLevel(Level.ERROR);				
			}
			else{
				try {
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
}
