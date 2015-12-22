package gr.kzps.id2212.project.client.agent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import gr.kzps.id2212.project.agentserver.agentservice.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

/**
 * Implementation of an agent's remote interface
 * @author Antonis Kouzoupis
 *
 */
public class RemoteAgentImpl extends UnicastRemoteObject implements RemoteAgent {
	private static final long serialVersionUID = -5011039590439146215L;
	private final PeerAgent current;
	private final AgentRunningContainer container;
	
	/**
	 * @param container Running container in agent server
	 * @throws RemoteException
	 */
	public RemoteAgentImpl(AgentRunningContainer container) throws RemoteException {
		this.current = container.getSelf();
		this.container = container;
	}
	
	/**
	 * Responds to clients request for the location of the agent
	 * @return The current location of the agent
	 * @throws RemoteException
	 */
	@Override
	public VisitedServer whereAreYou() throws RemoteException {
		return new VisitedServer(current.getAddress().toString(),
				current.getServicePort());
	}
	
	/**
	 * The client can instruct the agent to cancel whatever is doing
	 * and return home
	 * @throws RemoteException
	 */
	@Override
	public void cancel() throws RemoteException {
		container.cancelAgent();
	}
}
