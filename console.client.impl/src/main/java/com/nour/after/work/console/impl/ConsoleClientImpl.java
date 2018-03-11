package com.nour.after.work.console.impl;

import java.io.PrintWriter;
import java.net.Socket;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

//@Component(name="consoleClient", immediate=true)
public class ConsoleClientImpl implements BundleActivator {

	private int failCount;
	private boolean die;
	private String DIE_WORD = "die";
	private String BYE_WORD = "bye";

//	@Activate
	public void start(BundleContext context) throws Exception {
		System.out.println("coucou");
		Runtime.getRuntime().exec("consoleServer.sh");
		connect();
	}
	
//	@Override
	public void connect() throws Exception {
		try {
			Socket socket = new Socket("localhost", 2048);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			//			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
			String input;
			System.out.println("connected after: " + failCount + " tries");
			failCount = 0;
			Thread outThread = new Thread() {
				@Override
				public void run() {
					int counter = 0;
					try {
						byeLoop:
						while(true) {
							while(!die) {
								String message = "wlop " + counter++;
//								message = "bye";
								Thread.sleep(1000);
								out.println(message);
								out.flush();
								if(message.toLowerCase().equals(BYE_WORD)) {
									break byeLoop;
								}
							}
							out.println(DIE_WORD);
							out.flush();
							System.out.println("Console Terminated");
							break;
						}
						out.close();
						socket.close();
						System.out.println("Console Socket Closed");
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			};
			outThread.start();
		} catch (Exception e) {
			Thread.sleep(100);
			if(failCount++<10) {
				System.out.println("failed to connect n°:" + failCount);
				connect();
			} 
		}

	}

//	@Deactivate
	public void stop(BundleContext context) throws Exception {
		die = true;
		System.out.println("mais il faut rester leger");
	}
}
