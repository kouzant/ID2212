package gr.kzps.id2212.project.client.agent;

import java.io.Serializable;
import java.net.InetAddress;
import java.rmi.RemoteException;
import java.util.List;
import java.util.UUID;

import gr.kzps.id2212.project.agentserver.agentservice.AgentRunningContainer;
import gr.kzps.id2212.project.client.query.Result;

/**
 * Interface of an agent
 * @author Antonis Kouzoupis
 *
 */
public interface Agent extends Serializable {
	public void agentArrived(AgentRunningContainer container)
			throws RemoteException;
	public List<VisitedServer> getVisitedServers();
	public List<Result> getResultSet();
	public RemoteAgent getRemoteInterface();
	public UUID getId();
	public InetAddress getHomeAddress();
	public Integer getHomePort();
}
