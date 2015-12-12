package gr.kzps.id2212.project.client;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
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
			
			Agent agent = new AgentImpl();

			LOG.debug("Agent created and sending to server");
			socket = new Socket(server, port);
			ObjectOutputStream outStream = new ObjectOutputStream(socket.getOutputStream());
			
			outStream.writeObject(agent);
			
			outStream.flush();
			if (outStream != null)
				outStream.close();
			if (socket != null)
				socket.close();
		} catch (IOException ex) {
			ex.printStackTrace();
		}

	}

}
