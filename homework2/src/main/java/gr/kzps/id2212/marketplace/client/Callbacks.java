package gr.kzps.id2212.marketplace.client;

import java.rmi.Remote;
import java.rmi.RemoteException;

public interface Callbacks extends Remote {
	// Notify when our own item is bought
	public void itemBought(String itemName, String buyerName) throws RemoteException;
	// Notify when a wish is fulfilled
	public void wishFulfilled(String itemName, float price) throws RemoteException;
}
