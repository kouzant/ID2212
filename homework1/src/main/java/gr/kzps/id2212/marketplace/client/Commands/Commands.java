package gr.kzps.id2212.marketplace.client.Commands;

public enum Commands {
	newaccount, exit;
	
	public static Commands getCommand(String command) {
		return valueOf(command);
	}
}
