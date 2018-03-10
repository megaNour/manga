package telnet.server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class OldTelnetWay {
	public static void main(String[] args) throws UnknownHostException, IOException {
		if(args.length != 2) {
			System.out.println("Usage: java TelnetServer <host name> <port number>");
			System.out.println("will use default host 127.0.0.1 port 23");
			//System.exit(1);
			args = new String[2];
			args[0] = "127.0.0.1";
			args[1] = "23";
		}
		
		String hostName = args[0];
		int portNumber = Integer.parseInt(args[1]);
		
		try(
				Socket telnetSocket = new Socket(hostName, portNumber);
				PrintWriter out = new PrintWriter(telnetSocket.getOutputStream());
				BufferedReader in = new BufferedReader(new InputStreamReader(telnetSocket.getInputStream()));
				BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
				) {
			String userInput;
			while((userInput = stdIn.readLine()) != null) {
				out.println(userInput);
				System.out.println("echo: " + in.readLine());
			}
		} 
	}
}
