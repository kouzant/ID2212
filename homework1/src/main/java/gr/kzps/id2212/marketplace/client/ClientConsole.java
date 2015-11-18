package gr.kzps.id2212.marketplace.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;
import java.util.List;
import java.util.StringTokenizer;

import gr.kzps.id2212.marketplace.client.Commands.BankNewAccount;
import gr.kzps.id2212.marketplace.client.Commands.BaseCommand;
import gr.kzps.id2212.marketplace.client.Commands.BuyCommand;
import gr.kzps.id2212.marketplace.client.Commands.Commands;
import gr.kzps.id2212.marketplace.client.Commands.Exit;
import gr.kzps.id2212.marketplace.client.Commands.Help;
import gr.kzps.id2212.marketplace.client.Commands.ListCommand;
import gr.kzps.id2212.marketplace.client.Commands.NotEnoughParams;
import gr.kzps.id2212.marketplace.client.Commands.RegisterMarketplace;
import gr.kzps.id2212.marketplace.client.Commands.SellCommand;
import gr.kzps.id2212.marketplace.client.Commands.UnknownCommand;
import gr.kzps.id2212.marketplace.client.Commands.UnregisterMarketplace;
import gr.kzps.id2212.marketplace.server.BaseItem;
import gr.kzps.id2212.marketplace.server.MarketServer;
import gr.kzps.id2212.marketplace.server.exceptions.BankBalance;
import gr.kzps.id2212.marketplace.server.exceptions.ItemDoesNotExists;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;
import gr.kzps.id2212.marketplace.server.exceptions.UserAlreadyExists;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;
import se.kth.id2212.ex2.bankrmi.RejectedException;

public class ClientConsole {
	private static final Logger LOG = LogManager.getLogger(ClientConsole.class);

	private boolean running;
	private MarketServer market;
	// private Callbacks callbacks;
	// private Client currentUser;
	private Parser parser;
	private Executor executor;

	public ClientConsole(Bank bank, MarketServer market) {
		// callbacks = null;
		// currentUser = null;
		this.market = market;
		running = true;
		parser = new Parser();
		executor = new Executor(bank, market);
	}

	public void console() throws RemoteException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to " + market.getName());

		while (isRunning()) {
			try {
				String input = in.readLine();
				LOG.debug("Raw user input: {}", input);
				BaseCommand command = parser.parse(input);

				Integer returnVal = executor.execute(command);
				
				if (returnVal == -1) {
					exit();
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
