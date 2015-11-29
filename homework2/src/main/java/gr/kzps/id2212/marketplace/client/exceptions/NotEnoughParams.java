package gr.kzps.id2212.marketplace.client.exceptions;

public class NotEnoughParams extends Exception {

	private static final long serialVersionUID = -1181187608297575076L;
	
	public NotEnoughParams() {
		super("Not enough parameters");
	}
	
	public NotEnoughParams(String msg) {
		super(msg);
	}
}
