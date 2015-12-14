package gr.kzps.id2212.project.agentserver.agentservice;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.Acceptor;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;
import gr.kzps.id2212.project.client.agent.Agent;

public class AgentAcceptorImpl extends Acceptor {

	private final Logger LOG = LogManager.getLogger(AgentAcceptorImpl.class);
	private final PeerStorage peerStorage;
	
	public AgentAcceptorImpl(Socket cSocket, PeerStorage peerStorage) {
		super(cSocket);
		this.peerStorage = peerStorage;
	}
	
	@Override
	public void run() {
		try {
			inStream = new ObjectInputStream(cSocket.getInputStream());
			
			Agent agent = (Agent) inStream.readObject();
			
			AgentRunningContainer container = new AgentRunningContainer(peerStorage, agent);
			container.executeAgent();
			
			if (inStream != null)
				inStream.close();
			if (cSocket != null)
				cSocket.close();
		} catch (IOException | ClassNotFoundException ex) {
			LOG.error(ex.getMessage());
		}
		
	}

}
