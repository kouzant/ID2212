package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;
import gr.kzps.id2212.marketplace.server.exceptions.UserAlreadyExists;



import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

import java.util.stream.Collector;

import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;



import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;

public class MarketServerImpl extends UnicastRemoteObject implements
		MarketServer {

	private static final long serialVersionUID = -7510739400260172856L;
	
	private final ReentrantLock usersLock = new ReentrantLock();
	private final ReentrantLock itemsLock = new ReentrantLock();
	
	private static final Logger LOG = LogManager
			.getLogger(MarketServerImpl.class);

	private final Integer RMI_PORT = 1099;
	private final String marketName;
	private Map<String, MarketUsers> marketUsers;
	private List<ExtendedItem> sellingItems;
	private Bank bank;

	public MarketServerImpl(String marketName) throws RemoteException {
		super();
		this.marketName = marketName;
		marketUsers = new HashMap<>();
		sellingItems = new ArrayList<>();

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
	public void unregister(String email) throws RemoteException, NoUserException {
		LOG.debug("Unregistering user");
		
		usersLock.lock();
		MarketUsers user = marketUsers.remove(email);
		usersLock.unlock();
		
		if (user == null) {
			LOG.debug("User trying to unregister does not exist");
			throw new NoUserException("User does not exist");
		} else {
			
			// Remove selling orders from that user
			List<ExtendedItem> foundItems = new ArrayList<ExtendedItem>();
			
			for (ExtendedItem sellingItem: sellingItems) {
				if (sellingItem.getOwner().getClient().getEmail().equals(email)) {
					foundItems.add(sellingItem);
				}
			}
			
			itemsLock.lock();
			sellingItems.removeAll(foundItems);
			itemsLock.unlock();
			foundItems = null;
			
			// TODO Probably I will have to remove his/her wishes later
		}
	}
	
	@Override
	public void register(Client client, Callbacks callbacks)
			throws RemoteException, NoBankAccountException,
			UserAlreadyExists {
		// Check if client has account in the bank
		Account bankAccount = bank.getAccount(client.getName());
		
		if (bankAccount == null) {
			// User does not have an account
			// Respond back
			LOG.warn("User trying to register does not have an account");
			throw new NoBankAccountException("User does NOT have a bank account");
			
		} else {
			// Put client in the list
			if (marketUsers.containsKey(client.getEmail())) {
				throw new UserAlreadyExists("A user with the same email already exists");
			} else {
				usersLock.lock();
				marketUsers.put(client.getEmail(), new MarketUsers(client, callbacks));
				usersLock.unlock();
				LOG.info("Registered user: {}", client.getName());
			}
		}
		
	}
	
	@Override
	public void sell(String email, String itemName, float price)
			throws RemoteException, NoUserException {
		MarketUsers user = marketUsers.get(email);
		
		if (user == null) {
			LOG.debug("User trying to sell does NOT exist");
			throw new NoUserException("User trying to sell does NOT exist");
		} else {
			ExtendedItem newItem = new ExtendedItem(itemName, price, user);
			itemsLock.lock();
			sellingItems.add(newItem);
			itemsLock.unlock();
			LOG.debug("Selling order stored");
			LOG.debug(newItem.toString());
		}
	}
	
	@Override
	public List<BaseItem> listItems() throws RemoteException {
		List<BaseItem> ret = sellingItems.stream()
			.map(I -> new BaseItem(I.getName(), I.getPrice()))
			.collect(Collectors.toList());
		
		return ret;
	}
	
	@Override
	public String getName() {
		return marketName;
	}

}
