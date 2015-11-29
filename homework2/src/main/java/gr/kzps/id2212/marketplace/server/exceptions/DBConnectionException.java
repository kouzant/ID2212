package gr.kzps.id2212.marketplace.server.exceptions;

/**
 * Something is wrong with the bank balance
 */
public class DBConnectionException extends Exception {

	private static final long serialVersionUID = 3463413928734845265L;

	public DBConnectionException(String msg) {
		super(msg);
	}
}
