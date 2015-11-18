package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * The item the user is trying to process does not exist
 */
public class ItemDoesNotExists extends Exception {

	private static final long serialVersionUID = 1612643574980623424L;

	public ItemDoesNotExists(String msg) {
		super(msg);
	}
}
