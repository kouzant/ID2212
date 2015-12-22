package gr.kzps.id2212.project.agentserver.overlay;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.Acceptor;
import gr.kzps.id2212.project.agentserver.overlay.messages.GenericMessage;

/**
 * Implementation for the acceptor of Base service
 * @author Antonis Kouzoupis
 *
 */
public class BaseAcceptorImpl extends Acceptor {
	private final Logger LOG = LogManager.getLogger(BaseAcceptorImpl.class);

	private PeerStorage peerStorage;

	/**
	 * @param cSocket Reference of the client socket
	 * @param peerStorage Storage of the discovery service
	 */
	public BaseAcceptorImpl(Socket cSocket, PeerStorage peerStorage) {
		super(cSocket);
		this.peerStorage = peerStorage;
	}

	@Override
	public void run() {
		try {
			inStream = new ObjectInputStream(cSocket.getInputStream());
			outStream = new ObjectOutputStream(cSocket.getOutputStream());

			// Receive request
			GenericMessage request = (GenericMessage) inStream.readObject();

			// Execute the appropriate job...
			GenericMessage reply = request.execute(peerStorage);
			
			// ...and maybe reply
			if (reply != null) {
				outStream.writeObject(reply);
			}

			outStream.flush();

			if (outStream != null)
				outStream.close();

			if (inStream != null)
				inStream.close();

			LOG.debug("Closing client socket");
			if (cSocket != null)
				cSocket.close();
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
