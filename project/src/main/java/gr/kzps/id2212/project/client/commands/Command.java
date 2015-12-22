package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;

/**
 * Interface for client commands
 * @author Antonis Kouzoupis
 *
 */
public interface Command {
	/**
	 * Operation to execute upon receive of that command
	 * @param db Client's local store
	 */
	public void execute(AgentDB db);
}
