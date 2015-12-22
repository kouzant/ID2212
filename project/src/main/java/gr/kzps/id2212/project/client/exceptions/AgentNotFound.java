package gr.kzps.id2212.project.client.exceptions;

/**
 * Exception thrown when a agent is not found in the local store
 * @author Antonis Kouzoupis
 *
 */
public class AgentNotFound extends Exception {
	private static final long serialVersionUID = 4774015689031290312L;
	public AgentNotFound(String msg) {
		super(msg);
	}
	public AgentNotFound() {
		super("Agent not found");
	}
}
