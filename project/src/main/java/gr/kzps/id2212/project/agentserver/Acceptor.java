package gr.kzps.id2212.project.agentserver;

import java.io.ObjectInputStream;
import java.io.ObjectOutput;
import java.net.Socket;

public abstract class Acceptor implements Runnable {
	protected Socket cSocket;
	protected ObjectInputStream inStream;
	protected ObjectOutput outStream;
	
	public Acceptor(Socket cSocket) {
		this.cSocket = cSocket;
	}
	
	public abstract void run();
	
}
