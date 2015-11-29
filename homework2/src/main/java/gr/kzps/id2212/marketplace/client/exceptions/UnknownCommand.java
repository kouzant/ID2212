package gr.kzps.id2212.marketplace.client.exceptions;

public class UnknownCommand extends Exception {

	private static final long serialVersionUID = 5240093562691118689L;

	public UnknownCommand() {
		super("Unknown command");
	}
	
	public UnknownCommand(String msg) {
		super(msg);
	}
}
