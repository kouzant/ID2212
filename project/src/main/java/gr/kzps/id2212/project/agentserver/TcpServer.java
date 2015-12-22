package gr.kzps.id2212.project.agentserver;

import java.net.ServerSocket;
import java.net.Socket;

/**
 * Abstract class for implementing a non-blocking TCP server
 * @author Antonis Kouzoupis
 *
 */
public abstract class TcpServer implements Runnable {	
	private Integer tcpPort;
	private Boolean running = true;
	
	protected ServerSocket sSocket;
	protected Socket cSocket;
	
	public TcpServer(Integer tcpPort) {
		this.tcpPort = tcpPort;
	}
	
	public abstract void run();
	
	/**
	 * Get running port
	 * @return Running port
	 */
	protected Integer getPort() {
		return tcpPort;
	}
		
	/**
	 * Control whether server should be running
	 * @return Should be running?
	 */
	protected Boolean isRunning() {
		return running;
	}
	
	/**
	 * Stop the server
	 */
	protected void stopRunning() {
		running = false;
	}
}
