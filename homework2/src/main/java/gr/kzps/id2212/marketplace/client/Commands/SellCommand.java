package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

public class SellCommand extends BaseCommandImpl {
	private final String itemName;
	private final float price;
        private final int quantity;
	
	public SellCommand(Client client, String itemName, float price, int quantity) {
		super(client);
		this.itemName = itemName;
		this.price = price;
		this.quantity = quantity;
	}

	public String getItemName() {
		return itemName;
	}
        
        public int getQuantity() {
		return quantity;
	}

	public float getPrice() {
		return price;
	}
}
