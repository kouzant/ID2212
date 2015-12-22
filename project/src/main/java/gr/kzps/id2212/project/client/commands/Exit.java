package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;

/**
 * Command to exit from the console
 * @author Antonis Kouzoupis
 *
 */
public class Exit extends CommandAbstr {

	/**
	 * Quit the console
	 */
	@Override
	public void execute(AgentDB db) {
		console.print("Bye!");
		console.halt();
	}

}
