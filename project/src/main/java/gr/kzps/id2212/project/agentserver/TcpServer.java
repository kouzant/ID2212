package gr.kzps.id2212.project.agentserver;

import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public abstract class TcpServer implements Runnable {
	private final Logger LOG = LogManager.getLogger(TcpServer.class);
	
	private Integer tcpPort;
	private Boolean running = true;
	
	protected ServerSocket sSocket;
	protected Socket cSocket;
	
	public TcpServer(Integer tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	public abstract void run();
	
	protected Integer getPort() {
		return tcpPort;
	}
		
	protected Boolean isRunning() {
		return running;
	}
	
	protected void stopRunning() {
		running = false;
	}
}
