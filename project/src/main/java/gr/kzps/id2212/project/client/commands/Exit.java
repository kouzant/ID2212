package gr.kzps.id2212.project.client.commands;

import gr.kzps.id2212.project.client.AgentDB;

public class Exit extends CommandAbstr {

	@Override
	public void execute(AgentDB db) {
		console.print("Bye!");
		console.halt();
	}

}
