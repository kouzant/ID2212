package gr.kzps.id2212.project.agentserver.overlay;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class PeerStorage {
	private volatile List<PeerAgent> peerAgents;
	private Lock storeLock;
	private Random rand;

	public PeerStorage(PeerAgent local) {
		peerAgents = new ArrayList<>();
		peerAgents.add(local);
		storeLock = new ReentrantLock();
		rand = new Random();
	}

	public void addPeer(PeerAgent peer) {
		Integer preSize = peerAgents.size();

		if (!peerAgents.contains(peer)) {
			storeLock.lock();
			peerAgents.add(peer);
			storeLock.unlock();

			if (preSize < 2) {
				notify();
			}
		}
	}

	public List<PeerAgent> createSample(Integer sampleSize) {
		Integer actualSize = Math.min(sampleSize, peerAgents.size());
		List<PeerAgent> sample = new ArrayList<>();
		
		while (sample.size() < actualSize) {
			sample.add(getRandomPeer());
		}
		
		return sample;
	}
	
	public PeerAgent getRandomPeer() {
		while (isEmpty()) {
			try {
				wait();
			} catch (InterruptedException ex) {
				ex.printStackTrace();
			}
		}
		// Exclude local reference
		Integer rndPeer = ThreadLocalRandom.current().nextInt(1, peerAgents.size());
		return peerAgents.get(rndPeer);
	}

	public PeerAgent getPeer(PeerAgent peer) throws PeerNotFound {

		Optional<PeerAgent> maybe = peerAgents.stream().filter(a -> a.equals(peer)).findFirst();

		if (maybe.isPresent()) {
			return maybe.get();
		} else {
			throw new PeerNotFound();
		}
	}

	public void removePeer(PeerAgent peer) {
		storeLock.lock();
		peerAgents.remove(peer);
		storeLock.unlock();
	}

	private Boolean isEmpty() {
		// Do not count the self reference
		return peerAgents.size() < 2;
	}
}
