package gr.kzps.id2212.hangman.client.gui;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

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
		
		this.add(infoField);
	}
	
	public JTextField getInfoField() {
		return infoField;
	}
}
