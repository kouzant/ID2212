package gr.kzps.id2212.hangman.client.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class HeaderPanel extends JPanel {
	
	private static final long serialVersionUID = 967545756701867250L;
	
	private JTextField header;
	
	public HeaderPanel() {
		super();
		create();
	}
	
	private void create() {
		header = new JTextField("Welcome to Hangman", 30);
		header.setEditable(false);
		header.setHorizontalAlignment(JTextField.CENTER);
		header.setMargin(new Insets(50, 100, 50, 100));
		header.setMaximumSize(new Dimension(100, 10));
		
		add(header);
	}
}
