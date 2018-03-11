package com.nour.after.work.console.server;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ConsoleDisplay {

	private static Display  display;
	private static StyledText consoleText;
	
	static {
		display = new Display();
	}

	//	public ConsoleDisplay() {
	//		createDisplay();
	//	}

	public static void println(String message) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				consoleText.append(message + "\r\n");
			}
		});
	}

	public static void launch() {
		System.out.println("gonna launch");
		if (consoleText == null) {
			System.out.println("launching display");
			Shell shell = new Shell(display);
			GridLayout layout = new GridLayout(1, true);
			layout.marginHeight=0;
			layout.marginWidth=0;
			shell.setBackground(display.getSystemColor(SWT.COLOR_DARK_GRAY));
			shell.setLayout(layout);
			shell.open();
			
			consoleText = new StyledText(shell, SWT.V_SCROLL | SWT.H_SCROLL);
			GridData data = new GridData(SWT.FILL, SWT.FILL, true, true);
			consoleText.setLayoutData(data);
			consoleText.setBackground(display.getSystemColor(SWT.COLOR_BLACK));
			consoleText.setForeground(display.getSystemColor(SWT.COLOR_GREEN));
			consoleText.addListener(SWT.Modify, new Listener(){
			    public void handleEvent(Event e){
			    	consoleText.setTopIndex(consoleText.getLineCount() - 1);
			    }
			});
			
			shell.layout();


			while (!shell.isDisposed()) {
				if (!display.readAndDispatch()) {
					display.sleep();
				}
			}
			display.dispose();
		}
	}
}
