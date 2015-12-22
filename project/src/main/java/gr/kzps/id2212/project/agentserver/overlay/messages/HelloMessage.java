package gr.kzps.id2212.project.agentserver.overlay.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

/**
 * Message for a new node to introduce itself to the overlay
 * @author Antonis Kouzoupis
 *
 */
public class HelloMessage implements Serializable, GenericMessage {

	private static final long serialVersionUID = -2658849552972241367L;

	private final PeerAgent peer;
	
	/**
	 * @param peer A reference of the new node
	 */
	public HelloMessage(PeerAgent peer) {
		this.peer = peer;
	}
	
	public PeerAgent getPeer() {
		return peer;
	}

	
	/**
	 * Action performed when receive this kind of message
	 * @param peerStorage Storage of the discovery service
	 * @return Reply back with a sample of our view
	 */
	@Override
	public GenericMessage execute(PeerStorage peerStorage) {
		List<PeerAgent> sample = new ArrayList<>();
		try {
			sample = peerStorage.createSample();
		} catch (PeerNotFound ex) {
			//LOG.debug(ex.getMessage());
		}
		
		peerStorage.addPeer(peer);
		sample.add(peerStorage.getSelf());
		
		return new SampleExchange(sample);
	}
}
