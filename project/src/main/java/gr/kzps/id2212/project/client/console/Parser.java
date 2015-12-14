package gr.kzps.id2212.project.client.console;

import java.util.StringTokenizer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.commands.Command;
import gr.kzps.id2212.project.client.commands.Commands;
import gr.kzps.id2212.project.client.commands.CreateAgent;
import gr.kzps.id2212.project.client.exceptions.NotEnoughArguments;
import gr.kzps.id2212.project.client.exceptions.UnknownCommand;

public class Parser {
	private final Logger LOG = LogManager.getLogger(Parser.class);
	private StringTokenizer tokens;
	private Commands command;
	private String commandStr;
	
	public Parser() {
		
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
				throw new NotEnoughArguments();
			}
			String queryClass = tokens.nextToken();
			String targetIp = tokens.nextToken();
			Integer targetPort = Integer.parseInt(tokens.nextToken());
			
			return new CreateAgent(queryClass, targetIp, targetPort);
		}
		
		return null;
	}
}
