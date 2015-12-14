package gr.kzps.id2212.project.client.exceptions;

public class UnknownCommand extends Exception {
	
	private static final long serialVersionUID = 7505153558687490779L;

	public UnknownCommand(String msg) {
		super(msg);
	}
	
	public UnknownCommand() {
		super("Unknown command");
	}
}
