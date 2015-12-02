package gr.kzps.id2212.conv.exceptions;

public class ResultNotFound extends Exception {

	private static final long serialVersionUID = -3690429028006407395L;

	public ResultNotFound() {
		super("No result found");
	}
	
	public ResultNotFound(String msg) {
		super(msg);
	}
}
