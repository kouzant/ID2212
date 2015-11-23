package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

/**
 * Base command that all the other commands extend
 */
public class BaseCommandImpl implements BaseCommand {
	private final Client client;
	
	public BaseCommandImpl(Client client) {
		this.client = client;
	}
	
	public Client getClient() {
		return client;
	}
}
