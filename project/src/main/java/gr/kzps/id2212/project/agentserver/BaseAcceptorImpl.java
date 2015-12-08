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
			
			BaseMessage request = (BaseMessage) inStream.readObject();
			request.setAgentPort(Cache.getInstance().getAgentPort());
			
			outStream.writeObject(request);
			
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
