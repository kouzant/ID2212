package gr.kzps.id2212.project.client.console;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.AgentDB;
import gr.kzps.id2212.project.client.AgentServer;
import gr.kzps.id2212.project.client.commands.Command;
import gr.kzps.id2212.project.client.exceptions.NotEnoughArguments;
import gr.kzps.id2212.project.client.exceptions.UnknownCommand;

public class ClientConsole {
	private AgentDB db;
	private Boolean running;
	private BufferedReader reader;
	private String input;
	private final Parser parser;
	
	public ClientConsole(AgentDB db, AgentServer server) {
		running = true;
		this.db = db;
		parser = new Parser(this, server);
		reader = new BufferedReader(new InputStreamReader(System.in));
	}
	
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
	
	public void print(String msg) {
		System.out.println("> " + msg);
	}
	
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
