package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

/**
 * Base command that all the other commands extend
 */
public class BaseCommand {
	private final Client client;
	
	public BaseCommand(Client client) {
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
}
