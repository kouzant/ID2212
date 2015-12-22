package gr.kzps.id2212.project.client.agent;

import java.rmi.Remote;
import java.rmi.RemoteException;

/**
 * Interface for the agent's remote interface
 * @author Antonis Kouzoupis
 *
 */
public interface RemoteAgent extends Remote {
	public VisitedServer whereAreYou() throws RemoteException;
	public void cancel() throws RemoteException;
}
