package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.exceptions.BankBalance;
import gr.kzps.id2212.marketplace.server.exceptions.ItemDoesNotExists;
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
import java.util.Optional;
import java.util.concurrent.locks.ReentrantLock;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import se.kth.id2212.ex2.bankrmi.Account;
import se.kth.id2212.ex2.bankrmi.Bank;
import se.kth.id2212.ex2.bankrmi.RejectedException;

public class MarketServerImpl extends UnicastRemoteObject implements
		MarketServer {

	private static final long serialVersionUID = -7510739400260172856L;

	// Locks for different stores
	private final ReentrantLock usersLock = new ReentrantLock();
	private final ReentrantLock itemsLock = new ReentrantLock();
	private final ReentrantLock wishesLock = new ReentrantLock();

	private static final Logger LOG = LogManager
			.getLogger(MarketServerImpl.class);

	private final String marketName;
	// Users of the marketplace
	private Map<String, MarketUsers> marketUsers;
	// Available sell orders
	private List<ExtendedItem> sellingItems;
	// Unfulfilled wishes
	private List<ExtendedItem> wishes;
	private Bank bank;

	public MarketServerImpl(String marketName, Bank bank) throws RemoteException {
		super();
		this.marketName = marketName;
		marketUsers = new HashMap<>();
		sellingItems = new ArrayList<>();
		wishes = new ArrayList<>();
		this.bank = bank;
	}

	@Override
	public void unregister(String email) throws RemoteException,
			NoUserException {
		LOG.debug("Unregistering user: {}", email);

		usersLock.lock();
		MarketUsers user = marketUsers.remove(email);
		usersLock.unlock();

		if (user == null) {
			LOG.debug("User trying to unregister does not exist");
			throw new NoUserException("User does not exist");
		} else {

			// Remove selling orders from that user
			List<ExtendedItem> foundItems = sellingItems.stream()
					.filter(I -> I.getOwner().getClient().getEmail().equals(email))
					.collect(Collectors.toList());
			
			itemsLock.lock();
			sellingItems.removeAll(foundItems);
			itemsLock.unlock();
			foundItems = null;

			// Remove wishes from that user
			List<ExtendedItem> foundWishes = wishes.stream()
					.filter(I -> I.getOwner().getClient().getEmail().equals(email))
					.collect(Collectors.toList());
			
			wishesLock.lock();
			wishes.removeAll(foundWishes);
			wishesLock.unlock();
			
			foundWishes = null;
		}
	}

	@Override
	public void register(Client client, Callbacks callbacks)
			throws RemoteException, NoBankAccountException, UserAlreadyExists {
		// Check if client has account in the bank
		Account bankAccount = bank.getAccount(client.getName());

		if (bankAccount == null) {
			// User does not have an account
			// Respond back
			LOG.warn("User trying to register does not have an account");
			throw new NoBankAccountException(
					"User does NOT have a bank account");

		} else {
			// Put client in the list
			if (marketUsers.containsKey(client.getEmail())) {
				throw new UserAlreadyExists(
						"A user with the same email already exists");
			} else {
				usersLock.lock();
				marketUsers.put(client.getEmail(), new MarketUsers(client,
						callbacks));
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
			// Iterate the wishes to see if any of them in fulfilled now
			checkWishes();
		}
	}

	@Override
	public void buy(String buyersEmail, String itemName)
			throws RemoteException, ItemDoesNotExists, NoUserException,
			BankBalance {
		
		// Check if item exists
		Optional<ExtendedItem> itemOpt = sellingItems.stream()
				.filter(I -> I.getName().equals(itemName))
				.findFirst();
		
		if (!itemOpt.isPresent()) {
			throw new ItemDoesNotExists(
					"The item you are trying to buy does not exist");
		} else {
			ExtendedItem item = itemOpt.get();
			// Check user exists
			// Find user in local db
			MarketUsers buyer = marketUsers.get(buyersEmail);
			if (buyer == null) {
				throw new NoUserException(
						"You are not registered in the marketplace");
			} else {
				// Check user balance in bank
				Account buyerBankAcc = bank.getAccount(buyer.getClient()
						.getName());

				try {
					// Update buyer balance
					buyerBankAcc.withdraw(item.getPrice());
					// Update seller balance
					Account sellerBankAcc = bank.getAccount(item.getOwner()
							.getClient().getName());
					sellerBankAcc.deposit(item.getPrice());
					// Remove from sellingItems
					itemsLock.lock();
					sellingItems.remove(item);
					itemsLock.unlock();
					// Notify seller
					item.getOwner()
							.getCallbacks()
							.itemBought(item.getName(),
									buyer.getClient().getName());
				} catch (RejectedException ex) {
					throw new BankBalance(ex.getMessage());
				}
			}

		}

	}

	@Override
	public void wish(String userEmail, String itemName, float price)
			throws RemoteException, NoUserException {

		LOG.debug("Make a wish for item {} in price {}", new Object[] {
				itemName, price });

		MarketUsers user = marketUsers.get(userEmail);

		if (user == null) {
			throw new NoUserException(
					"You are not registered to the marketplace");
		} else {
			wishesLock.lock();
			wishes.add(new ExtendedItem(itemName, price, user));
			wishesLock.unlock();
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

	private void checkWishes() {
		LOG.debug("Checking wishes");
		
		List<ExtendedItem> fulfilledWishes = new ArrayList<>();
		
		for (ExtendedItem wish : wishes) {
			
			// Take the first item that matches the criteria
			Optional<ExtendedItem> fitItemOpt = sellingItems
					.stream()
					.filter(I -> I.getName().equals(wish.getName())
							&& I.getPrice() <= wish.getPrice()).findFirst();

			if (fitItemOpt.isPresent()) {
				LOG.debug(
						"Found fit for wish item: {} with price {} for user: {}",
						new Object[] { wish.getName(), wish.getPrice(),
								wish.getOwner() });
				
				ExtendedItem fitItem = fitItemOpt.get();
				fulfilledWishes.add(wish);
				// Notify user
				try {
					wish.getOwner().getCallbacks().wishFulfilled(fitItem.getName(), fitItem.getPrice());
				} catch (RemoteException ex) {
					ex.printStackTrace();
				}
			}
		}
		
		// Remove fulfilled wishes
		wishesLock.lock();
		wishes.removeAll(fulfilledWishes);
		wishesLock.unlock();
		fulfilledWishes = null;
	}
}
