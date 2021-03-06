package gr.kzps.id2212.project.agentserver.agentservice;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;

import org.apache.logging.log4j.LogManager;

import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;
import gr.kzps.id2212.project.client.agent.Agent;

/**
 * Container class for running the agent and perform agent related operations
 * @author Antonis Kouzoupis
 *
 */
public class AgentRunningContainer {
	private final Logger LOG = LogManager.getLogger(AgentRunningContainer.class);

	private final PeerStorage peerStorage;
	private final Agent agent;
	private Registry registry;

	/**
	 * @param peerStorage Discovery service storage
	 * @param agent Agent to execute
	 */
	public AgentRunningContainer(PeerStorage peerStorage, Agent agent) {
		this.peerStorage = peerStorage;
		this.agent = agent;
		try {
			registry = LocateRegistry.createRegistry(1099);
			LOG.debug("Created new RMI registry");
		} catch (RemoteException e) {
			e.printStackTrace();
			try {
				registry = LocateRegistry.getRegistry();
				LOG.debug("Got an existing RMI registry");
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	/**
	 * Spawn the agent thread and its remote interface
	 */
	public void executeAgent() {
		try {
			// I should never register the remote interface before that call!
			agent.agentArrived(this);
			
			// Register agent remote interface
			registry.rebind(agent.getId().toString(), agent.getRemoteInterface());
		} catch (RemoteException ex) {
			LOG.error("Could not create registry: {}", ex.getMessage());
			ex.printStackTrace();
		}
	}

	/**
	 * Migrate an agent to another agent server
	 * @param address IP Address of the target
	 * @param servicePort Running port of the target agent service
	 */
	public void agentMigrate(InetAddress address, Integer servicePort) {
		try {
			// Unregister agent RMI calls
			UnicastRemoteObject.unexportObject(agent.getRemoteInterface(), true);
			
			// Send the agent
			Socket socket = new Socket(address, servicePort);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());

			outStream.writeObject(agent);
			outStream.flush();

			if (outStream != null) {
				outStream.close();
			}
			if (socket != null) {
				socket.close();
			}
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		}
	}

	/**
	 * Migrate the agent to its home instead of another agent server
	 */
	public void cancelAgent() {
		InetAddress homeAddress = agent.getHomeAddress();
		Integer homePort = agent.getHomePort();
		
		agentMigrate(homeAddress, homePort);
	}
	
	/**
	 * Get a reference of the current agent server
	 * @return Self reference
	 */
	public PeerAgent getSelf() {
		return peerStorage.getSelf();
	}
	
	/**
	 * Get a list of all the discovered agent servers
	 * @return List of the local view
	 * @throws PeerNotFound
	 */
	public List<PeerAgent> getLocalView() throws PeerNotFound {
		return peerStorage.getLocalView();
	}
}
