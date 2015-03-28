package controller.server;

import java.net.InetAddress;
import java.net.UnknownHostException;

import controller.MyLogger;

public class MainServer {
	private static int PORT = 10000;
	private static String HOST = "localhost";
	
	public static void main() {
		try {
			new AppServer(InetAddress.getByName(HOST),PORT).execute();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			MyLogger.errorMessage(e.getMessage());
		}
	}
	
	public static void mainNIO() {
		try {
			new AppServer(InetAddress.getByName(HOST),PORT).executeNIO();
		} catch (UnknownHostException e) {
			// TODO Auto-generated catch block
			MyLogger.errorMessage(e.getMessage());
		}
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
}