package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Callbacks;
import gr.kzps.id2212.marketplace.client.Client;

public class RegisterMarketplace extends BaseCommand {
	private final Callbacks callbacks;
	
	public RegisterMarketplace(Client client, Callbacks callbacks) {
		super(client);
		
		this.callbacks = callbacks;
	}

	public Callbacks getCallbacks() {
		return callbacks;
	}
}
