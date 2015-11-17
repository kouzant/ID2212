package gr.kzps.id2212.marketplace.server.exceptions;

public class NoBankAccountException extends Exception {

	private static final long serialVersionUID = 3052563706438691558L;

	public NoBankAccountException(String msg) {
		super(msg);
	}
}
