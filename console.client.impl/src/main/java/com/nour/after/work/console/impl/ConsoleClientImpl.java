package com.nour.after.work.console.impl;

import java.io.PrintWriter;
import java.net.Socket;

import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.nour.after.work.console.api.ConsoleClientApi;
import com.nour.after.work.console.api.CoucouApi;

@Component(immediate=true)
public class ConsoleClientImpl implements ConsoleClientApi {

	private int failCount;
	private boolean die;

	@Activate
	public void start(BundleContext context) throws Exception {
		System.out.println(this.getClass().getName() + " activated");
		Runtime.getRuntime().exec("consoleServer.sh");
		connect();
	}
	
	@Reference
	public void coucou(CoucouApi bidon) {
		System.out.println("coucou Reference !");
	}

	public void uncoucou(CoucouApi bidon) {
		
	}
	
	@Override
	public void connect() throws Exception {
		try {
			Socket socket = new Socket("localhost", 2048);
			PrintWriter out = new PrintWriter(socket.getOutputStream());
			//			BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
			//			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//			String input;
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
								Thread.sleep(10);
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

	@Deactivate
	public void stop(BundleContext context) throws Exception {
		System.out.println(this.getClass().getName() + " Stopping");
	}
}
