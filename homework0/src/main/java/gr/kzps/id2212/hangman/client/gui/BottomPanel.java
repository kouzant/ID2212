package gr.kzps.id2212.hangman.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Footer of the GUI
 * Host the notification bar
 */
public class BottomPanel extends JPanel {
	private static final Logger LOG = LogManager.getLogger(BottomPanel.class);
	
	private static final long serialVersionUID = 6221051443026694390L;
	private JTextField infoField;
	public BottomPanel() {
		setLayout(new BorderLayout());
		
		create();
	}
	
	private void create() {
		LOG.debug("Creating footer panel");
		infoField = new JTextField();
		infoField.setEditable(false);
		infoField.setBackground(Color.GRAY);
		
		this.add(infoField);
	}
	
	public JTextField getInfoField() {
		return infoField;
	}
}
