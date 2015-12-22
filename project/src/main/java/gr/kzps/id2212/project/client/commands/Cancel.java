package gr.kzps.id2212.project.client.commands;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;
import gr.kzps.id2212.project.client.agent.RemoteAgent;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class Cancel extends CommandAbstr {
	private final String agentId;
	
	public Cancel(String agentId) {
		this.agentId = agentId;
	}
	
	@Override
	public void execute(AgentDB db) {
		try {
			AgentItem item = db.get(agentId);
			String uuid = item.getId().toString();
			Registry registry = LocateRegistry.getRegistry();
			RemoteAgent remoteIntf = (RemoteAgent) registry.lookup(uuid);
			remoteIntf.cancel();
			console.printPrompt();
		} catch (AgentNotFound | RemoteException | NotBoundException ex) {
			console.print(ex.getMessage());
			console.printPrompt();
		}
	}

}
