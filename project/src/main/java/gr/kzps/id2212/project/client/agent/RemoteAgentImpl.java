package gr.kzps.id2212.project.client.agent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import gr.kzps.id2212.project.agentserver.agentservice.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public class RemoteAgentImpl extends UnicastRemoteObject implements RemoteAgent {
	private static final long serialVersionUID = -5011039590439146215L;
	private final PeerAgent current;
	private final AgentRunningContainer container;
	
	public RemoteAgentImpl(AgentRunningContainer container) throws RemoteException {
		this.current = container.getSelf();
		this.container = container;
	}
	
	@Override
	public VisitedServer whereAreYou() throws RemoteException {
		return new VisitedServer(current.getAddress().toString(),
				current.getServicePort());
	}
	
	@Override
	public void cancel() throws RemoteException {
		container.cancelAgent();
	}
}
