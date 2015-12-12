package gr.kzps.id2212.project.client;

import java.net.InetAddress;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import gr.kzps.id2212.project.agentserver.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;

public class AgentImpl implements Agent, Runnable {

	private static final long serialVersionUID = 4772482720958169130L;
	
	private final InetAddress homeAddress;
	private final Integer homePort;
	private List<PeerAgent> serversVisited;
	
	private transient AgentRunningContainer container;
	private transient PeerAgent currentServer;
	
	public AgentImpl(InetAddress homeAddress, Integer homePort) {
		this.homeAddress = homeAddress;
		this.homePort = homePort;
		serversVisited = new ArrayList<>();
	}
	
	@Override
	public void run() {
		String agentName = Thread.currentThread().getName();
		System.out.println(agentName + " is doing something in " + currentServer);
		try {
			PeerAgent nextServer = nextServer();
			container.agentMigrate(nextServer.getAddress(), nextServer.getServicePort());
		} catch (PeerNotFound ex) {
			System.out.println(ex.getMessage());
			container.agentMigrate(homeAddress, homePort);
		}
	}

	@Override
	public void agentArrived(AgentRunningContainer container, PeerAgent serverReference) {
		serversVisited.add(serverReference);
		this.container = container;
		currentServer = serverReference;
		Thread agentThread = new Thread(this);
		agentThread.setName("Agent-Thread");
		agentThread.start();
	}
	
	@Override
	public String getResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("Servers visited").append("\n");
		
		serversVisited.stream()
			.forEach(s -> sb.append(s).append("\n"));
		
		return sb.toString();
	}
	
	private PeerAgent nextServer() throws PeerNotFound {
		List<PeerAgent> localView = container.getLocalView();
		Optional<PeerAgent> maybe = localView.parallelStream()
				.filter(p -> !serversVisited.contains(p))
				.findAny();
		
		if (maybe.isPresent()) {
			return maybe.get();
		}
		
		throw new PeerNotFound("No more unvisited servers");
	}
}
