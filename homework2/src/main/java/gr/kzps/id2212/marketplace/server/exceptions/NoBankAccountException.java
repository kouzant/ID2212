package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * User trying to register does not have a bank account
 */
public class NoBankAccountException extends Exception {

	private static final long serialVersionUID = 3052563706438691558L;

	public NoBankAccountException(String msg) {
		super(msg);
	}
}
