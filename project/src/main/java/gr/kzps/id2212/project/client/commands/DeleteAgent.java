package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class DeleteAgent extends CommandAbstr {

	private final String agentId;
	
	public DeleteAgent(String agentId) {
		this.agentId = agentId;
	}
	
	@Override
	public void execute(AgentDB db) {
		try {
			db.remove(agentId);
		} catch (AgentNotFound ex) {
			console.print(ex.getMessage());
		}
	}
}
