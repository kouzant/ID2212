package gr.kzps.id2212.project.messages;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import gr.kzps.id2212.project.agentserver.overlay.BootstrapPeer;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

public class HelloMessage implements Serializable, GenericMessage {

	private static final long serialVersionUID = -2658849552972241367L;

	private final BootstrapPeer peer;
	
	public HelloMessage(BootstrapPeer peer) {
		this.peer = peer;
	}
	
	public BootstrapPeer getPeer() {
		return peer;
	}

	@Override
	public GenericMessage execute(PeerStorage peerStorage) {
		List<BootstrapPeer> sample = new ArrayList<>();
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
