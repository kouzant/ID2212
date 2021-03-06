package gr.kzps.id2212.hangman.client.gui;

import java.awt.Dimension;
import java.awt.Insets;

import javax.swing.JPanel;
import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Header panel of the GUI
 */
public class HeaderPanel extends JPanel {
	private static final Logger LOG = LogManager.getLogger(HeaderPanel.class);
	
	private static final long serialVersionUID = 967545756701867250L;
	
	private JTextField header;
	
	public HeaderPanel() {
		super();
		create();
	}
	
	private void create() {
		LOG.debug("Creating header panel");
		header = new JTextField("Hangman", 30);
		header.setEditable(false);
		header.setHorizontalAlignment(JTextField.CENTER);
		header.setMargin(new Insets(20, 20, 20, 20));
		header.setMaximumSize(new Dimension(50, 10));
		
		add(header);
	}
}
