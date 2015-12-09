package gr.kzps.id2212.project.agentserver.overlay;

import java.net.InetAddress;

public class PeerAgent {
	private final InetAddress address;
	private final Integer port;
	
	public PeerAgent(InetAddress address, Integer port) {
		this.address = address;
		this.port = port;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public Integer getPort() {
		return port;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PeerAgent) {
			PeerAgent otherPeer = (PeerAgent) other;
			if(otherPeer.getAddress().equals(address) &&
					otherPeer.getPort().equals(port)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return address.hashCode() + port.hashCode();
	}
	
	@Override
	public String toString() {
		return "Address: " + address.toString() + " Port: " + port;
	}
}
