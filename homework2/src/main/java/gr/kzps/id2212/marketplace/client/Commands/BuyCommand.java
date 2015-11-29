package gr.kzps.id2212.marketplace.client.Commands;

import gr.kzps.id2212.marketplace.client.Client;

public class BuyCommand extends BaseCommandImpl {
	private final String itemName;
        private int quantity;
	
	public BuyCommand(Client client, String itemName, int quantity) {
		super(client);
		this.itemName = itemName;
                this.quantity = quantity;
	}
        
        public int getQuantity(){
            return quantity;
        }

	public String getItemName() {
		return itemName;
	}
}
