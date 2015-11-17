package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;
import gr.kzps.id2212.marketplace.server.exceptions.NoBankAccountException;
import gr.kzps.id2212.marketplace.server.exceptions.NoUserException;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MarketServer extends Remote {
	public void register(Client client, Callbacks callbacks) throws RemoteException, NoBankAccountException;
	public void unregister(String email) throws RemoteException, NoUserException;
	public String getName() throws RemoteException;
}