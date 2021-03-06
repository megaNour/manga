package com.nour.after.work.console.server;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.StyleRange;
import org.eclipse.swt.custom.StyledText;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;

public class ConsoleDisplay {

	private static final Display  display;
	private static StyledText consoleText;
	static final Color GREEN;
	static final Color RED;
	static final Color BLACK;

	static {
		display = new Display();
		GREEN = display.getSystemColor(SWT.COLOR_GREEN);
		RED = display.getSystemColor(SWT.COLOR_RED);
		BLACK = display.getSystemColor(SWT.COLOR_BLACK);
	}

	public static void println(String message) {
		display.syncExec(new Runnable() {
			@Override
			public void run() {
				consoleText.append(message + "\r\n");
			}
		});
	}

	public static void println(String message, Color color) {
		if(!display.isDisposed()){
			display.syncExec(new Runnable() {
				@Override
				public void run() {
//					synchronized (consoleText) {
						consoleText.append(message + "\r\n");
						int textLength = consoleText.getText().length() - 2;
						StyleRange range = new StyleRange(textLength - message.length(), message.length(), color, BLACK);
						consoleText.setStyleRange(range);
//					}
				}
			});
		}
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
					if(consoleText.getText().length() == consoleText.getTextLimit()) {
						consoleText.setText("");
					}
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
