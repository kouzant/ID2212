package gr.kzps.id2212.hangman.server;

import gr.kzps.id2212.hangman.general.OpCodes;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.Arrays;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Accept every client connection
 */
public class Acceptor implements Runnable {
	private static final Logger LOG = LogManager.getLogger(Acceptor.class);
	private final Socket cSocket;
	private final Handler handler;
	private boolean running;

	public Acceptor(Socket cSocket, PlayersTracker playersTracker) {
		this.cSocket = cSocket;
		this.running = true;

		handler = new Handler(playersTracker);
	}

	@Override
	public void run() {
		try {
			LOG.debug("New handler");
			// Get the streams
			BufferedInputStream bin = new BufferedInputStream(
					cSocket.getInputStream());
			BufferedOutputStream bout = new BufferedOutputStream(
					cSocket.getOutputStream());

			while (isRunning() && !cSocket.isClosed()) {
				// Fetch the request from the client
				byte[] input = readStream(bin);

				LOG.debug("Received message from {}", cSocket.getInetAddress()
						.getHostAddress());

				// Emulate network latency
				try {
					Integer latency = ThreadLocalRandom.current().nextInt(3000);
					LOG.debug("Sleeping for {} ms", latency);
					TimeUnit.MILLISECONDS.sleep(latency);
					;
				} catch (InterruptedException ex) {
					ex.printStackTrace();
				}

				// Handle the request
				if (input.length != 0) {
					byte[] response = handler.handle(input);

					// Send back the appropriate answer
					bout.write(response);
					bout.flush();

					// If client exit the program we close the connection and
					// terminate that thread
					if (response[0] == OpCodes.CLOSE) {
						stop();
					}
				} else {
					LOG.warn("Received empty request, closing the connection");
					stop();
				}
			}

			LOG.debug("Closing connection");

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

		// Remove empty bytes
		return Arrays.copyOf(buffer, bytesRead);
	}

	private boolean isRunning() {
		return running;
	}

	private void stop() {
		running = false;
	}
}
