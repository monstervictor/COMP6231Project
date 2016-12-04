package org.concordia.comp6231.client;

import java.awt.EventQueue;

import javax.swing.JFrame;

public class MainClient extends JFrame {
	private static final long serialVersionUID = 8926089729051860775L;
	
	public MainClient() {
        initUI();
    }

    private void initUI() {
        setTitle("Main Client");
        setSize(600, 400);
        // center the window on the screen.
        setLocationRelativeTo(null);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
    }

	public static void main(String[] args) {
		// this method places the application on the Swing Event Queue
		EventQueue.invokeLater(() -> {
			MainClient mainClient = new MainClient();
			mainClient.setVisible(true);
        });

	}

}
