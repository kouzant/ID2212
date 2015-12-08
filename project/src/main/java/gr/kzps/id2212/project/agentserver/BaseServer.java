package gr.kzps.id2212.project.agentserver;

import java.io.IOException;
import java.net.ServerSocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class BaseServer extends TcpServer {
	private final Logger LOG = LogManager.getLogger(BaseServer.class);

	public BaseServer(Integer basePort) {
		super(basePort);
	}

	@Override
	public void run() {
		try {
			sSocket = new ServerSocket(getPort());

			while (isRunning()) {
				cSocket = sSocket.accept();
				LOG.debug("Accepted connection");
				// Create a new thread with the acceptor and parser
				Thread acceptorThread = new Thread(new BaseAcceptorImpl(cSocket));
				acceptorThread.start();
			}
		} catch (IOException ex) {
			LOG.error("Base Server: {}", ex.getMessage());
		}
	}
}
