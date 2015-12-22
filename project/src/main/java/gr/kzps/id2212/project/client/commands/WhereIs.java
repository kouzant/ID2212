package gr.kzps.id2212.project.client.commands;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;
import gr.kzps.id2212.project.client.agent.RemoteAgent;
import gr.kzps.id2212.project.client.agent.VisitedServer;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class WhereIs extends CommandAbstr {
	private final String agentId;
	
	public WhereIs(String agentId) {
		this.agentId = agentId;
	}
	
	@Override
	public void execute(AgentDB db) {
		try {
			AgentItem agentItem = db.get(agentId);
			String uuid = agentItem.getId().toString();
			LocateRegistry.getRegistry();
			RemoteAgent agent = (RemoteAgent) Naming.lookup(uuid);
			VisitedServer hostAgent = agent.whereAreYou();
			console.print("Agent " + uuid + " is at " + hostAgent.getServer()
				+ ":" + hostAgent.getPort());
			
			hostAgent = null;
			agent = null;
		} catch (AgentNotFound | RemoteException
				| MalformedURLException | NotBoundException ex) {
			console.print(ex.getMessage());
			console.printPrompt();
		}
	}

}
