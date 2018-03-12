package com.nour.after.work.console.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.swt.graphics.Color;

public class ConsoleClientReader extends Thread {
		private static final String BYE = "bye";
		private static final String DIE = "die";
		private static List<ConsoleClientReader> clients;
		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		private Color color;
		
		static {
			clients = new ArrayList<>();
		}
		
		public ConsoleClientReader(Socket socket, Color color) {
			this.socket = socket;
			this.color = color;
			clients.add(this);
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
					if(input.equals(DIE)) {
						for(ConsoleClientReader r : clients) {
							r.closeNicely("I will be back...\r\nConsole Terminated");
						}
						System.exit(0);
					} else if (input.equals(BYE)) {
						closeNicely("Ok! Bye...\r\nClosing nicely...");
						clients.remove(this);
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
		}
}
