package gr.kzps.id2212.project.client.console;

import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.AgentServer;
import gr.kzps.id2212.project.client.commands.Command;
import gr.kzps.id2212.project.client.commands.CommandAbstr;
import gr.kzps.id2212.project.client.commands.Commands;
import gr.kzps.id2212.project.client.commands.CreateAgent;
import gr.kzps.id2212.project.client.commands.DeleteAgent;
import gr.kzps.id2212.project.client.commands.Exit;
import gr.kzps.id2212.project.client.commands.PurgeAgent;
import gr.kzps.id2212.project.client.commands.Status;
import gr.kzps.id2212.project.client.exceptions.NotEnoughArguments;
import gr.kzps.id2212.project.client.exceptions.UnknownCommand;

public class Parser {
	private final Logger LOG = LogManager.getLogger(Parser.class);
	private final ClientConsole console;
	private final AgentServer server;
	private StringTokenizer tokens;
	private Commands command;
	private String commandStr;
	private CommandAbstr execCommand;
	
	public Parser(ClientConsole console, AgentServer server) {
		this.console = console;
		this.server = server;
	}
	
	public Command parse(String rawCommand) throws UnknownCommand, NotEnoughArguments {
		if (rawCommand == null) {
			LOG.debug("rawCommand is null");
			throw new UnknownCommand();
		}
		
		tokens = new StringTokenizer(rawCommand);
		
		if (!tokens.hasMoreTokens()) {
			LOG.debug("rawCommand has no tokens");
			throw new UnknownCommand();
		}
		
		commandStr = tokens.nextToken();
		
		try {
			command = Commands.getCommand(commandStr);
		} catch (IllegalArgumentException ex) {
			LOG.debug("Command is not member of Commands enum");
			throw new UnknownCommand();
		}
		
		if (Commands.create.equals(command)) {
			if (tokens.countTokens() != 3) {
				throw new NotEnoughArguments("Usage: create QUERY_PLAN_CLASS TARGET_IP TARGET_PORT");
			}
			String queryClass = tokens.nextToken();
			String targetIp = tokens.nextToken();
			Integer targetBasePort = Integer.parseInt(tokens.nextToken());
			
			execCommand = new CreateAgent(queryClass, server, targetIp, targetBasePort);
		} else if (Commands.status.equals(command)) {
			execCommand = new Status();
		} else if (Commands.delete.equals(command)) {
			if (tokens.countTokens() != 1) {
				throw new NotEnoughArguments("Usage: delete AGENT_ID");
			}
			
			String agentId = tokens.nextToken();
			execCommand = new DeleteAgent(agentId);
		} else if (Commands.purge.equals(command)) {
			if (tokens.countTokens() != 1) {
				throw new NotEnoughArguments("Usage: purge AGENT_ID");
			}
			
			String agentId = tokens.nextToken();
			execCommand = new PurgeAgent(agentId);
		} else if (Commands.exit.equals(command)) {
			execCommand = new Exit();
		} else {
			throw new UnknownCommand();
		}
		
		execCommand.setConsole(console);
		return execCommand;
	}
}
