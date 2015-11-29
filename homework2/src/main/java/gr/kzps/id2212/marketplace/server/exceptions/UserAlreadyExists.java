package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * User who is trying to register, already exists
 */
public class UserAlreadyExists extends Exception {

	private static final long serialVersionUID = 6511156706433122968L;

	public UserAlreadyExists(String msg) {
		super(msg);
	}
}
