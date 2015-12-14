package gr.kzps.id2212.project.client.agent;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.InetAddress;
import java.nio.file.DirectoryStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

public class AgentImpl implements Agent, Runnable {

	private static final long serialVersionUID = 4772482720958169130L;

	private final InetAddress homeAddress;
	private final Integer homePort;
	private final UUID id;
	private final Query query;
	private final Pattern pattern;
	private List<PeerAgent> visitedServers;
	// TODO to be removed!!!
	private List<Result> resultList;

	private transient AgentRunningContainer container;
	private transient PeerAgent currentServer;
	private transient List<Result> localResultList;
	private transient Utilities utils;
	
	public AgentImpl(UUID id, InetAddress homeAddress, Integer homePort, Query query) {
		this.id = id;
		this.homeAddress = homeAddress;
		this.homePort = homePort;
		this.query = query;
		visitedServers = new ArrayList<>();
		resultList = new ArrayList<>();
		localResultList = new ArrayList<>();
		// Currently accept only pdf
		// "\\S+\\s*\\S*.((pdf)|(odt))$"
		pattern = Pattern.compile("\\S+\\s*\\S*.(pdf)$");
	}

	@Override
	public void run() {
		String agentName = Thread.currentThread().getName();
		System.out.println(agentName + " is doing something in " + currentServer);
		Path searchDir = Paths.get(Cache.getInstance().getSearchPath());
		localResultList = filterFiles(listFiles(searchDir));
		resultList.addAll(localResultList);
		localResultList.clear();

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
		utils = new Utilities();
		Thread agentThread = new Thread(this);
		agentThread.setName("Agent-Thread");
		agentThread.start();
	}

	@Override
	public String getResult() {
		StringBuilder sb = new StringBuilder();
		sb.append("Servers visited").append("\n");

		visitedServers.stream().forEach(s -> sb.append(s).append("\n"));

		sb.append("Files found").append("\n");
		resultList.stream().forEach(p -> sb.append(p).append("\n"));

		return sb.toString();
	}

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

	private Boolean checkType(Path file) {
		Matcher matcher = pattern.matcher(file.toAbsolutePath().toString());
		if (matcher.matches()) {
			return true;
		}
		
		return false;
	}
	
	private PeerAgent nextServer() throws PeerNotFound {
		List<PeerAgent> localView = container.getLocalView();
		Optional<PeerAgent> maybe = localView.parallelStream().filter(p -> !visitedServers.contains(p)).findAny();

		if (maybe.isPresent()) {
			return maybe.get();
		}

		throw new PeerNotFound("No more unvisited servers");
	}

	private List<Result> filterFiles(List<Path> files) {

		List<Result> filtered = files.parallelStream().filter(f -> checkQuery(f))
				.map(f -> new Result(currentServer, f.toAbsolutePath().toString()))
				.collect(Collectors.toList());

		return filtered;
	}

	private Boolean checkQuery(Path file) {

		Boolean check = false;

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
				check = true;
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

		return check;
	}

	// Check title metadata
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

	// Check author
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
	
	// Check keywords. All query keywords should exist
	private Boolean checkKeywords(Metadata metadata) {
		// TODO check the success threshold
		Integer successThreshold = query.getKeywords().getSuccessThreshold();
		// Ignore keywords
		if (query.getKeywords().getParameterSwitch().equals(ParameterSwitch.OFF)) {
			return true;
		}
		
		// Check keywords. All query keywords should exist
		List<String> queryKeywords = query.getKeywords().getParameter();
		for (String keyword : queryKeywords) {
			if (!metadata.get("Keywords").toLowerCase().contains(keyword.toLowerCase())) {
				return false;
			}
		}
		
		return true;
	}
	
	// Check date
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
