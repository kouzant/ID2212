package gr.kzps.id2212.project.client.commands;

public enum Commands {
	create;
	
	public static Commands getCommand(String command) {
		return valueOf(command);
	}
}
