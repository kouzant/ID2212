package gr.kzps.id2212.project.client;

import java.io.Serializable;

import gr.kzps.id2212.project.agentserver.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public interface Agent extends Serializable {
	public void agentArrived(AgentRunningContainer container, PeerAgent serverReference);
	public String getResult();
}
