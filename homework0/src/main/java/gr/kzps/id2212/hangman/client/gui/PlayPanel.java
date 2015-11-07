package gr.kzps.id2212.hangman.client.gui;

import gr.kzps.id2212.hangman.client.Connection;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

public class PlayPanel extends JPanel {
	private Connection connection;
	private JTextField jtxt_notifications;
	private byte[] hint;
	
	public PlayPanel (Connection connection, JTextField jtxt_notifications, byte[] initialHint) {
		super();
		this.connection = connection;
		this.jtxt_notifications = jtxt_notifications;
		this.hint = initialHint;
		
		create();
	}
	
	private void create() {
		JLabel lala = new JLabel("lala");
		add(lala);
		JTextField koko = new JTextField();
		koko.setText(new String(hint));
		add(koko);
	}
}
