package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.general.ReturnCodes;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.HashMap;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;

public class MarketServerImpl extends UnicastRemoteObject implements
		MarketServer {

	private static final long serialVersionUID = -7510739400260172856L;
	private static final Logger LOG = LogManager
			.getLogger(MarketServerImpl.class);

	private final Integer RMI_PORT = 1099;
	private Map<String, MarketUsers> marketUsers;
	private Bank bank;

	public MarketServerImpl() throws RemoteException {
		super();
		marketUsers = new HashMap<>();

		try {
			try {
				LocateRegistry.getRegistry(1099).list();
			} catch (RemoteException ex) {
				LOG.error("Could not locate registry");
			}

			bank = (Bank) Naming.lookup("Nordea");
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	@Override
	public byte register(Client client, Callbacks callbacks) throws RemoteException {
		// Check if client has account in the bank
		Account bankAccount = bank.getAccount(client.getName());
		
		if (bankAccount == null) {
			// User does not have an account
			// Respond back
			LOG.warn("User trying to register does not have an account");
			callbacks.itemBought();
			
			return ReturnCodes.NO_ACCOUNT;
		} else {
			// Put client in the list
			marketUsers.put(client.getEmail(), new MarketUsers(client, callbacks));
			
			return ReturnCodes.REGISTERED;
		}
		
	}

}
