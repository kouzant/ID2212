package gr.kzps.id2212.project.client;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.console.ClientConsole;

public class ClientExecEnv {
	private static final Logger LOG = LogManager.getLogger(ClientExecEnv.class);
	
	public static void main(String[] args) {
		LOG.debug("Client started");
		AgentDB db = new AgentDB();
		
		new ClientConsole(db).console();
	}

}
