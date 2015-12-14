package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;

public interface Command {
	public void execute(AgentDB db);
}
