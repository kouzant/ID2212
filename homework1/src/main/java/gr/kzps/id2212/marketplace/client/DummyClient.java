package gr.kzps.id2212.marketplace.client;

import gr.kzps.id2212.marketplace.general.ReturnCodes;
import gr.kzps.id2212.marketplace.server.MarketServer;

import java.rmi.Naming;
import java.rmi.NoSuchObjectException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;

public class DummyClient {

	public static void main(String[] args) {
		MarketServer market = null;
		byte res = (byte) 0xff;
		Callbacks callbacks = null;
		Client dummyClient = new Client("antonis", "antkou@kth.se");
		
		try {
			LocateRegistry.getRegistry().list();
			market = (MarketServer) Naming.lookup("mall");
			callbacks = new CallbacksImpl();
			
			res = market.register(dummyClient, callbacks);
			
		} catch (Exception ex) {
			ex.printStackTrace();
		} 
		
		if (res == ReturnCodes.NO_ACCOUNT) {
			System.out.println("Hhhmm I've got to create a bank account first");
		} else if (res == ReturnCodes.REGISTERED) {
			System.out.println("yeah I'm registered");
		}
		
		try {
			UnicastRemoteObject.unexportObject(callbacks, true);
		} catch (NoSuchObjectException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
