package gr.kzps.id2212.hangman.client.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

public class BottomPanel extends JPanel {

	private static final long serialVersionUID = 6221051443026694390L;
	private JTextField infoField;
	public BottomPanel() {
		setLayout(new BorderLayout());
		
		infoField = new JTextField();
		infoField.setEditable(false);
		
		this.add(infoField);
	}
	
	public JTextField getInfoField() {
		return infoField;
	}
}
