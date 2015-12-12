package gr.kzps.id2212.project.agentserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;
import gr.kzps.id2212.project.client.Agent;

public class AgentRunningContainer {
	private final Logger LOG = LogManager.getLogger(AgentRunningContainer.class);
	
	private final PeerStorage peerStorage;
	private final Agent agent;
	
	public AgentRunningContainer(PeerStorage peerStorage, Agent agent) {
		this.peerStorage = peerStorage;
		this.agent = agent;
	}
	
	public void executeAgent() {
		agent.agentArrived(this, peerStorage.getSelf());
	}
	
	public void agentMigrate(InetAddress address, Integer servicePort) {
		try {
			Socket socket = new Socket(address, servicePort);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			
			outStream.writeObject(agent);
			outStream.flush();
			
			if (outStream != null) {
				outStream.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
