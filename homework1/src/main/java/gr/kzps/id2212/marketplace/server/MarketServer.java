package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.exceptions.BankBalance;
import gr.kzps.id2212.marketplace.server.exceptions.ItemDoesNotExists;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;
import gr.kzps.id2212.marketplace.server.exceptions.UserAlreadyExists;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MarketServer extends Remote {
	// Register user to marketplace
	public void register(Client client, Callbacks callbacks)
			throws RemoteException, NoBankAccountException, UserAlreadyExists;
	// Uregister user from marketplace
	public void unregister(String email) throws RemoteException, NoUserException;
	// User wants to sell an item
	public void sell(String email, String itemName, float price)
			throws RemoteException, NoUserException;
	// List all available items
	public List<BaseItem> listItems() throws RemoteException;
	// User wants to buy an item
	public void buy(String buyersEmail, String itemName) throws RemoteException,
		ItemDoesNotExists, NoUserException, BankBalance;
	// User placed a wish for an item
	public void wish(String userEmail, String itemName, float price)
		throws RemoteException, NoUserException;
	// Get the name of the marketplace
	public String getName() throws RemoteException;
}
