package gr.kzps.id2212.project.client;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import gr.kzps.id2212.project.client.agent.Agent;
import gr.kzps.id2212.project.client.exceptions.AgentNotFound;

public class AgentAcceptor implements Runnable {
	private final Logger LOG = LogManager.getLogger(AgentAcceptor.class);
	private final AgentDB db;
	private final Socket cSocket;
	private ObjectInputStream inStream;
	private Agent agent;
	
	public AgentAcceptor(Socket cSocket, AgentDB db) {
		this.db = db;
		this.cSocket = cSocket;
	}
	
	@Override
	public void run() {
		try {
			inStream = new ObjectInputStream(cSocket.getInputStream());
			agent = (Agent) inStream.readObject();
			
			AgentItem item = db.get(agent.getId().toString());
			item.setResultSet(agent.getResultSet());
			item.setVisitedServers(agent.getVisitedServers());
			item.setStatus(AgentStatus.FINISHED);
			
			ExportResults exporter = new ExportResults(item);
			
			System.out.print(exporter.export());
		} catch (IOException | ClassNotFoundException | AgentNotFound ex) {
			LOG.error(ex.getMessage());
			ex.printStackTrace();
		} finally {
			try {
				if (inStream != null)
					inStream.close();
				if (cSocket != null)
					cSocket.close();
			} catch (IOException ex) {
				LOG.error(ex.getMessage());
				ex.printStackTrace();
			}
		}
		
	}

}
