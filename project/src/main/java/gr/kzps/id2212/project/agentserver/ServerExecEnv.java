package gr.kzps.id2212.project.agentserver;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.logging.log4j.core.lookup.MainMapLookup;

import gr.kzps.id2212.project.agentserver.overlay.Discovery;
import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.BootstrapPeer;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;

public class ServerExecEnv {
	private final Logger LOG = LogManager.getLogger(ServerExecEnv.class);
	private String serverId;
	private String searchPath;
	private Integer agentPort;
	private Integer basePort;
	private Discovery discoveryService;
	private PeerAgent local;
	private TcpServer baseServer;
	private TcpServer agentServer;
	
	public ServerExecEnv(String[] args) {
		ArgumentsParser argsParser = new ArgumentsParser(args);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		try {
			CommandLine cmd = argsParser.parseArgs();
			
			serverId = cmd.getOptionValue("id");
			searchPath = cmd.getOptionValue("searchPath");
			Cache.getInstance().setSearchPath(searchPath);
			
			// Add server id to log4j lookup map
			MainMapLookup.setMainArguments(new String[]{"id", serverId});
			
			if (cmd.hasOption("agentPort")) {
				agentPort = Integer.parseInt(cmd.getOptionValue("agentPort"));
			} else {
				agentPort = 8080;
			}
			
			if (cmd.hasOption("basePort")) {
				basePort = Integer.parseInt(cmd.getOptionValue("basePort"));
			} else {
				basePort = 9090;
			}
			
			LOG.info("Starting server {} at agent port: {} and base port: {}",
					new Object[]{serverId, agentPort, basePort});
			
			Cache.getInstance().setAgentPort(agentPort);
			
			try {
				local = new PeerAgent(InetAddress.getLocalHost(), basePort, agentPort);
				
				// Sample size
				PeerStorage peerStorage = new PeerStorage(local, 4);
				
				agentServer = new AgentServer(agentPort, peerStorage);
				baseServer = new BaseServer(basePort, peerStorage);
				threadPool.execute(agentServer);
				threadPool.execute(baseServer);
				
				discoveryService = new Discovery(local, peerStorage);
				
				if (cmd.hasOption("bootstrap")) {
					String bootstrap = cmd.getOptionValue("bootstrap");
					Integer bootstrapPort = 9090;
					if (cmd.hasOption("bootstrapPort")) {
						bootstrapPort = Integer.parseInt(cmd.getOptionValue("bootstrapPort"));
					}
					
					discoveryService.connectBootstrap(InetAddress.getByName(bootstrap),
							bootstrapPort);
				}
			} catch (UnknownHostException ex) {
				LOG.fatal(ex.getMessage());
			}
			
			threadPool.execute(discoveryService);
		} catch (ParseException ex) {
			LOG.error(ex.getMessage());
		}
	}
	
	public static void main(String[] args) {
		
		new ServerExecEnv(args);
	}

}
