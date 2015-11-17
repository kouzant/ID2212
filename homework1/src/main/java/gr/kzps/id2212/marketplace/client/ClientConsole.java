package gr.kzps.id2212.marketplace.client;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.rmi.RemoteException;
import java.util.StringTokenizer;

import gr.kzps.id2212.marketplace.client.Commands.BankNewAccount;
import gr.kzps.id2212.marketplace.client.Commands.BaseCommand;
import gr.kzps.id2212.marketplace.client.Commands.Commands;
import gr.kzps.id2212.marketplace.client.Commands.Exit;
import gr.kzps.id2212.marketplace.client.Commands.Help;
import gr.kzps.id2212.marketplace.client.Commands.NotEnoughParams;
import gr.kzps.id2212.marketplace.client.Commands.RegisterMarketplace;
import gr.kzps.id2212.marketplace.client.Commands.UnknownCommand;
import gr.kzps.id2212.marketplace.server.MarketServer;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;
import se.kth.id2212.ex2.bankrmi.RejectedException;

public class ClientConsole {
	private static final Logger LOG = LogManager.getLogger(ClientConsole.class);

	private boolean running;
	private final Bank bank;
	private final MarketServer market;

	public ClientConsole(Bank bank, MarketServer market) {
		this.bank = bank;
		this.market = market;

		running = true;
	}

	public void console() throws RemoteException {
		BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
		System.out.println("Welcome to " + market.getName());

		while (isRunning()) {
			try {
				String input = in.readLine();
				LOG.debug("Raw user input: {}", input);
				BaseCommand command = parse(input);

				execute(command);

			} catch (IOException ex) {
				ex.printStackTrace();
			}
		}
	}

	private void execute(BaseCommand command) {
		if (command instanceof UnknownCommand) {
			System.out.println("> Unknown command, try help");
		} else if (command instanceof NotEnoughParams) {
			System.out.println("> Wrong parameters, try help");
		} else if (command instanceof BankNewAccount) {
			LOG.debug("Create new bank acount");
			try {
				Account account = bank.newAccount(command.getClient().getName());
				account.deposit(((BankNewAccount) command).getInitialBalance());
				System.out.println("> Account created\n");
			} catch (RemoteException ex) {
				LOG.error("RMI error");
				ex.printStackTrace();
			} catch (RejectedException ex) {
				LOG.info("Could not create new account");
				System.out.println("> Could not create account");
			}
		} else if (command instanceof RegisterMarketplace) {
			LOG.debug("Register user to marketplace");
			try {
				market.register(command.getClient(), ((RegisterMarketplace) command).getCallbacks());
			} catch (RemoteException ex) {
				LOG.error("RMI error");
				ex.printStackTrace();
			} catch (NoBankAccountException ex) {
				LOG.debug("User does not have a bank account");
				System.out.println("> You do NOT have a bank account");
			}
		} else if (command instanceof Help) {
			System.out.println("> Help menu");
			for (Commands com: Commands.values()) {
				System.out.println(com);
			}
		} else if (command instanceof Exit) {
			System.out.println("> Bye");
			exit();
		}
	}
	
	private BaseCommand parse(String input) {
		if (input == null) {
			return new UnknownCommand(new Client(null, null));
		}

		StringTokenizer inputTokens = new StringTokenizer(input);
		Commands command = null;

		String commandStr = inputTokens.nextToken();

		try {
			command = Commands.getCommand(commandStr);
		} catch (IllegalArgumentException ex) {
			LOG.debug("Unknown command");
			return new UnknownCommand(null);
		}

		if (command.equals(Commands.newaccount)) {
			if (inputTokens.countTokens() != 3) {
				// System.out.println("> Not enough parameters");
				
				return new NotEnoughParams(null);
			} else {
				String name = inputTokens.nextToken();
				String email = inputTokens.nextToken();
				String initialBalance = inputTokens.nextToken();

				return new BankNewAccount(new Client(name, email),
						Float.parseFloat(initialBalance));
			}
		} else if (command.equals(Commands.register)) {
			if (inputTokens.countTokens() != 2) {
				
				return new NotEnoughParams(null);
			} else {
				String name = inputTokens.nextToken();
				String email = inputTokens.nextToken();
				Callbacks callbacks = null;
				
				try {
					callbacks = new CallbacksImpl();
				} catch (RemoteException ex) {
					LOG.error("Error while creating user callback method");
					ex.printStackTrace();
				}
				
				return new RegisterMarketplace(new Client(name, email), callbacks);
			}
		} else if (command.equals(Commands.exit)) {
			
			return new Exit(null);
		} else if (command.equals(Commands.help)) {
			
			return new Help(null);
		} else {
			
			return new UnknownCommand(null);
		}
	}

	private boolean isRunning() {
		return running;
	}

	private void exit() {
		running = false;
	}
}
