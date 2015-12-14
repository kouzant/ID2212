package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.console.ClientConsole;

public abstract class CommandAbstr implements Command {
	protected ClientConsole console;
	
	public void setConsole(ClientConsole console) {
		this.console = console;
	}
}
