package gr.kzps.id2212.project.agentserver.overlay;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.messages.GenericMessage;
import gr.kzps.id2212.project.agentserver.overlay.messages.HelloMessage;
import gr.kzps.id2212.project.agentserver.overlay.messages.SampleExchange;

/**
 * Discovery service
 * @author Antonis Kouzoupis
 *
 */
public class Discovery implements Runnable {
	private final Logger LOG = LogManager.getLogger(Discovery.class);
	// Seconds
	private final Integer SLEEP = 5;
	
	private PeerStorage peerStorage;
	private final PeerAgent local;
	private List<PeerAgent> sample;
	private Socket targetSocket;
	private ObjectOutputStream outStream;

	/**
	 * @param local Self reference
	 * @param peerStorage Storage of the discovery service
	 */
	public Discovery(PeerAgent local, PeerStorage peerStorage) throws UnknownHostException {
		this.local = local;
		this.peerStorage = peerStorage;
	}

	/**
	 * Introduce ourself to the bootstrap node
	 * @param bAddress IP address of the bootstrap node
	 * @param bPort Running port of the Base service of the bootstrap node
	 */
	public void connectBootstrap(InetAddress bAddress, Integer bPort) {
		// Send a Hello message with my reference
		LOG.debug("Bootstraping from {}:{}", new Object[] { bAddress.toString(), bPort });
		send(new BootstrapPeer(bAddress, bPort), new HelloMessage(local));
	}

	/**
	 * Every a fixed interval exchange views with a random peer
	 */
	@Override
	public void run() {

		while (true) {
			try {
				LOG.debug("Sleeping for {} seconds", SLEEP);
				TimeUnit.SECONDS.sleep(SLEEP);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}

			try {
				PeerAgent target = peerStorage.getRandomPeer();
				// Create a sample
				sample = peerStorage.createSample();
				sample.add(local);

				// Send sample
				send(target, new SampleExchange(sample));

				LOG.debug("Local view: {}", peerStorage);

			} catch (PeerNotFound ex) {
				LOG.debug(ex.getMessage());
			}
		}
	}

	/**
	 * Send a message to a peer
	 * @param target node
	 * @param message Message to send
	 */
	private <T extends GenericMessage> void send(BootstrapPeer target, T message) {
		try {
			targetSocket = new Socket(target.getAddress(), target.getBasePort());
			outStream = new ObjectOutputStream(targetSocket.getOutputStream());
			outStream.writeObject(message);
			outStream.flush();

		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
	}

}
