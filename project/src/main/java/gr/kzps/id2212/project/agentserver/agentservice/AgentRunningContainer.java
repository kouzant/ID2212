package gr.kzps.id2212.project.agentserver.agentservice;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.MalformedURLException;
import java.net.Socket;
import java.rmi.Naming;
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
import gr.kzps.id2212.project.client.agent.RemoteAgent;
import gr.kzps.id2212.project.client.agent.RemoteAgentImpl;

public class AgentRunningContainer {
	private final Logger LOG = LogManager.getLogger(AgentRunningContainer.class);

	private final PeerStorage peerStorage;
	private final Agent agent;
	private Registry registry;

	public AgentRunningContainer(PeerStorage peerStorage, Agent agent) {
		this.peerStorage = peerStorage;
		this.agent = agent;
		try {
			registry = LocateRegistry.createRegistry(1099);
		} catch (RemoteException e) {
			e.printStackTrace();
			try {
				registry = LocateRegistry.getRegistry();
			} catch (RemoteException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeAgent() {
		try {
			// I should never register the remote interface before that call!
			agent.agentArrived(this, peerStorage.getSelf());
			
			// Register agent remote interface
			LOG.debug("BEFORE REGISTER AGENT");
			registry.rebind(agent.getId().toString(), agent.getRemoteInterface());
			LOG.debug("JUST REGISTERED AGENT");
		} catch (RemoteException ex) {
			LOG.error("Could not create registry: {}", ex.getMessage());
			ex.printStackTrace();
		}
	}

	public void agentMigrate(InetAddress address, Integer servicePort) {
		try {
			// Unregister agent RMI calls
			UnicastRemoteObject.unexportObject(agent.getRemoteInterface(), true);
			
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

	public List<PeerAgent> getLocalView() throws PeerNotFound {
		return peerStorage.getLocalView();
	}
}
