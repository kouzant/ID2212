package gr.kzps.id2212.project.messages;

import java.io.Serializable;

public class BaseMessage implements Serializable {

	private static final long serialVersionUID = 2538126070475359291L;
	
	private Integer agentPort;
	
	public BaseMessage() {
		super();
	}
	
	public Integer getAgentPort() {
		return agentPort;
	}
	
	public void setAgentPort(Integer agentPort) {
		this.agentPort = agentPort;
	}
}
