package gr.kzps.id2212.project.messages;

import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

public interface GenericMessage {
	public GenericMessage execute(PeerStorage peerStorage);
}
