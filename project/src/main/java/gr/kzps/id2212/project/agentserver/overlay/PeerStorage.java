package gr.kzps.id2212.project.agentserver.overlay;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Storage of the discovery service. Store discovered peers.
 * @author Antonis Kouzoupis
 *
 */
public class PeerStorage {
	private final Logger LOG = LogManager.getLogger(PeerStorage.class);

	private volatile List<PeerAgent> peerAgents;
	private Lock storeLock;
	private Integer sampleSize;

	/**
	 * @param local Self reference
	 * @param sampleSize Size of the sample to exchange
	 */
	public PeerStorage(PeerAgent local, Integer sampleSize) {
		peerAgents = new ArrayList<>();
		this.sampleSize = sampleSize;
		peerAgents.add(local);
		storeLock = new ReentrantLock();
	}

	/**
	 * Add a peer in the storage
	 * @param peer New peer
	 */
	public void addPeer(PeerAgent peer) {

		if (!peerAgents.contains(peer)) {
			storeLock.lock();
			peerAgents.add(peer);
			storeLock.unlock();
		}
	}

	/**
	 * Merge a received view with the local one
	 * @param view Received view from an exchange message
	 */
	public void mergeView(List<PeerAgent> view) {
		storeLock.lock();
		List<PeerAgent> toMerge = view.stream()
				.distinct()
				.filter(v -> !peerAgents.contains(v))
				.collect(Collectors.toList());

		peerAgents.addAll(toMerge);
		storeLock.unlock();
	}
	
	/**
	 * Get the local view
	 * @return A list of peers in the local view
	 * @throws PeerNotFound
	 */
	public List<PeerAgent> getLocalView() throws PeerNotFound {
		if (peerAgents.size() > 1) {
			return peerAgents;
		}
		
		throw new PeerNotFound("Local view is Empty");
	}

	/**
	 * Create a sample from the local view
	 * @return Sample list with nodes
	 * @throws PeerNotFound
	 */
	public List<PeerAgent> createSample() throws PeerNotFound {
		Integer actualSize = Math.min(sampleSize, peerAgents.size());
		List<PeerAgent> sample = new ArrayList<>();

		while (sample.size() < actualSize) {
			sample.add(getRandomPeer());
		}

		return sample;
	}

	/**
	 * Get a random peer from the local view
	 * @return A random peer
	 * @throws PeerNotFound
	 */
	public PeerAgent getRandomPeer() throws PeerNotFound {
		if (!isEmpty()) {
			// Exclude local reference
			Integer rndPeer = ThreadLocalRandom.current().nextInt(1, peerAgents.size());

			return peerAgents.get(rndPeer);
		}

		throw new PeerNotFound("Local view is empty");
	}

	/**
	 * Find a node in the storage with the given IP address and service port
	 * @param address IP address
	 * @param servicePort Running port of the agent service
	 * @return Corresponding agent
	 * @throws PeerNotFound
	 */
	public PeerAgent getPeer(InetAddress address, Integer servicePort) throws PeerNotFound {

		Optional<PeerAgent> maybe = peerAgents.stream()
				.filter(a -> a.getAddress().equals(address)
						&& a.getServicePort().equals(servicePort))
				.findFirst();

		if (maybe.isPresent()) {
			return maybe.get();
		} else {
			throw new PeerNotFound();
		}
	}

	/**
	 * Remove a node from the storage
	 * @param peer The node to remove
	 */
	public void removePeer(PeerAgent peer) {
		storeLock.lock();
		peerAgents.remove(peer);
		storeLock.unlock();
	}

	/**
	 * Get a self reference. The first element of the storage is always ourself
	 * @return A self reference
	 */
	public PeerAgent getSelf() {
		return peerAgents.get(0);
	}

	/**
	 * Check whether storage list is empty. Our self reference does not count
	 * @return Empty or not
	 */
	private Boolean isEmpty() {
		// Do not count the self reference
		return peerAgents.size() < 2;
	}

	@Override
	public String toString() {
		StringBuilder sb = new StringBuilder();

		peerAgents.stream().forEach(p -> sb.append(p).append("\n"));

		return sb.toString();
	}
}
