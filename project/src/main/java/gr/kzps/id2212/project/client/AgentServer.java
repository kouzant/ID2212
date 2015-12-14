package gr.kzps.id2212.project.client;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AgentServer implements Runnable {
	private final Logger LOG = LogManager.getLogger(AgentServer.class);
	private final AgentDB db;
	private final Integer port;
	private ServerSocket sSocket;
	private Socket cSocket;
	private ExecutorService threadPool;
	
	public AgentServer(AgentDB db, Integer port) {
		this.db = db;
		this.port = port;
	}
	
	@Override
	public void run() {
		while(true) {
			try {
				cSocket = sSocket.accept();
				// Spawn acceptor
				threadPool.execute(new AgentAcceptor(cSocket, db));
			} catch (IOException ex) {
				LOG.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
	}

	public void start() {
		threadPool = Executors.newCachedThreadPool();
		try {
			sSocket = new ServerSocket(port);
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			ex.printStackTrace();
		}
	}
}
