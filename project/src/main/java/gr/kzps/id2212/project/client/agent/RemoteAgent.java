package gr.kzps.id2212.project.client.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface RemoteAgent extends Remote {
	public VisitedServer whereAreYou() throws RemoteException;
}
