package gr.kzps.id2212.hangman.client;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import javax.swing.JTextField;
import javax.swing.SwingWorker;

public class RecvWorker extends SwingWorker<byte[], String>{
	private final Connection connection;
	private JTextField jtxf_notifications;
	private BufferedInputStream input;
	
	public RecvWorker(Connection connection, JTextField jtxf_notifications) {
		this.connection = connection;
		this.jtxf_notifications = jtxf_notifications;
	}

	@Override
	protected byte[] doInBackground() throws Exception {
		input = connection.getInput();
		byte[] buffer = new byte[256];
        Integer length;
        Integer bytesRead = 0;
		
		publish("Receiving data...");
		
		try {
			while((length = input.read(buffer, bytesRead, 128)) != -1 ) {
				bytesRead += length;
				
				if (bytesRead >= 256) {
					break;
				}
				
				if (input.available() == 0) {
					break;
				}
			}
			
			byte[] response = Arrays.copyOf(buffer, bytesRead);
			publish("Received");
			
			return response;
			
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		return null;
	}
	
	@Override
	protected void process(List<String> chunks) {
		for (String notif : chunks) {
			jtxf_notifications.setText(notif);
		}
	}
}
