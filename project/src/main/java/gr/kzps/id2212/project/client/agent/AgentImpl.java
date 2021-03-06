package gr.kzps.id2212.project.client.agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.rmi.RemoteException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.apache.tika.exception.TikaException;
import org.apache.tika.metadata.Metadata;
import org.apache.tika.parser.AutoDetectParser;
import org.apache.tika.sax.BodyContentHandler;
import org.xml.sax.SAXException;

import gr.kzps.id2212.project.agentserver.Cache;
import gr.kzps.id2212.project.agentserver.agentservice.AgentRunningContainer;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;
import gr.kzps.id2212.project.client.query.Query;
import gr.kzps.id2212.project.client.query.Result;
import gr.kzps.id2212.project.client.query.parameterOperators.DateOperators;
import gr.kzps.id2212.project.client.query.parameterOperators.ParameterSwitch;
import gr.kzps.id2212.project.utils.Utilities;

/**
 * Agent class traveling around the network gathering information
 * @author Antonis Kouzoupis
 *
 */
public class AgentImpl implements Agent, Runnable {

	private static final long serialVersionUID = 4772482720958169130L;

	private final InetAddress homeAddress;
	private final Integer homePort;
	private final UUID id;
	private final Query query;
	private final Pattern pattern;
	private List<PeerAgent> visitedServers;
	private List<Result> resultList;

	private transient RemoteAgent remoteInterface;
	private transient AgentRunningContainer container;
	private transient PeerAgent currentServer;
	private transient List<Result> localResultList;
	private transient Utilities utils;
	
	/**
	 * @param id UUID of the agent
	 * @param homeAddress Home IP address of the agent
	 * @param homePort Running port of the client service
	 * @param query Search query for files
	 * @throws RemoteException
	 */
	public AgentImpl(UUID id, InetAddress homeAddress, Integer homePort, Query query)
		throws RemoteException {
		this.id = id;
		this.homeAddress = homeAddress;
		this.homePort = homePort;
		this.query = query;
		visitedServers = new ArrayList<>();
		resultList = new ArrayList<>();
		// "\\S+\\s*\\S*.((pdf)|(odt))$"
		// Currently accept only pdf
		pattern = Pattern.compile("\\S+\\s*\\S*.(pdf)$");
	}

	@Override
	public UUID getId() {
		return id;
	}
	
	@Override
	public InetAddress getHomeAddress() {
		return homeAddress;
	}
	
	@Override
	public Integer getHomePort() {
		return homePort;
	}
	
	@Override
	public void run() {
		String agentName = Thread.currentThread().getName();
		System.out.println(agentName + " is doing something in " + currentServer);
		// Search directory of the server
		Path searchDir = Paths.get(Cache.getInstance().getSearchPath());
		localResultList = filterFiles(listFiles(searchDir));
		resultList.addAll(localResultList);
		localResultList.clear();

		// Calculate next destination
		// If there are no unvisited servers, go home
		try {
			PeerAgent nextServer = nextServer();
			container.agentMigrate(nextServer.getAddress(), nextServer.getServicePort());
		} catch (PeerNotFound ex) {
			System.out.println(ex.getMessage());
			container.agentMigrate(homeAddress, homePort);
		}
	}

	/**
	 * Callback called from the agent server container when the agent arrives the server
	 * @param container Running container in the server
	 * @throws RemoteException
	 */
	@Override
	public void agentArrived(AgentRunningContainer container)
			throws RemoteException {
		visitedServers.add(container.getSelf());
		this.container = container;
		currentServer = container.getSelf();
		// Create the remote interface reference
		remoteInterface = new RemoteAgentImpl(container);
		localResultList = new ArrayList<>();
		utils = new Utilities();
		// Start the agent in a new thread
		Thread agentThread = new Thread(this);
		agentThread.setName("Agent-Thread");
		agentThread.start();
	}

	@Override
	public RemoteAgent getRemoteInterface() {
		return remoteInterface;
	}
	
	@Override
	public List<Result> getResultSet() {
		return resultList;
	}
	
	@Override
	public List<VisitedServer> getVisitedServers() {
		List<VisitedServer> result = visitedServers.parallelStream()
				.map(p -> new VisitedServer(p.getAddress().toString(), p.getServicePort()))
				.collect(Collectors.toList());
		
		return result;
	}

	/**
	 * Create a list with all the files in the search path, recursively...
	 * @param directory Search path of the agent server
	 * @return A list with all files in the search directory
	 */
	private List<Path> listFiles(Path directory) {
		List<Path> files = new ArrayList<>();

		try (DirectoryStream<Path> stream = Files.newDirectoryStream(directory)) {
			for (Path file : stream) {
				// That's nasty...
				if (Files.isDirectory(file)) {
					files.addAll(listFiles(file));
				} else {
					if (checkType(file)) {
						files.add(file);	
					}
				}
			}
		} catch (IOException ex) {
			ex.printStackTrace();
		}

		return files;
	}

