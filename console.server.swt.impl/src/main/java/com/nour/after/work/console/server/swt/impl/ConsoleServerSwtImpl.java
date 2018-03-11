package com.nour.after.work.console.server.swt.impl;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.Socket;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Shell;
import org.osgi.framework.BundleContext;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;

import com.nour.after.work.console.server.swt.api.ConsoleServerSwtApi;
import com.nour.after.work.console.server.swt.api.CoucouApi;


@Component(name="consoleSwtServer", immediate=true)
public class ConsoleServerSwtImpl implements ConsoleServerSwtApi {

	private int failCount;
	private StyledText consoleText;
	private String input;

	@Activate
	public void start(BundleContext context) throws Exception {
		System.out.println(this.getClass().getName() + " activated");
		Runtime.getRuntime().exec("consoleServer.sh");
		Thread swtThread = new Thread() {
			public void run() { createDisplay();}
		};
		swtThread.start();
	}

	@Reference
	public void coucou(CoucouApi bidon) {
		System.out.println("coucou Reference !");
	}

	public void uncoucou(CoucouApi bidon) {

	}

	private void createDisplay() {
		System.out.println("launching display");
		Display display = new Display();
		Shell shell = new Shell(display);
		GridLayout layout = new GridLayout(1, true);
		layout.marginHeight=0;
		layout.marginWidth=0;
		shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
		shell.setLayout(layout);
		shell.open();
		consoleText = new StyledText(shell, SWT.BORDER);
		GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
		consoleText.setLayoutData(data);
		consoleText.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
		consoleText.setForeground(display.getSystemColor(SWT.COLOR_RED));
		shell.layout();
		
		connect();
		
		while (!shell.isDisposed()) {
			if (!display.readAndDispatch()) {
				display.sleep();
			}
		}
		display.dispose();
	}

	@Override
	public void connect() {
		Thread outThread = new Thread() {
			@Override
			public void run() {
				try {
					Socket socket = new Socket("localhost", 2048);
					//			PrintWriter out = new PrintWriter(socket.getOutputStream());
					BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
					//			BufferedReader stdIn = new BufferedReader(new InputStreamReader(System.in));
					//			String input;
					System.out.println("connected after: " + failCount + " tries");
					failCount = 0;
					try {
						while((input = in.readLine()) != null) {
							Display.getDefault().syncExec(new Runnable() {
								@Override
								public void run() {
									consoleText.append(input);
									
								}
							});
						}
						socket.close();
						System.out.println("Console Socket Closed");
					} catch (Exception e) {
						e.printStackTrace();
					}
				} catch (Exception e) {
					try {
						Thread.sleep(100);
						if(failCount++<10) {
							System.out.println("failed to connect n°:" + failCount);
							connect();
						} 
					} catch (InterruptedException e1) {
						e1.printStackTrace();
					}
				}
			}
		};
		outThread.start();
	}

	@Deactivate
	public void stop(BundleContext context) throws Exception {
		System.out.println(this.getClass().getName() + " Stopping");
	}
}
