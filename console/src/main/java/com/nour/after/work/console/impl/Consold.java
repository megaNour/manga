//package com.nour.after.work.console;
//
//import java.io.BufferedReader;
//import java.io.IOException;
//import java.io.InputStreamReader;
//import java.io.PrintWriter;
//import java.net.ServerSocket;
//import java.net.Socket;
//
//import org.osgi.framework.BundleActivator;
//import org.osgi.framework.BundleContext;
//
//public class Consold implements BundleActivator/*, ServiceListener*/ {
//
//	private Process console;
//	@Override
//	public void start(BundleContext context) throws Exception {
//		Thread consoleThread = new Thread() {
//			@Override
//			public void run() {
//				try {
//					System.out.println("j'aime le chocolat1");
//					//		context.addServiceListener(this);
////					System.out.println("ready ? Press enter");
////					System.in.read();
//
//					//		Thread consoleThread = new Thread() {
//					//			@Override
//					//			public void run() {
//					//				try {
//					console = Runtime.getRuntime().exec("telnet.sh");
//					//				} catch (IOException e) {
//					//					e.printStackTrace();
//					//				}
//					//			}
//					//		};
//					//
//					//		consoleThread.start();
//					//		consoleThread.join();
//					Thread.sleep(1000);
//
//					System.out.println("console is " + console);
//
//					try(
//							Socket socket = new Socket("localhost", 2048);
//							PrintWriter out = new PrintWriter(socket.getOutputStream());
//							BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
//							BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
//							){
//						//			out.println("Hey !");
//						//			out.flush();
//						//			Thread stdInThread = new Thread() {
//						//				@Override
//						//				public void run() {
//						String input;
//						try {
////							while((input = stdIn.readLine()) != null) {
//							while(true) {
//								Thread.sleep(1000);
//								out.println("plop");
//								out.flush();
////								if(input.equals("bye")) {
////									System.out.println("you said bye");
////									break;
////								}
//							}
//						} catch (Exception e) {
//							e.printStackTrace();
//						}
//					}
//					//			};
//					//			stdInThread.start();
//					//		} catch (IOException e) {
//					//			e.printStackTrace();
//					//		}
//				} catch (Exception e) {
//					// TODO: handle exception
//				}
//			}};
//			consoleThread.start();
//	}
//
//	@Override
//	public void stop(BundleContext context) throws Exception {
//		System.out.println("mais il faut rester leger");
//		//		context.removeServiceListener(this);
//	}
//
//	//	@Override
//	//	public void serviceChanged(ServiceEvent event) {
//	//		System.out.println("service change ! " + event.getSource());
//	//		String[] objectClass = (String[])
//	//	            event.getServiceReference().getProperty("objectClass");
//	//
//	//	        if (event.getType() == ServiceEvent.REGISTERED)
//	//	        {
//	//	            System.out.println(
//	//	                "Ex1: Service of type " + objectClass[0] + " registered.");
//	//	        }
//	//	        else if (event.getType() == ServiceEvent.UNREGISTERING)
//	//	        {
//	//	            System.out.println(
//	//	                "Ex1: Service of type " + objectClass[0] + " unregistered.");
//	//	        }
//	//	        else if (event.getType() == ServiceEvent.MODIFIED)
//	//	        {
//	//	            System.out.println(
//	//	                "Ex1: Service of type " + objectClass[0] + " modified.");
//	//	        }
//	//	}
//
//
//}
