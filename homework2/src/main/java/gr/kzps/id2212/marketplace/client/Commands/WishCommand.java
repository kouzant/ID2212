package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

public class WishCommand extends BaseCommandImpl {
	private final String itemName;
	private final float price;
	
	public WishCommand(Client client, String itemName, float price) {
		super(client);
		this.itemName = itemName;
		this.price = price;
	}

	public String getItemName() {
		return itemName;
	}
	
	public float getPrice() {
		return price;
	}
}
