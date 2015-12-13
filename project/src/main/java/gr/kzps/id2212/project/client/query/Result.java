package gr.kzps.id2212.project.client.query;

import java.io.Serializable;

import gr.kzps.id2212.project.agentserver.overlay.PeerAgent;

public class Result implements Serializable {
	private static final long serialVersionUID = 5584882819897680615L;

	public final PeerAgent server;
	public final String fileName;
	
	public Result(PeerAgent server, String fileName) {
		this.server = server;
		this.fileName = fileName;
	}
	
	public PeerAgent getServer() {
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
