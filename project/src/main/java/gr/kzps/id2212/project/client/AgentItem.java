package gr.kzps.id2212.project.client;

import java.util.List;
import java.util.UUID;

import gr.kzps.id2212.project.client.agent.VisitedServer;
import gr.kzps.id2212.project.client.query.Query;
import gr.kzps.id2212.project.client.query.Result;

/**
 * Representation of an agent stored in the client. It stores the UUID of the agent,
 * the status, the provided query, the result set and the visited servers.
 * @author Antonis Kouzoupis
 *
 */
public class AgentItem {
	private final UUID id;
	private final Query query;
	private AgentStatus status;
	private List<Result> resultSet;
	private List<VisitedServer> visitedServers;
	
	/**
	 * @param id The UUID of the agent
	 * @param query The query used by that agent
	 * @param Status of the agent
	 */
	public AgentItem(UUID id, Query query, AgentStatus status) {
		this.id = id;
		this.query = query;
		this.status = status;
	}

	public AgentStatus getStatus() {
		return status;
	}

	public void setStatus(AgentStatus status) {
		this.status = status;
	}

	public List<Result> getResultSet() {
		return resultSet;
	}

	public void setResultSet(List<Result> resultSet) {
		this.resultSet = resultSet;
	}
	
	public List<VisitedServer> getVisitedServers() {
		return visitedServers;
	}
	
	public void setVisitedServers(List<VisitedServer> visitedServers) {
		this.visitedServers = visitedServers;
	}
	
	public UUID getId() {
		return id;
	}
	
	public Query getQuery() {
		return query;
	}
	
}
