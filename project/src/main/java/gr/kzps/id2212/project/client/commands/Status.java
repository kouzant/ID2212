package gr.kzps.id2212.project.client.commands;

import java.util.Collection;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;

public class Status extends CommandAbstr {

	@Override
	// TODO I should complete this
	public void execute(AgentDB db) {
		Collection<AgentItem> agents = db.getValues();
		
		agents.stream().forEach(a -> {
			console.print(a.getStatus().toString());
		});
	}

}
