package gr.kzps.id2212.project.agentserver.overlay.messages;

import java.io.Serializable;
import java.util.List;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

/**
 * Message for exchanging local views of the overlay
 * @author Antonis Kouzoupis
 *
 */
public class SampleExchange implements Serializable, GenericMessage {

	private static final long serialVersionUID = 7301974793419264443L;
	private final List<PeerAgent> sample;
	
	/**
	 * @param sample A sample of the local view of a node
	 */
	public SampleExchange(List<PeerAgent> sample) {
		this.sample = sample;
	}

	public List<PeerAgent> getSample() {
		return sample;
	}

	
	/**
	 * Action performed when receive this kind of message
	 * @param peerStorage Storage of the discovery service
	 * @return Do not reply back, just merge the received view
	 */
	@Override
	public GenericMessage execute(PeerStorage peerStorage) {
		
		peerStorage.mergeView(sample);
		
		return null;
	}
}
