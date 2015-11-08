package gr.kzps.id2212.hangman.client;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

/*
 * Keep connection reference
 */
public class Connection {

	private Socket socket;
	private BufferedInputStream input;
	private BufferedOutputStream output;

	public Connection() {
		super();
	}

	public void connect(String ip, Integer port) {
		try {
			socket = new Socket(ip, port);
			input = new BufferedInputStream(socket.getInputStream());
			output = new BufferedOutputStream(socket.getOutputStream());

		} catch (UnknownHostException ex) {
			ex.printStackTrace();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

	// Close socket and streams
	public void close() {
		try {
			if (input != null) {
				input.close();
			}
			if (output != null) {
				output.close();
			}
			if (socket != null) {
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
}
