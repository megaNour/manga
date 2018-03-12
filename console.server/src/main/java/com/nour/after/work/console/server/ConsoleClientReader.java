package com.nour.after.work.console.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.widgets.Display;

public class ConsoleClientReader extends Thread {
		private static List<ConsoleClientReader> clients;
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		private Color color;
		
		static {
			clients = new ArrayList<>();
		}
		
		public ConsoleClientReader(Socket socket) {
			this.socket = socket;
			if(socket.getPort()==2048) {
				color = ConsoleDisplay.green;
			} else {
				color = ConsoleDisplay.red;
			}
		}

		@Override
		public void run() {
			super.run();
			try {
				out = new PrintWriter(socket.getOutputStream());
				in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
				out.println("what is it ?");
				out.flush();
				String input;
				while((input = in.readLine()) != null) {
					if(input.equals("die")) {
						for(ConsoleClientReader r : clients) {
							r.closeNicely("I will be back...\r\nConsole Terminated");
						}
						System.exit(0);
					} else if (input.equals("bye")) {
						closeNicely("Ok! Bye...\r\nClosing nicely...");
						break;
					}
					ConsoleDisplay.println(input, color);
					System.out.println(input);
				}
			} catch (IOException e) {
				System.out.println("end of stream");
			}
		}

		protected void closeNicely(String lastWord) throws IOException {
			out.println(lastWord);
			out.close();
			in.close();
			socket.close();
			clients.remove(this);
		}
}
