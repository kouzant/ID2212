package gr.kzps.id2212.project.client.query;

import java.io.Serializable;

import gr.kzps.id2212.project.client.agent.VisitedServer;

/**
 * Representation of a single result
 * @author Antonis Kouzoupis
 *
 */
public class Result implements Serializable {
	private static final long serialVersionUID = 5584882819897680615L;

	public final VisitedServer server;
	public final String fileName;
	
	/**
	 * @param server The server that the file was found
	 * @param fileName The name of the successful file
	 */
	public Result(VisitedServer server, String fileName) {
		this.server = server;
		this.fileName = fileName;
	}
	
	public VisitedServer getServer() {
		return server;
	}
	
	public String getFileName() {
		return fileName;
	}
	
	@Override
	public String toString() {
		return "Server: " + server + " File: " + fileName;
	}
}
