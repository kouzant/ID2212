package gr.kzps.id2212.project.agentserver;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AgentServer extends TcpServer {

	private final Logger LOG = LogManager.getLogger(AgentServer.class);
	
	public AgentServer(Integer agentPort) {
		super(agentPort);
	}

	@Override
	public void run() {
		try {
			sSocket = new ServerSocket(getPort());
			
			while(isRunning()) {
				cSocket = sSocket.accept();
				LOG.debug("Accepted connection");
				Thread acceptorThread = new Thread(new AgentAcceptorImpl(cSocket));
				acceptorThread.start();
			}
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
