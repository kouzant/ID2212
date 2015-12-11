package gr.kzps.id2212.project.messages;

import java.io.Serializable;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public class HelloMessage implements Serializable {

	private static final long serialVersionUID = -2658849552972241367L;

	private final PeerAgent peer;
	
	public HelloMessage(PeerAgent peer) {
		this.peer = peer;
	}
	
	public PeerAgent getPeer() {
		return peer;
	}
}
