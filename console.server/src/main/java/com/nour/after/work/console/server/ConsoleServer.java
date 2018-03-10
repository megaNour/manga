package console.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class ConsoleServer {

	private static List<ClientThread> clients;
	private static ServerSocket server;
	
	public static void main(String[] args) throws NumberFormatException, IOException {
		clients = new ArrayList<>();
		server = new ServerSocket(Integer.parseInt(args[0]));
		while(true) {
			Socket socket = server.accept();
			ClientThread client = new ClientThread(socket);
			clients.add(client);
			client.start();
		}

	}

	protected static class ClientThread extends Thread {

		private Socket socket;
		private PrintWriter out;
		private BufferedReader in;
		
		public ClientThread(Socket socket) {
			this.socket = socket;
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
						for(ClientThread t : clients) {
							t.closeNicely("I will be back...\r\nConsole Terminated");
						}
						server.close();
						System.exit(0);
					} else if (input.equals("bye")) {
						closeNicely("Ok! Bye...\r\nClosing nicely...");
						break;
					}
					System.out.println(input);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		protected void closeNicely(String lastWord) throws IOException {
			out.println(lastWord);
			out.close();
			in.close();
			socket.close();
		}
	}
}
