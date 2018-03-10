package console.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class TelnetServer {

	public static void main(String[] args) {
		
		try(
		ServerSocket server = new ServerSocket(Integer.parseInt(args[0]));
		Socket socket = server.accept();
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		){
			out.println("what is it ?");
			out.flush();
			String input;
			while((input = in.readLine()) != null) {
				System.out.println("input: " + input);
				out.println("echoed: " + input);
				out.flush();
				if(input.equals("bye")) {
					System.exit(0);
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
