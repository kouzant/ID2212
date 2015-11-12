package gr.kzps.id2212.hangman.client;

import java.io.BufferedOutputStream;
import java.util.List;
import javax.swing.JTextField;
import javax.swing.SwingWorker;

/*
 * SwingWorker for sending packets to the server and publish messages to the
 * notification bar
 */
public class SendWorker extends SwingWorker<Void, String>{

	private final Connection connection;
	private JTextField jtxt_notifications;
	private BufferedOutputStream output;
	private final byte[] message;
	
	public SendWorker(Connection connection, byte[] message, JTextField jtxt_notifications) {
		this.connection = connection;
		this.jtxt_notifications = jtxt_notifications;
		this.message = message;
	}
	
	@Override
	protected Void doInBackground() throws Exception {
		setProgress(0);
		
		output = connection.getOutput();
		publish("Sending data...");
		
		output.write(message);
		output.flush();
		
		publish("Sent");
		setProgress(30);
		
		return null;
	}

	// Write publish messages to the notification bar
	@Override
	protected void process(List<String> chunks) {
		for (String notif: chunks) {
			jtxt_notifications.setText(notif);
		}
	}
}
