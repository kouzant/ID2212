package gr.kzps.id2212.project.agentserver;

import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.ParseException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerExecEnv {
	private static final Logger LOG = LogManager.getLogger(ServerExecEnv.class);
	private static String serverId;
	private static Integer serverPort;
	private static Integer basePort;
	
	public static void main(String[] args) {
		
		ArgumentsParser argsParser = new ArgumentsParser(args);
		
		try {
			CommandLine cmd = argsParser.parseArgs();
			
			serverId = cmd.getOptionValue("id");
			
			if (cmd.hasOption("serverPort")) {
				serverPort = Integer.parseInt(cmd.getOptionValue("serverPort"));
			} else {
				serverPort = 8080;
			}
			
			if (cmd.hasOption("basePort")) {
				basePort = Integer.parseInt(cmd.getOptionValue("basePort"));
			} else {
				basePort = 9090;
			}
			
			LOG.info("Starting server {} at server port: {} and base port: {}",
					new Object[]{serverId, serverPort, basePort});
			
		} catch (ParseException ex) {
			LOG.error(ex.getMessage());
		}
	}

}
