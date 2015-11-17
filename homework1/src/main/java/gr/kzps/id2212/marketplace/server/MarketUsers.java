package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;

public class MarketUsers {
	private final Client client;
	private final Callbacks callbacks;
	
	public MarketUsers(Client client, Callbacks callbacks) {
		this.client = client;
		this.callbacks = callbacks;
	}
	
	public Client getClient() {
		return client;
	}
	
	public Callbacks getCallbacks() {
		return callbacks;
	}
}
