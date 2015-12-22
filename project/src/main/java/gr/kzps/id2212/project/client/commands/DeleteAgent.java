package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

/**
 * Delete an agent from the local store but keep the result file
 * @author Antonis Kouzoupis
 *
 */
public class DeleteAgent extends CommandAbstr {

	private final String agentId;
	
	/**
	 * @param agentId Partial ID of the agent to remove
	 */
	public DeleteAgent(String agentId) {
		this.agentId = agentId;
	}
	
	/**
	 * Remove an agent from the local store but not the result file
	 */
	@Override
	public void execute(AgentDB db) {
		try {
			db.remove(agentId);
			console.print("Agent deleted");
			console.printPrompt();
		} catch (AgentNotFound ex) {
			console.print(ex.getMessage());
			console.printPrompt();
		}
	}
}
