package de.ees.group1.cs;

import java.awt.EventQueue;

import de.ees.group1.bt.BT_manager;
import de.ees.group1.cs.controller.ControlStation;
import de.ees.group1.cs.gui.MainWindow;

public class Main {

	public static void main(String[] args) {
		
		MainWindow window = new MainWindow();
		ControlStation cs = new ControlStation(window);
		BT_manager man = cs.getManager();
		
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					window.setVisible(true);
					
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
		
		while(true){

			try {
				man.getMessage();
				Thread.sleep(20);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
		}
	}

}
