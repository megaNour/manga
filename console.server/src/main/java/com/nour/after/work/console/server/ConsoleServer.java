package com.nour.after.work.console.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleServer {


	//	public static ServerSocket server;
	public static ConsoleDisplay consoleDisplay;

	public static void main(String[] args) {
		try (ServerSocket serverGreen = new ServerSocket(Integer.parseInt(args[0]));
				ServerSocket serverRed = new ServerSocket(Integer.parseInt(args[0])+1);
				){

			Thread displayThread = new Thread() {
				public void run() {
					ConsoleDisplay.launch();
				};
			};

			displayThread.start();

			Thread redThread = new Thread() {
				public void run() {
					try {
						while(true) {
							Socket socket;
							socket = serverRed.accept();
							ConsoleClientReader client = new ConsoleClientReader(socket);
							client.start();
						}
					} catch (IOException e) {
						e.printStackTrace();
					}
				}
			};
			redThread.start();

			while(true) {
				Socket socket;
				socket = serverGreen.accept();
				ConsoleClientReader client = new ConsoleClientReader(socket);
				client.start();
			}
		} catch (NumberFormatException e1) {
			e1.printStackTrace();
		} catch (IOException e1) {
			e1.printStackTrace();
		}
	}
}
