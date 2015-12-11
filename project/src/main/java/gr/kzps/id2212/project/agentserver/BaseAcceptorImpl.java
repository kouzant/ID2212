package gr.kzps.id2212.project.agentserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;
import gr.kzps.id2212.project.agentserver.overlay.PeerNotFound;
import gr.kzps.id2212.project.agentserver.overlay.PeerStorage;
import gr.kzps.id2212.project.messages.BaseMessage;
import gr.kzps.id2212.project.messages.HelloMessage;
import gr.kzps.id2212.project.messages.GenericMessage;
import gr.kzps.id2212.project.messages.SampleExchange;

public class BaseAcceptorImpl extends Acceptor {
	private final Logger LOG = LogManager.getLogger(BaseAcceptorImpl.class);

	private PeerStorage peerStorage;
	private List<PeerAgent> sample;

	public BaseAcceptorImpl(Socket cSocket, PeerStorage peerStorage) {
		super(cSocket);
		this.peerStorage = peerStorage;
	}

	@Override
	public void run() {
		try {
			inStream = new ObjectInputStream(cSocket.getInputStream());
			outStream = new ObjectOutputStream(cSocket.getOutputStream());

			GenericMessage request = (GenericMessage) inStream.readObject();

			GenericMessage reply = request.execute(peerStorage);
			
			if (reply != null) {
				outStream.writeObject(reply);
			}

			outStream.flush();

			if (outStream != null)
				outStream.close();

			if (inStream != null)
				inStream.close();

			LOG.debug("Closing client socket");
			if (cSocket != null)
				cSocket.close();
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
			ex.printStackTrace();
		} catch (ClassNotFoundException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
