package gr.kzps.id2212.project.client.agent;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public class RemoteAgentImpl extends UnicastRemoteObject implements RemoteAgent {
	private static final long serialVersionUID = -5011039590439146215L;
	private final PeerAgent current;
	
	public RemoteAgentImpl(PeerAgent current) throws RemoteException {
		this.current = current;
	}
	
	@Override
	public VisitedServer whereAreYou() throws RemoteException {
		return new VisitedServer(current.getAddress().toString(),
				current.getServicePort());
	}
}
