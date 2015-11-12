package gr.kzps.id2212.hangman.client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;

import javax.swing.BoxLayout;
import javax.swing.JPanel;
import javax.swing.JProgressBar;
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
	private JProgressBar progressBar;
	
	public BottomPanel() {
		setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
		create();
	}
	
	private void create() {
		LOG.debug("Creating footer panel");
		infoField = new JTextField();
		infoField.setEditable(false);
		infoField.setBackground(Color.GRAY);
		infoField.setMaximumSize(new Dimension(10000, 20));
		
		progressBar = new JProgressBar(0, 100);
		progressBar.setForeground(Color.ORANGE);
		progressBar.setVisible(true);
		
		this.add(infoField);
		this.add(progressBar);
	}
	
	public JTextField getInfoField() {
		return infoField;
	}
	
	public JProgressBar getProgressBar() {
		return progressBar;
	}
}
