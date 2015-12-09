package gr.kzps.id2212.project.agentserver.overlay;

public class PeerNotFound extends Exception {
	
	public PeerNotFound(String msg) {
		super(msg);
	}
	
	public PeerNotFound() {
		super("Peer not found in local store");
	}
}
