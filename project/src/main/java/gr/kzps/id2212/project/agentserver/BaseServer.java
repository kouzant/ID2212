package gr.kzps.id2212.project.agentserver;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

public class BaseServer extends TcpServer {
	private final Logger LOG = LogManager.getLogger(BaseServer.class);
	
	private PeerStorage peerStorage;
	
	public BaseServer(Integer basePort, PeerStorage peerStorage) {
		super(basePort);
		this.peerStorage = peerStorage;
	}

	@Override
	public void run() {
		try {
			sSocket = new ServerSocket(getPort());

			while (isRunning()) {
				cSocket = sSocket.accept();
				LOG.debug("Accepted connection");
				// Create a new thread with the acceptor and parser
				Thread acceptorThread = new Thread(new BaseAcceptorImpl(cSocket, peerStorage));
				acceptorThread.start();
			}
		} catch (IOException ex) {
			LOG.error("Base Server: {}", ex.getMessage());
			ex.printStackTrace();
		}
	}
}
