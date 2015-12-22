package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.console.ClientConsole;

/**
 * Abstract class of client commands
 * @author Antonis Kouzoupis
 *
 */
public abstract class CommandAbstr implements Command {
	protected ClientConsole console;
	
	/**
	 * Set the console reference to be able to print messages
	 * @param console The console reference
	 */
	public void setConsole(ClientConsole console) {
		this.console = console;
	}
}
