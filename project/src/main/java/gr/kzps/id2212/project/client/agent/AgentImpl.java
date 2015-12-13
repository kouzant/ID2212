package gr.kzps.id2212.project.client.agent;

import java.io.IOException;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

import gr.kzps.id2212.project.agentserver.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.Cache;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;

public class AgentImpl implements Agent, Runnable {

	private static final long serialVersionUID = 4772482720958169130L;
	
	private final InetAddress homeAddress;
	private final Integer homePort;
	private final UUID id;
	private final Query query;
	private List<PeerAgent> visitedServers;
	// TODO to be removed!!!
	private List<String> resultFiles;
	
	private transient AgentRunningContainer container;
	private transient PeerAgent currentServer;
	
	public AgentImpl(UUID id, InetAddress homeAddress, Integer homePort,
			Query query) {
		this.id = id;
		this.homeAddress = homeAddress;
		this.homePort = homePort;
		this.query = query;
		visitedServers = new ArrayList<>();
		resultFiles = new ArrayList<>();
	}
	
	@Override
	public void run() {
		String agentName = Thread.currentThread().getName();
		System.out.println(agentName + " is doing something in " + currentServer);
		// Search in Cache.getInstance().getSearchPath()
		Path searchDir = Paths.get(Cache.getInstance().getSearchPath());
		resultFiles = listFiles(searchDir).stream()
				.map(p -> p.toAbsolutePath().toString())
				.collect(Collectors.toList());
		
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
		visitedServers.add(serverReference);
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
		
		visitedServers.stream()
			.forEach(s -> sb.append(s).append("\n"));
		
		sb.append("Files found").append("\n");
		resultFiles.stream()
			.forEach(p -> sb.append(p).append("\n"));
		
		return sb.toString();
	}
	
	private List<Path> listFiles(Path directory) {
		List<Path> files = new ArrayList<>();
		
		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path file: stream) {
				// That's nasty...
				if (Files.isDirectory(file)) {
					files.addAll(listFiles(file));
				} else {
					files.add(file);
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}
		
		return files;
	}
	
	private PeerAgent nextServer() throws PeerNotFound {
		List<PeerAgent> localView = container.getLocalView();
		Optional<PeerAgent> maybe = localView.parallelStream()
				.filter(p -> !visitedServers.contains(p))
				.findAny();
		
		if (maybe.isPresent()) {
			return maybe.get();
		}
		
		throw new PeerNotFound("No more unvisited servers");
	}
}
