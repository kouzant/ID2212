package gr.kzps.id2212.marketplace.server;

import gr.kzps.id2212.marketplace.server.database.entities.ItemEntity;

/**
 * Store also the owner of the item
 */
public class ExtendedItem extends BaseItem {
	
	private static final long serialVersionUID = -5542452786544546333L;
	
	private final MarketUsers owner;
        
        	
	public ExtendedItem(String name, float price, int quantity, MarketUsers owner) {
		super(name, price, quantity);
		this.owner = owner;
	}

    ExtendedItem(ItemEntity itemFromDb, MarketUsers owner) {
        super(itemFromDb.getName(), itemFromDb.getPrice(), itemFromDb.getQuantity());
        this.owner = owner;
    }

	public MarketUsers getOwner() {
		return owner;
	}
        
	@Override
	public String toString() {
		return super.toString() + " Owner: " + owner;
	}
}
