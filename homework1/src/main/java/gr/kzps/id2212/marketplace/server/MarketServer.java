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
	public void register(Client client, Callbacks callbacks)
			throws RemoteException, NoBankAccountException, UserAlreadyExists;
	public void unregister(String email) throws RemoteException, NoUserException;
	public void sell(String email, String itemName, float price)
			throws RemoteException, NoUserException;
	public List<BaseItem> listItems() throws RemoteException;
	public void buy(String buyersEmail, String itemName) throws RemoteException,
		ItemDoesNotExists, NoUserException, BankBalance;
	public void wish(String userEmail, String itemName, float price)
		throws RemoteException, NoUserException;
	public String getName() throws RemoteException;
}
