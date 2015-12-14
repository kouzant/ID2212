package gr.kzps.id2212.project.client.exceptions;

public class NotEnoughArguments extends Exception {
	
	private static final long serialVersionUID = 6139111854364607934L;

	public NotEnoughArguments(String str) {
		super(str);
	}

	public NotEnoughArguments() {
		super("Invalid number of arguments");
	}
}
