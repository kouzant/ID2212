package gr.kzps.id2212.project.client.agent;

import java.io.Serializable;

public class VisitedServer implements Serializable {
	private static final long serialVersionUID = -6616923692716348756L;
	private final String server;
	private final Integer port;
	
	public VisitedServer(String server, Integer port) {
		this.server = server;
		this.port = port;
	}

	public String getServer() {
		return server;
	}

	public Integer getPort() {
		return port;
	}
	
	@Override
	public String toString() {
		return server + ":" + port;
	}
}
