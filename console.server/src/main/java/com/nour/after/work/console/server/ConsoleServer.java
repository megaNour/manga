package com.nour.after.work.console.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleServer {


	//	public static ServerSocket server;
	public static ConsoleDisplay consoleDisplay;

	public static void main(String[] args) {
		try (ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));){

			Thread displayThread = new Thread() {
				public void run() {
					ConsoleDisplay.launch();
				};
			};

			displayThread.start();


			while(true) {
				Socket socket;
				socket = server.accept();
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
