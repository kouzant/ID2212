package se.kth.id2212.ex2.chat;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.ArrayList;
import java.util.List;

@SuppressWarnings("serial")
public class MyServer extends UnicastRemoteObject implements ServerInterface {
    private static final int REGISTRY_PORT_NUMBER = 1099;
    private List<ClientInterface> clientTable = new ArrayList<>();

    public MyServer() throws RemoteException, MalformedURLException {
        super();
    }

    @Override
    public List<ClientInterface> getClients() {
        return (clientTable);
    }

    @Override
    public void registerClient(ClientInterface client) throws RemoteException {
        if (clientTable.contains(client)) {
            throw new RemoteException("client already registered");
        }
        clientTable.add(client);
    }

    @Override
    public void unregisterClient(ClientInterface client) throws RemoteException {
        if (!clientTable.contains(client)) {
            throw new RemoteException("client not registered");
        }
        clientTable.remove(client);
    }

    @Override
    public void broadcastMsg(String msg) throws RemoteException {
        for (ClientInterface client : clientTable) {
            try {
                client.receiveMsg(msg);
            } catch (RemoteException re) {
                re.printStackTrace();
            }
        }
    }

    public static void main(String[] args) {
        try {
            
            try {
                LocateRegistry.getRegistry(REGISTRY_PORT_NUMBER).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(REGISTRY_PORT_NUMBER);
            }
            Naming.rebind("rmi://localhost/chat", new MyServer());
            
        } catch (RemoteException | MalformedURLException re) {
            System.out.println(re);
            System.exit(1);
        }
    }
}
