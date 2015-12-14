package gr.kzps.id2212.project.client.agent;

import java.io.Serializable;

import gr.kzps.id2212.project.agentserver.agentservice.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public interface Agent extends Serializable {
	public void agentArrived(AgentRunningContainer container, PeerAgent serverReference);
	public String getResult();
}
