package gr.kzps.id2212.marketplace.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;

import gr.kzps.id2212.marketplace.client.Commands.BaseCommand;
import gr.kzps.id2212.marketplace.client.Commands.BaseCommandImpl;
import gr.kzps.id2212.marketplace.client.exceptions.NotEnoughParams;
import gr.kzps.id2212.marketplace.client.exceptions.UnknownCommand;
import gr.kzps.id2212.marketplace.server.MarketServer;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Bank;

public class ClientConsole {
	private static final Logger LOG = LogManager.getLogger(ClientConsole.class);

	private boolean running;
	private MarketServer market;
	private Parser parser;
	private Executor executor;

	// Bank and Marketplace remote methods
	public ClientConsole(Bank bank, MarketServer market) {
		this.market = market;
		running = true;
		parser = new Parser();
		executor = new Executor(bank, market);
	}

	public void console() throws RemoteException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("> +++ Welcome to " + market.getName() + " +++");

		BaseCommand command = null;
		Integer returnVal;
		
		while (isRunning()) {
			try {
				String input = in.readLine();
				LOG.debug("Raw user input: {}", input);
				try {
					command = parser.parse(input);
					returnVal = executor.execute(command);
					
					if (returnVal == -1) {
						exit();
					}
				} catch (UnknownCommand ex) {
					System.out.println("> " + ex.getMessage());
				} catch (NotEnoughParams ex) {
					System.out.println("> " + ex.getMessage());
				}

			} catch (RemoteException ex) {
				LOG.error("RMI error");
				ex.printStackTrace();
			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private boolean isRunning() {
		return running;
	}

	private void exit() {
		running = false;
	}
}
