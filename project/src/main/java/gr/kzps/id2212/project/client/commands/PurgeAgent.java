package gr.kzps.id2212.project.client.commands;

import java.nio.file.Path;
import java.nio.file.Paths;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class PurgeAgent extends CommandAbstr {
	private final String agentId;
	
	public PurgeAgent(String agentId) {
		this.agentId = agentId;
	}
	
	@Override
	public void execute(AgentDB db) {
		// Remove it from db
		try {
			AgentItem agent = db.remove(agentId);
			Path resultFile = Paths.get("results", agent.getId().toString());
			
			// Delete result file
			if (resultFile.toFile().exists()) {
				resultFile.toFile().delete();
			} else {
				console.print("Result file for agent " + agent.getId().toString()
						+ " is missing!");
			}
		} catch (AgentNotFound ex) {
			console.print(agentId);
		}
		
	}

}
