package gr.kzps.id2212.marketplace.client;

import gr.kzps.id2212.marketplace.server.MarketServer;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Bank;

public class ClientExecEnv {

	private static final Logger LOG = LogManager.getLogger(ClientExecEnv.class);
	
	private static Bank bank;
	private static MarketServer market;
	
	public static void main(String[] args) {
		
		try {
			LocateRegistry.getRegistry();
			// Bank Remote objects. RMI naming should remain Nordea...
			bank = (Bank) Naming.lookup("Nordea");
			// Marketplace Remote Objects. The same as above...
			market = (MarketServer) Naming.lookup("marketplace");
			
			new ClientConsole(bank, market).console();

		} catch (RemoteException ex) {
			LOG.error("Could not locate registry");
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (NotBoundException e) {
			e.printStackTrace();
		}
		
	}

}
