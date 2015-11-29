package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * User who is trying to register, already exists
 */
public class UserNotRegistered extends Exception {

	private static final long serialVersionUID = 8512136716432122968L;

	public UserNotRegistered(String msg) {
		super(msg);
	}
}
