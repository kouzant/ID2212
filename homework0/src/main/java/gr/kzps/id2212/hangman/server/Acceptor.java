package gr.kzps.id2212.hangman.server;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class Acceptor implements Runnable {
	private static final Logger LOG = LogManager.getLogger(Acceptor.class);
	private final Socket cSocket;
	private final Handler handler;

	public Acceptor(Socket cSocket, PlayersTracker playersTracker) {
		this.cSocket = cSocket;
		
		handler = new Handler(playersTracker);
	}

	@Override
	public void run() {
		try {
			LOG.debug("New handler");
			BufferedInputStream bin = new BufferedInputStream(
					cSocket.getInputStream());
			BufferedOutputStream bout = new BufferedOutputStream(
					cSocket.getOutputStream());

			byte[] input = readStream(bin);

			LOG.debug("Received message from {}", cSocket.getInetAddress().getHostAddress());

			byte[] response = handler.handle(input);
			
			bout.write(response);

			bout.flush();
			bin.close();
			bout.close();
			cSocket.close();

		} catch (IOException ex) {
			LOG.error("Could not open client streams");
			ex.printStackTrace();
		}
	}

	private byte[] readStream(BufferedInputStream bin) throws IOException {
		byte[] buffer = new byte[1024];
		Integer length;
		Integer bytesRead = 0;

		while ((length = bin.read(buffer, bytesRead, 256)) != -1) {
			bytesRead += length;

			if (bytesRead == 1024) {
				break;
			}

			if (bin.available() == 0) {
				break;
			}
		}

		return Arrays.copyOf(buffer, bytesRead);
	}

}
