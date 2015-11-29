package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * User who is trying to register, already exists
 */
public class IncorrectPasswordException extends Exception {

	private static final long serialVersionUID = 1512136756332090968L;

	public IncorrectPasswordException(String msg) {
		super(msg);
	}
}
