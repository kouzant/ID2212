package gr.kzps.id2212.project.client.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.commands.Command;
import gr.kzps.id2212.project.client.exceptions.NotEnoughArguments;
import gr.kzps.id2212.project.client.exceptions.UnknownCommand;

public class ClientConsole {
	private final Logger LOG = LogManager.getLogger(ClientConsole.class);
	private AgentDB db;
	private Boolean running;
	private BufferedReader reader;
	private String input;
	private final Parser parser;
	
	public ClientConsole(AgentDB db) {
		running = true;
		this.db = db;
		parser = new Parser(this);
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
	public void console() {
		LOG.debug("Client console");
		System.out.println("++ Hi my name is Moneypenny ++");
		
		while(isRunning()) {
			try {
				input = reader.readLine();
				LOG.debug("Raw user input: {}", input);
				Command command = parser.parse(input);
				command.execute(db);
				
			} catch (UnknownCommand | NotEnoughArguments ex) {
				System.out.println("> " + ex.getMessage());
				System.out.print("> ");
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}
	
	public void print(String msg) {
		System.out.println("> " + msg);
		System.out.print("> ");
	}
	
	private Boolean isRunning() {
		return running;
	}
	
	public void halt() {
		running = false;
	}
}