	/**
	 * Check whether a file matches the type pattern. Currently only
	 * pdf's are accepted
	 * @param file A file in the search directory
	 */
	private Boolean checkType(Path file) {
		Matcher matcher = pattern.matcher(file.toAbsolutePath().toString());
		if (matcher.matches()) {
			return true;
		}
		
		return false;
	}
	
	/**
	 * Calculate the next agent server to migrate. If there are no more
	 * unvisited server, throw an exception
	 * @return Next agent server
	 * @throws PeerNotFound
	 */
	private PeerAgent nextServer() throws PeerNotFound {
		List<PeerAgent> localView = container.getLocalView();
		Optional<PeerAgent> maybe = localView.parallelStream()
				.filter(p -> !visitedServers.contains(p)).findAny();

		if (maybe.isPresent()) {
			return maybe.get();
		}

		throw new PeerNotFound("No more unvisited servers");
	}

	/**
	 * Filter files in the search path according to the given query
	 * @param files List of file in search directory
	 * @return List of files that their metadata comply with the user query
	 */
	private List<Result> filterFiles(List<Path> files) {

		List<Result> filtered = files.parallelStream().filter(f -> checkQuery(f))
				.map(f -> new Result(new VisitedServer(
						currentServer.getAddress().toString(),
						currentServer.getServicePort()), f.toAbsolutePath().toString()))
				.collect(Collectors.toList());

		return filtered;
	}

	/**
	 * Wrapper method to check whether a file's metadata complies with the query
	 * @param A file in the search directory
	 */
	private Boolean checkQuery(Path file) {

		// Tika is not thread safe so I have to create separate references
		// for each file
		// For the time being this is the 'best' solution

		Metadata metadata = new Metadata();
		AutoDetectParser parser = new AutoDetectParser();
		BodyContentHandler handler = new BodyContentHandler();

		// All query properties should be met
		try (InputStream in = new FileInputStream(file.toFile())) {
			System.out.println("Parsing file: " + file.toAbsolutePath());

			parser.parse(in, handler, metadata);

			if (checkTitle(metadata) && checkKeywords(metadata)
					&& checkAuthor(metadata) && checkDate(metadata)) {
				return true;
			}

		} catch (IOException | SAXException ex) {
			ex.printStackTrace();
		} catch (TikaException ex) {
			System.err.println("Document: " + file.toAbsolutePath() + 
					" could not be parsed");
		} catch (ParseException ex) {
			System.err.println("Could not parse meta:creation-date property of file " +
					file.toAbsolutePath());
		}

		return false;
	}

	/**
	 * Check title metadata of a file
	 * @param metadata Metadata variable 
	 */
	private Boolean checkTitle(Metadata metadata) {
		// Ignore title
		if (query.getTitle().getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return true;
		}
		
		if (metadata.get("title").toLowerCase().contains(
				query.getTitle().getParameter().toLowerCase())) {
			return true;
		}

		return false;
	}

	/**
	 * Check author metadata of a file
	 * @param metadata Metadata variable 
	 */
	private Boolean checkAuthor(Metadata metadata) {
		// Ignore author
		if (query.getAuthor().getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return true;
		}
		
		if(metadata.get("Author").toLowerCase().contains(
				query.getAuthor().getParameter().toLowerCase())) {
			return true;
		}
		
		return false;
	}
	

	/**
	 * Check keywords metadata of a file. A user can specify in the Query
	 * how many keywords should be present for the query to be successful
	 * @param metadata Metadata variable 
	 */
	private Boolean checkKeywords(Metadata metadata) {
		Integer successThreshold = query.getKeywords().getSuccessThreshold();
		Integer matches = 0;
		// Ignore keywords
		if (query.getKeywords().getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return true;
		}
		
		// Check keywords. Consider query success threshold for a query to be
		// successful
		List<String> queryKeywords = query.getKeywords().getParameter();
		for (String keyword : queryKeywords) {
			if (metadata.get("Keywords").toLowerCase().contains(keyword.toLowerCase())) {
				matches++;
			}
			
			if (matches.equals(successThreshold)) {
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Check date metadata of a file. Consider also the operation in the query
	 * if the date should be BEFORE, EQUAL or AFTER the given one
	 * @param metadata Metadata variable 
	 */
	private Boolean checkDate(Metadata metadata) throws ParseException {
		// Ignore date
		if (query.getDate().getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return true;
		}
		Date parsedDate = utils.parseDate(metadata.get("meta:creation-date"));
		
		if (DateOperators.AFTER.equals(query.getDate().getOperator())) {
			if (parsedDate.after(query.getDate().getParameter())) {
				return true;
			}
		} else if (DateOperators.EQUALS.equals(query.getDate().getOperator())) {
			if (parsedDate.equals(query.getDate().getParameter())) {
				return true;
			}
		} else if (DateOperators.BEFORE.equals(query.getDate().getOperator())) {
			if (parsedDate.before(query.getDate().getParameter())) {
				return true;
			}
		}
		
		return false;
	}
}
