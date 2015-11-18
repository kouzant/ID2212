package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

public class BuyCommand extends BaseCommand {
	private final String itemName;
	
	public BuyCommand(Client client, String itemName) {
		super(client);
		this.itemName = itemName;
	}

	public String getItemName() {
		return itemName;
	}
}
