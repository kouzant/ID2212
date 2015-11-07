package gr.kzps.id2212.hangman.client.gui;

import gr.kzps.id2212.hangman.client.Connection;

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.EventQueue;

import javax.swing.JFrame;
import javax.swing.JPanel;

public class Hangman {

	private JFrame frame;
	private JPanel container;
	private BottomPanel bottomPanel;
	private HeaderPanel header;
	private ConnectionPanel mainTopPanel;
	private Connection connection;
	
	/**
	 * Launch the application.
	 */
	public static void main(String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				try {
					Hangman window = new Hangman();
					window.frame.setVisible(true);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		});
	}

	/**
	 * Create the application.
	 */
	public Hangman() {
		connection = new Connection();
		initialize();
	}

	/**
	 * Initialize the contents of the frame.
	 */
	private void initialize() {
		frame = new JFrame("Hangman");
		frame.setBounds(300, 100, 1000, 800);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		
		container = new JPanel(new BorderLayout());
		frame.add(container);
		
		header = new HeaderPanel();
		container.add(header, BorderLayout.NORTH);
		
		bottomPanel = new BottomPanel();
		container.add(bottomPanel, BorderLayout.SOUTH);
		
		mainTopPanel = new ConnectionPanel(connection, bottomPanel.getInfoField());
		container.add(mainTopPanel, BorderLayout.CENTER);
		
		bottomPanel.getInfoField().setText("Hello");
	}

}