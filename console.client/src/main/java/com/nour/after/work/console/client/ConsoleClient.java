package com.nour.after.work.console.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

public class ConsoleClient {

	public static void main(String[] args) {
		try(
		Socket socket = new Socket("127.0.0.1", 2048);
		PrintWriter out = new PrintWriter(socket.getOutputStream());
		BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			
		){
			String input;
			while((input = in.readLine()) != null) {
				System.out.println(input);
				if(input.equals("echoed: bye")) {
					System.exit(0);
				} else if (input.equals("hey ?")) {
					out.write("hey !\r\n");
					out.flush();
				} else {
					out.write(stdIn.readLine() + "\r\n");
					out.flush();
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}
