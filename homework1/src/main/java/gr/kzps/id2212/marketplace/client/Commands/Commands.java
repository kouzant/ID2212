package gr.kzps.id2212.marketplace.client.Commands;

public enum Commands {
	newaccount, register, exit, help;
	
	public static Commands getCommand(String command) {
		return valueOf(command);
	}
}
