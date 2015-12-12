package gr.kzps.id2212.project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/*
 * Just for TESTING purposes
 */
public class AgentClientTesting {
	private static final Logger LOG = LogManager.getLogger(AgentClientTesting.class);
	private static Socket socket;
	
	public static void main(String[] args) {
		try {
			InetAddress server = InetAddress.getByName("localhost");
			Integer port = 6060;
			
			Agent agent = new AgentImpl(InetAddress.getByName("localhost"), 5050);

			LOG.debug("Agent created and sending to server");
			socket = new Socket(server, port);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			
			outStream.writeObject(agent);
			
			outStream.flush();
			if (outStream != null)
				outStream.close();
			if (socket != null)
				socket.close();
			
			ServerSocket sSocket = new ServerSocket(5050);
			Socket cSocket = sSocket.accept();
			
			ObjectInputStream inStream = new ObjectInputStream(cSocket.getInputStream());
			Agent comingAgent = (Agent) inStream.readObject();
			
			if (inStream != null)
				inStream.close();
			if (cSocket != null)
				cSocket.close();
			if (sSocket != null)
				sSocket.close();
			
			LOG.debug(comingAgent.getResult());
		} catch (IOException | ClassNotFoundException ex) {
			ex.printStackTrace();
		}

	}

}