package gr.kzps.id2212.project.client.commands;

/**
 * Available commands of the client console
 * @author Antonis Kouzoupis
 *
 */
public enum Commands {
	create, status, delete, purge, help, whereis, cancel, exit;
	
	public static Commands getCommand(String command) {
		return valueOf(command);
	}
}
