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

import gr.kzps.id2212.project.messages.SampleExchange;

public class Discovery implements Runnable {
	private final Logger LOG = LogManager.getLogger(Discovery.class);

	private PeerStorage peerStorage;
	private final PeerAgent local;
	private final Integer sampleSize;
	private List<PeerAgent> sample;
	private Socket targetSocket;
	private ObjectOutputStream outStream;
	
	public Discovery(Integer port, Integer sampleSize) throws UnknownHostException {
		local = new PeerAgent(InetAddress.getLocalHost(), port);
		peerStorage = new PeerStorage(local);
		this.sampleSize = sampleSize;
	}

	@Override
	public void run() {

		while (true) {
			try {
				TimeUnit.SECONDS.sleep(5);
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
			PeerAgent target = peerStorage.getRandomPeer();

			// Create a sample
			sample = peerStorage.createSample(sampleSize);
			sample.add(local);
			
			// Send sample
			try {
				targetSocket = new Socket(target.getAddress(), target.getPort());
				outStream = new ObjectOutputStream(targetSocket.getOutputStream());
				outStream.writeObject(new SampleExchange(sample));
				outStream.flush();
				outStream.close();
				targetSocket.close();
			} catch (IOException ex) {
				LOG.error(ex.getMessage());
			}
		}
	}

}
