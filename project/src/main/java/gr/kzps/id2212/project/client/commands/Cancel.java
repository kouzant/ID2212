package gr.kzps.id2212.project.client.commands;

import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentItem;
import gr.kzps.id2212.project.client.agent.RemoteAgent;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

/**
 * Command to cancel a running agent
 * @author Antonis Kouzoupis
 *
 */
public class Cancel extends CommandAbstr {
	private final String agentId;
	
	/**
	 * @param agentId Partial ID of the agent
	 */
	public Cancel(String agentId) {
		this.agentId = agentId;
	}
	
	/**
	 * Make an RMI call to the agent's remote interface to return home
	 */
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
