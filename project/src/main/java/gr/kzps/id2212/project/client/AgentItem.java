package gr.kzps.id2212.project.client;

import java.util.List;
import java.util.UUID;

import gr.kzps.id2212.project.client.agent.VisitedServer;
import gr.kzps.id2212.project.client.query.Query;
import gr.kzps.id2212.project.client.query.Result;

public class AgentItem {
	private final UUID id;
	private final Query query;
	private AgentStatus status;
	private List<Result> resultSet;
	private List<VisitedServer> visitedServers;
	
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