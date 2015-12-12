package gr.kzps.id2212.project.agentserver.overlay;

import java.io.Serializable;
import java.net.InetAddress;

public class BootstrapPeer implements Serializable {
	private static final long serialVersionUID = -4740911081188576606L;
	private final InetAddress address;
	private final Integer basePort;
	
	public BootstrapPeer(InetAddress address, Integer basePort) {
		this.address = address;
		this.basePort = basePort;
	}
	
	public InetAddress getAddress() {
		return address;
	}
	
	public Integer getBasePort() {
		return basePort;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof BootstrapPeer) {
			BootstrapPeer otherPeer = (BootstrapPeer) other;
			if(otherPeer.getAddress().equals(address)
				&& otherPeer.getBasePort().equals(basePort)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return address.hashCode() + basePort.hashCode();
	}
	
	@Override
	public String toString() {
		return "Address: " + address.toString() + " Base Port: " + basePort;
	}
}
