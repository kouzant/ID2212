package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * The requesting user is not registered
 */
public class NoUserException extends Exception {

	private static final long serialVersionUID = 3025486745671997092L;
	
	public NoUserException(String msg) {
		super(msg);
	}
}
