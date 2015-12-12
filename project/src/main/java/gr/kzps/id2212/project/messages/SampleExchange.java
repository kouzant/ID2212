package gr.kzps.id2212.project.messages;

import java.io.Serializable;
import java.util.List;

import gr.kzps.id2212.project.agentserver.overlay.BootstrapPeer;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

public class SampleExchange implements Serializable, GenericMessage {

	private static final long serialVersionUID = 7301974793419264443L;
	private final List<BootstrapPeer> sample;
	
	public SampleExchange(List<BootstrapPeer> sample) {
		this.sample = sample;
	}

	public List<BootstrapPeer> getSample() {
		return sample;
	}

	@Override
	public GenericMessage execute(PeerStorage peerStorage) {
		
		peerStorage.mergeView(sample);
		
		return null;
	}
}
