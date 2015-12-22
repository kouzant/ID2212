package gr.kzps.id2212.project.agentserver.overlay.messages;

import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

/**
 * Interface for the messages exchanged for the discovery service
 * @author Antonis Kouzoupis
 *
 */
public interface GenericMessage {
	public GenericMessage execute(PeerStorage peerStorage);
}
