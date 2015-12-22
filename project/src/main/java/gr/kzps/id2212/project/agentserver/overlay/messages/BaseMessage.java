package gr.kzps.id2212.project.agentserver.overlay.messages;

import java.io.Serializable;

import gr.kzps.id2212.project.agentserver.Cache;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

/**
 * Request the agent service port of a server
 * @author Antonis Kouzoupis
 *
 */
public class BaseMessage implements Serializable, GenericMessage {

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

	/**
	 * Action performed when receive this kind of message
	 * @param peerStorage Storage of the discovery service
	 * @return Reply to the client with the service port
	 */
	@Override
	public BaseMessage execute(PeerStorage peerStorage) {
	
		BaseMessage reply = new BaseMessage();
		reply.setAgentPort(Cache.getInstance().getAgentPort());
		
		return reply;
	}
}
