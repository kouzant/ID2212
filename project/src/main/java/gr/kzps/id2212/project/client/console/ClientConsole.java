package gr.kzps.id2212.project.client.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentServer;
import gr.kzps.id2212.project.client.commands.Command;
import gr.kzps.id2212.project.client.exceptions.NotEnoughArguments;
import gr.kzps.id2212.project.client.exceptions.UnknownCommand;

/**
 * Implement the console presented to the client
 * @author Antonis Kouzoupis
 *
 */
public class ClientConsole {
	private AgentDB db;
	private Boolean running;
	private BufferedReader reader;
	private String input;
	private final Parser parser;
	
	/**
	 * @param db The local agents store
	 * @param server Local client service
	 */
	public ClientConsole(AgentDB db, AgentServer server) {
		running = true;
		this.db = db;
		parser = new Parser(this, server);
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	/**
	 * REPL
	 */
	public void console() {
		System.out.println("++ Hi my name is Moneypenny ++");
		printPrompt();
		
		while(isRunning()) {
			try {
				input = reader.readLine();
				Command command = parser.parse(input);
				command.execute(db);
				
			} catch (UnknownCommand | NotEnoughArguments ex) {
				print(ex.getMessage());
				printPrompt();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	/**
	 * Print a message to user
	 * @param msg Message to be printed to the user
	 */
	public void print(String msg) {
		System.out.println("> " + msg);
	}
	
	/**
	 * Print the command line prompt
	 */
	public void printPrompt() {
		System.out.print("> ");
	}
	
	private Boolean isRunning() {
		return running;
	}
	
	public void halt() {
		running = false;
	}
}
