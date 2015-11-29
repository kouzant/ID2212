package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.exceptions.BankBalance;
import gr.kzps.id2212.marketplace.server.exceptions.DBConnectionException;
import gr.kzps.id2212.marketplace.server.exceptions.IncorrectPasswordException;
import gr.kzps.id2212.marketplace.server.exceptions.ItemDoesNotExists;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;
import gr.kzps.id2212.marketplace.server.exceptions.PasswordIsSmallException;
import gr.kzps.id2212.marketplace.server.exceptions.UserAlreadyExists;
import gr.kzps.id2212.marketplace.server.exceptions.UserNotRegistered;

import java.rmi.Remote;
import java.rmi.RemoteException;
import java.util.List;

public interface MarketServer extends Remote {
	// Register user to marketplace
	public void register(Client client)
			throws RemoteException, NoBankAccountException, PasswordIsSmallException, DBConnectionException, UserAlreadyExists;
	// Uregister user from marketplace
	public void unregister(String email, String password) throws RemoteException, NoUserException, IncorrectPasswordException, DBConnectionException;
        // User login
        public void login(Client client, Callbacks callbacks) throws RemoteException, DBConnectionException,  IncorrectPasswordException, UserNotRegistered;
        // User logout
        public void logout(Client client) throws RemoteException, NoUserException;    
        // User info
        public String info(String email) throws RemoteException, NoUserException, DBConnectionException;
	// User wants to sell an item
	public void sell(String email, String itemName, float price, int quantity)
			throws RemoteException, NoUserException, DBConnectionException;
	// List all available items
	public List<BaseItem> listItems() throws RemoteException, DBConnectionException;
	// User wants to buy an item
	public void buy(String buyersEmail, String itemName, int quantity) throws RemoteException,
		ItemDoesNotExists, NoUserException, BankBalance, DBConnectionException;
	// User placed a wish for an item
	public void wish(String userEmail, String itemName, float price)
		throws RemoteException, NoUserException, DBConnectionException;
	// Get the name of the marketplace
	public String getName() throws RemoteException;
}
