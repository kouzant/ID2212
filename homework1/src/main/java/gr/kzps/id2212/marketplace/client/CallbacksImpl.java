package gr.kzps.id2212.marketplace.client;

import java.rmi.RemoteException;
import java.rmi.server.UnicastRemoteObject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class CallbacksImpl extends UnicastRemoteObject implements Callbacks {

	private static final Logger LOG = LogManager.getLogger(Callbacks.class);

	protected CallbacksImpl() throws RemoteException {
		super();
	}

	private static final long serialVersionUID = 575383121843072296L;

	@Override
	public void itemBought(String itemName, String buyerName)
			throws RemoteException {
		LOG.debug("Item placed in the marketplace is bought");

		System.out.println("> Your item: " + itemName
				+ " has been bought from: " + buyerName);
	}

	@Override
	public void wishFulfilled(String itemName, float price)
			throws RemoteException {
		LOG.debug("Wish fulfilled");

		System.out.println("Available item fit your wish. Item: " + itemName
				+ " Price: " + price);
	}

}
