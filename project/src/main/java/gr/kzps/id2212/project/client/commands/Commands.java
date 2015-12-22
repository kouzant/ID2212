package gr.kzps.id2212.project.client.commands;

public enum Commands {
	create, status, delete, purge, help, whereis, cancel, exit;
	
	public static Commands getCommand(String command) {
		return valueOf(command);
	}
}
