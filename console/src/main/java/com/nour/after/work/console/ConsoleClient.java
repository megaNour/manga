package com.nour.after.work.console;

import java.io.PrintWriter;
import java.net.Socket;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

public class ConsoleClient implements BundleActivator/*, ServiceListener*/ {

	private int failCount;
	private boolean die;
	private String DIE_WORD = "die";


	@Override
	public void start(BundleContext context) throws Exception {
		Runtime.getRuntime().exec("consoleServer.sh");
		connect();
	}

	private void connect() throws Exception {
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
					try {
						int counter = 0;
						while(!die) {
							Thread.sleep(1000);
							out.println("plop " + counter++);
							out.flush();
						}
						out.println(DIE_WORD);
						out.flush();
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

	@Override
	public void stop(BundleContext context) throws Exception {
		die = true;
		System.out.println("mais il faut rester leger");
	}
}
