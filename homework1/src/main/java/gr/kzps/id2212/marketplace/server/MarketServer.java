package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface MarketServer extends Remote {
	public byte register(Client client, Callbacks callbacks) throws RemoteException;
}
