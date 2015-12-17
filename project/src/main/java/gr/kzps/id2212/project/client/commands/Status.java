package gr.kzps.id2212.project.client.commands;

import java.util.Collection;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;

public class Status extends CommandAbstr {

	@Override
	public void execute(AgentDB db) {
		Collection<AgentItem> agents = db.getValues();

		console.print("\t Agent-ID \t\t\t || Status");

		if (agents.isEmpty()) {
			console.print("No agents stored");
			console.printPrompt();
		} else {
			agents.stream().forEach(a -> {
				console.print(a.getId() + "\t|| " + a.getStatus().toString());
			});
			
			console.printPrompt();
		}
	}

}
