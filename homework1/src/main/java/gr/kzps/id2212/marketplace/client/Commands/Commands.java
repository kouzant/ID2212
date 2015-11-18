package gr.kzps.id2212.marketplace.client.Commands;

/**
 * Available commands
 */
public enum Commands {
	newaccount, register, unregister, sell, list, buy, wish, exit, help;
	
	public static Commands getCommand(String command) {
		return valueOf(command);
	}
}
