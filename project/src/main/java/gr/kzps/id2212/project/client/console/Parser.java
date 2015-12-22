package gr.kzps.id2212.project.client.console;

import java.util.StringTokenizer;

import gr.kzps.id2212.project.client.AgentServer;
import gr.kzps.id2212.project.client.commands.Cancel;
import gr.kzps.id2212.project.client.commands.Command;
import gr.kzps.id2212.project.client.commands.CommandAbstr;
import gr.kzps.id2212.project.client.commands.Commands;
import gr.kzps.id2212.project.client.commands.CreateAgent;
import gr.kzps.id2212.project.client.commands.DeleteAgent;
import gr.kzps.id2212.project.client.commands.Exit;
import gr.kzps.id2212.project.client.commands.Help;
import gr.kzps.id2212.project.client.commands.PurgeAgent;
import gr.kzps.id2212.project.client.commands.Status;
import gr.kzps.id2212.project.client.commands.WhereIs;
import gr.kzps.id2212.project.client.exceptions.NotEnoughArguments;
import gr.kzps.id2212.project.client.exceptions.UnknownCommand;

/**
 * Parse user input and return the corresponding commands
 * @author Antonis Kouzoupis
 *
 */
public class Parser {
	private final ClientConsole console;
	private final AgentServer server;
	private StringTokenizer tokens;
	private Commands command;
	private String commandStr;
	private CommandAbstr execCommand;
	
	/**
	 * @param console UI console
	 * @param server Client service reference
	 */
	public Parser(ClientConsole console, AgentServer server) {
		this.console = console;
		this.server = server;
	}
	
	/**
	 * Parse user input
	 * @param rawCommand String typed from the user
	 * @return Corresponding command
	 * @throws UnknownCommand
	 * @throws NotEnoughArguments
	 */
	public Command parse(String rawCommand) throws UnknownCommand, NotEnoughArguments {
		if (rawCommand == null) {
			throw new UnknownCommand();
		}
		
		// Tokenize with space char delimiter
		tokens = new StringTokenizer(rawCommand);
		
		if (!tokens.hasMoreTokens()) {
			throw new UnknownCommand();
		}
		
		commandStr = tokens.nextToken();
		
		// If command is not part of the Commands enum then is not recognized
		try {
			command = Commands.getCommand(commandStr);
		} catch (IllegalArgumentException ex) {
			throw new UnknownCommand();
		}
		
		if (Commands.create.equals(command)) {
			// Create new agent
			
			if (tokens.countTokens() != 3) {
				throw new NotEnoughArguments("Usage: create QUERY_PLAN_CLASS TARGET_IP TARGET_PORT");
			}
			String queryClass = tokens.nextToken();
			String targetIp = tokens.nextToken();
			Integer targetBasePort = Integer.parseInt(tokens.nextToken());
			
			execCommand = new CreateAgent(queryClass, server, targetIp, targetBasePort);
		} else if (Commands.status.equals(command)) {
			// Print status of all agents
			
			execCommand = new Status();
		} else if (Commands.delete.equals(command)) {
			// Delete an agent
			
			if (tokens.countTokens() != 1) {
				throw new NotEnoughArguments("Usage: delete AGENT_ID");
			}
			
			String agentId = tokens.nextToken();
			execCommand = new DeleteAgent(agentId);
		} else if (Commands.purge.equals(command)) {
			// Purge an agent
			
			if (tokens.countTokens() != 1) {
				throw new NotEnoughArguments("Usage: purge AGENT_ID");
			}
			
			String agentId = tokens.nextToken();
			execCommand = new PurgeAgent(agentId);
		} else if (Commands.help.equals(command)) {
			// Print help menu
			
			execCommand = new Help();
		} else if (Commands.whereis.equals(command)) {
			// Print the location of an agent
			
			if (tokens.countTokens() != 1) {
				throw new NotEnoughArguments("Usage: whereis AGENT_ID");
			}
			
			String agentId = tokens.nextToken();
			execCommand = new WhereIs(agentId);
		} else if (Commands.cancel.equals(command)) {
			// Cancel a running agent and return home
			
			if (tokens.countTokens() != 1) {
				throw new NotEnoughArguments("Usage: cancel AGENT_ID");
			}
			
			String agentId = tokens.nextToken();
			execCommand = new Cancel(agentId);
		} else if (Commands.exit.equals(command)) {
			// Exit client
			
			execCommand = new Exit();
		} else {
			throw new UnknownCommand();
		}
		
		execCommand.setConsole(console);
		return execCommand;
	}
}
