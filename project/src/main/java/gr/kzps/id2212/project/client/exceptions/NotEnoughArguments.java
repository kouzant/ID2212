package gr.kzps.id2212.project.client.exceptions;

/**
 * Exception thrown when the user has not supplied the correct number of
 * arguments for a command
 * @author Antonis Kouzoupis
 *
 */
public class NotEnoughArguments extends Exception {
	
	private static final long serialVersionUID = 6139111854364607934L;

	public NotEnoughArguments(String str) {
		super(str);
	}

	public NotEnoughArguments() {
		super("Invalid number of arguments");
	}
}
