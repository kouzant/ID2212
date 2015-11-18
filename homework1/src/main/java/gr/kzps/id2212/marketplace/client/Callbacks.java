package gr.kzps.id2212.marketplace.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callbacks extends Remote {
	public void itemBought(String itemName, String buyerName) throws RemoteException;
	public void wishFulfilled(String itemName, float price) throws RemoteException;
}
