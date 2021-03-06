package gr.kzps.id2212.project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.messages.BaseMessage;

/**
 * A non-blocking TCP server for receiving agents returning home
 * @author Antonis Kouzoupis
 *
 */
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

	/**
	 * Get running port of the client service
	 * @return Port of the client service
	 */
	public Integer getHomeport() {
		return port;
	}
	
	/**
	 * Get the agent service port of an agent server before create the agent
	 * @param ipAddress Target IP address
	 * @param basePort Running port of the Base service of an agent server
	 * @return The service port of an agent server
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public Integer getServicePort(String ipAddress, Integer basePort)
			throws UnknownHostException, IOException, ClassNotFoundException {
		Socket socket = new Socket(InetAddress.getByName(ipAddress), basePort);
		
		ObjectOutputStream out = new ObjectOutputStream(socket.getOutputStream());
		ObjectInputStream in = new ObjectInputStream(socket.getInputStream());
		out.writeObject(new BaseMessage());
		out.flush();
		
		BaseMessage reply = (BaseMessage) in.readObject();
		
		if (socket != null) {
			socket.close();
		}
		
		return reply.getAgentPort();
	}
	
	/**
	 * Start the client service in a new thread
	 */
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
