package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * User who is trying to register, already exists
 */
public class PasswordIsSmallException extends Exception {

	private static final long serialVersionUID = 1515136757332290963L;

	public PasswordIsSmallException(String msg) {
		super(msg);
	}
}
