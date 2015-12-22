package gr.kzps.id2212.project.client.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

/**
 * Command to delete both the agent from the store and its result file
 * @author Antonis Kouzoupis
 *
 */
public class PurgeAgent extends CommandAbstr {
	private final String agentId;
	
	/**
	 * @param agentId Partial ID of the agent to purge
	 */
	public PurgeAgent(String agentId) {
		this.agentId = agentId;
	}
	
	/**
	 * Purge the agent
	 */
	@Override
	public void execute(AgentDB db) {
		// Remove it from db
		try {
			AgentItem agent = db.remove(agentId);
			Path resultFile = Paths.get("results", agent.getId().toString());
			
			// Delete result file
			if (resultFile.toFile().exists()) {
				resultFile.toFile().delete();
				console.print("File " + resultFile.toString() + " and the agent deleted");
			} else {
				console.print("Result file for agent " + agent.getId().toString()
						+ " is missing!");
			}
			
			console.printPrompt();
		} catch (AgentNotFound ex) {
			console.print(ex.getMessage());
			console.printPrompt();
		}
		
	}

}
