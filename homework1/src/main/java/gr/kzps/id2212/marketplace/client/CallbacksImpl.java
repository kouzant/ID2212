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
	public void itemBought() throws RemoteException {
		LOG.debug("Item placed in the marketplace is bought");
		
		System.out.println("Item bought...");
	}

}
