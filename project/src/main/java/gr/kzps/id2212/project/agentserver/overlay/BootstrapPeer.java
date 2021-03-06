package gr.kzps.id2212.project.agentserver.overlay;

import java.io.Serializable;
import java.net.InetAddress;

/**
 * A bootstrap peer representation
 * @author Antonis Kouzoupis
 *
 */
public class BootstrapPeer implements Serializable {
	private static final long serialVersionUID = -4740911081188576606L;
	private final InetAddress address;
	private final Integer basePort;
	
	/**
	 * @param address IP address of the bootstrap node
	 * @param Running port of the Base service of the bootstrap node
	 */
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
	public String toString() {
		return "Address: " + address.toString() + " Base Port: " + basePort;
	}
}
