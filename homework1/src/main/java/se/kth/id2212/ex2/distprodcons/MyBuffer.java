package se.kth.id2212.ex2.distprodcons;

import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.RemoteException;
import java.rmi.registry.LocateRegistry;
import java.rmi.server.UnicastRemoteObject;
import java.util.LinkedList;

@SuppressWarnings("serial")
public class MyBuffer extends UnicastRemoteObject implements RemoteBuffer {
    private static final int REGISTRY_PORT_NUMBER = 1099;
    LinkedList<Integer> list = new LinkedList<>();

    public MyBuffer() throws RemoteException, MalformedURLException {
        super();
    }

    @Override
    public synchronized void put(Integer i) throws RemoteException {
        list.addLast(i);
        notifyAll();
    }

    @Override
    public synchronized Integer get() throws RemoteException {
        while (list.size() == 0) {
            try {
                wait();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return list.removeFirst();
    }

    public static void main(String[] args) {
        try {
            
            try {
                LocateRegistry.getRegistry(REGISTRY_PORT_NUMBER).list();
            } catch (RemoteException e) {
                LocateRegistry.createRegistry(REGISTRY_PORT_NUMBER);
            }
            
            Naming.rebind("rmi://localhost/buffer", new MyBuffer());
        
        } catch (RemoteException | MalformedURLException re) {
            System.out.println(re);
            System.exit(1);
        }
    }
}
