package gr.kzps.id2212.marketplace.server;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import se.kth.id2212.ex2.bankrmi.Bank;

public class ServerExecEnv {
	private static final Logger LOG = LogManager.getLogger(ServerExecEnv.class);

	private static final String RMI_HOST = "localhost";
	private static final Integer RMI_PORT = 1099;

	public ServerExecEnv(String marketName) {

		// Register Marketplace to rmiregistry
		try {
			
			try {
				LocateRegistry.getRegistry(RMI_HOST, RMI_PORT);
			} catch (RemoteException e) {
				LOG.error("Could not locate RMI registry, creating new");
				LocateRegistry.createRegistry(RMI_PORT);
			}
			Bank bank = (Bank) Naming.lookup("Nordea");
			MarketServer market = new MarketServerImpl("e-Mall", bank);
			
			Naming.rebind(marketName, market);
			LOG.info("Marketplace is up and running!");
			
		} catch (RemoteException ex) {
			LOG.error("Could not create RMI registry");
			ex.printStackTrace();
		} catch (MalformedURLException ex) {
			ex.printStackTrace();
		} catch (NotBoundException ex) {
			ex.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String marketName = args.length > 0 ? args[0] : "marketplace";
		System.out.println(marketName);
		new ServerExecEnv(marketName);
	}

}
