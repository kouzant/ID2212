package gr.kzps.id2212.hangman.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ConnectException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.JTextField;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Keep connection reference
 */
public class Connection {

	private static final Logger LOG = LogManager.getLogger(Connection.class);
	
	private Socket socket;
	private BufferedInputStream input;
	private BufferedOutputStream output;
	private JTextField jtxt_notifications;

	public Connection() {
		super();
	}

	public void connect(String ip, Integer port) {
		try {
			socket = new Socket(ip, port);
			input = new BufferedInputStream(socket.getInputStream());
			output = new BufferedOutputStream(socket.getOutputStream());

		} catch (UnknownHostException ex) {
			jtxt_notifications.setText("Wrong host!");
			ex.printStackTrace();
		}catch (ConnectException ex) {
			jtxt_notifications.setText("Wrong port !?");
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	// Close socket and streams
	public void close() {
		try {
			if (input != null) {
				LOG.debug("Closing input stream");
				input.close();
			}
			if (output != null) {
				LOG.debug("Closing output stream");
				output.close();
			}
			if (socket != null) {
				LOG.debug("Closing socket");
				socket.close();
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	public Socket getSocket() {
		return socket;
	}
	
	public BufferedInputStream getInput() {
		return input;
	}
	
	public BufferedOutputStream getOutput() {
		return output;
	}
	
	public void setNotifications(JTextField jtxt_notifications) {
		this.jtxt_notifications = jtxt_notifications;
	}
}
