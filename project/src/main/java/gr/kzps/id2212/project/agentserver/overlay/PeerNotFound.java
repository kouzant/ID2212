package gr.kzps.id2212.project.agentserver.overlay;

/**
 * Exception for not finding a node in the discovery service storage
 * @author Antonis Kouzoupis
 *
 */
public class PeerNotFound extends Exception {
	
	private static final long serialVersionUID = -585926348742957508L;

	public PeerNotFound(String msg) {
		super(msg);
	}
	
	public PeerNotFound() {
		super("Peer not found in local store");
	}
}
