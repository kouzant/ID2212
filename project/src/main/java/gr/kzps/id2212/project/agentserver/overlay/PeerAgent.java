package gr.kzps.id2212.project.agentserver.overlay;

import java.net.InetAddress;

/**
 * An agent server representation
 * @author Antonis Kouzoupis
 *
 */
public class PeerAgent extends BootstrapPeer {

	private static final long serialVersionUID = -5366734414093752165L;
	
	private final Integer servicePort;
	
	/**
	 * @param address IP address of the agent server
	 * @param basePort Running port of the Base server of the agent server
	 * @param servicePort Running port of the Agent service of the agent server
	 */
	public PeerAgent(InetAddress address, Integer basePort, Integer servicePort) {
		super(address, basePort);
		this.servicePort = servicePort;
	}
	
	public Integer getServicePort() {
		return servicePort;
	}
	
	@Override
	public boolean equals(Object other) {
		if (other instanceof PeerAgent) {
			PeerAgent otherPeer = (PeerAgent) other;
			if(otherPeer.getAddress().equals(super.getAddress())
				&& otherPeer.getBasePort().equals(super.getBasePort())
				&& otherPeer.getServicePort().equals(servicePort)) {
				return true;
			}
		}
		
		return false;
	}
	
	@Override
	public int hashCode() {
		return super.getAddress().hashCode() * super.getBasePort().hashCode()
				+ servicePort.hashCode();
	}
	
	@Override
	public String toString() {
		return super.toString() + " Service Port: " + servicePort;
	}
}
