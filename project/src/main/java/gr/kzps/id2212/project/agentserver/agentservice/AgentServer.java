package gr.kzps.id2212.project.agentserver.agentservice;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.TcpServer;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

/**
 * Agent service implementation. Receives agents and executes them
 * @author Antonis Kouzoupis
 *
 */
public class AgentServer extends TcpServer {

	private final Logger LOG = LogManager.getLogger(AgentServer.class);
	private final PeerStorage peerStorage;
	
	/**
	 * @param agentPort Running port of the agent service
	 * @param peerStorage Storage of the discovery service
	 */
	public AgentServer(Integer agentPort, PeerStorage peerStorage) {
		super(agentPort);
		
		this.peerStorage = peerStorage;
	}

	@Override
	public void run() {
		try {
			sSocket = new ServerSocket(getPort());
			
			while(isRunning()) {
				cSocket = sSocket.accept();
				LOG.debug("Accepted connection from {}", cSocket.getInetAddress().toString());
				Thread acceptorThread = new Thread(
						new AgentAcceptorImpl(cSocket, peerStorage));
				acceptorThread.start();
			}
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
