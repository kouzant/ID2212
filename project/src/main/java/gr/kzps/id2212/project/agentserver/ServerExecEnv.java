package gr.kzps.id2212.project.agentserver;

import java.net.UnknownHostException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.HelpFormatter;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.Discovery;

public class ServerExecEnv {
	private static final Logger LOG = LogManager.getLogger(ServerExecEnv.class);
	private static String serverId;
	private static Integer agentPort;
	private static Integer basePort;
	private static Discovery discoveryService;
	
	public static void main(String[] args) {
		
		ArgumentsParser argsParser = new ArgumentsParser(args);
		ExecutorService threadPool = Executors.newCachedThreadPool();
		
		try {
			CommandLine cmd = argsParser.parseArgs();
			
			serverId = cmd.getOptionValue("id");
			
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
			//TcpServer agentServer = new AgentServer(agentPort);
			TcpServer baseServer = new BaseServer(basePort);
			//threadPool.execute(agentServer);
			threadPool.execute(baseServer);
			
			try {
				discoveryService = new Discovery(basePort, 5);
				/*
				 * If I have to connect to a bootstrap server
				 * discoveryService.connectBootstrap
				 */
			} catch (UnknownHostException ex) {
				LOG.fatal(ex.getMessage());
			}
			
		} catch (ParseException ex) {
			LOG.error(ex.getMessage());
		}
	}

}
