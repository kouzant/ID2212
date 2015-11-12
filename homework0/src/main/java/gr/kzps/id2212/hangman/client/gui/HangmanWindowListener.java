package gr.kzps.id2212.hangman.client.gui;

import gr.kzps.id2212.hangman.client.Connection;
import gr.kzps.id2212.hangman.client.SendWorker;
import gr.kzps.id2212.hangman.general.OpCodes;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class HangmanWindowListener implements WindowListener {
	private final Connection connection;
	private JTextField jtxt_notifications;

	public HangmanWindowListener(Connection connection, JTextField jtxt_notifications) {
		this.connection = connection;
		this.jtxt_notifications = jtxt_notifications;
	}

	@Override
	public void windowActivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosed(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowClosing(WindowEvent e) {
		// Be good citizen and inform the server about our exit
		// The GUI won't wait though if there is any latency
		
		byte[] message = new byte[] { OpCodes.CLOSE };
		SendWorker sendWorker = new SendWorker(connection, message, jtxt_notifications);
		
		sendWorker.addPropertyChangeListener(new PropertyChangeListener() {
			
			@Override
			public void propertyChange(PropertyChangeEvent evt) {
				if (evt.getPropertyName().equals("state")) {
					if ((SwingWorker.StateValue) evt.getNewValue() == SwingWorker.StateValue.DONE) {
						connection.close();
					}
				}
			}
		});
		sendWorker.execute();
	}

	@Override
	public void windowDeactivated(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowDeiconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowIconified(WindowEvent e) {
		// TODO Auto-generated method stub

	}

	@Override
	public void windowOpened(WindowEvent e) {
		// TODO Auto-generated method stub

	}

}
