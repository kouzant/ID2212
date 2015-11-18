package gr.kzps.id2212.marketplace.server;

public class ExtendedItem extends BaseItem {
	
	private static final long serialVersionUID = -5542452786544546333L;
	
	private final MarketUsers owner;
	
	public ExtendedItem(String name, float price, MarketUsers owner) {
		super(name, price);
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
