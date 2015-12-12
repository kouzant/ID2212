package gr.kzps.id2212.project.agentserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.Agent;

public class AgentAcceptorImpl extends Acceptor {

	private final Logger LOG = LogManager.getLogger(AgentAcceptorImpl.class);
	
	public AgentAcceptorImpl(Socket cSocket) {
		super(cSocket);
	}
	
	@Override
	public void run() {
		try {
			inStream = new ObjectInputStream(cSocket.getInputStream());
			
			Agent agent = (Agent) inStream.readObject();
			
			agent.agentArrived();
			
			if (inStream != null)
				inStream.close();
			if (cSocket != null)
				cSocket.close();
		} catch (IOException | ClassNotFoundException ex) {
			LOG.error(ex.getMessage());
		}
		
	}

}
