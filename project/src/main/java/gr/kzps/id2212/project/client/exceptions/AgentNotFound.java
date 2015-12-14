package gr.kzps.id2212.project.client.exceptions;

public class AgentNotFound extends Exception {
	private static final long serialVersionUID = 4774015689031290312L;
	public AgentNotFound(String msg) {
		super(msg);
	}
	public AgentNotFound() {
		super("Agent not found");
	}
}
