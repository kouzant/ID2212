package gr.kzps.id2212.project.agentserver;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.Arrays;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.messages.BaseMessage;

public class BaseAcceptorImpl extends Acceptor {
	private final Logger LOG = LogManager.getLogger(BaseAcceptorImpl.class);

	public BaseAcceptorImpl(Socket cSocket) {
		super(cSocket);
	}

	@Override
	public void run() {
		try {
			inStream = new ObjectInputStream(cSocket.getInputStream());
			outStream = new ObjectOutputStream(cSocket.getOutputStream());

			Object request = inStream.readObject();

			// TODO implement sample update messages
			// TODO Handle HELLO messages
			if (request instanceof BaseMessage) {
				BaseMessage msg = (BaseMessage) request;
				msg.setAgentPort(Cache.getInstance().getAgentPort());

				outStream.writeObject(msg);
			}
			outStream.flush();
			outStream.close();
			inStream.close();
			LOG.debug("Closing client socket");
			cSocket.close();
		} catch (IOException ex) {
			LOG.error(ex.getMessage());
		} catch (ClassNotFoundException ex) {
			LOG.error(ex.getMessage());
		}
	}
}
