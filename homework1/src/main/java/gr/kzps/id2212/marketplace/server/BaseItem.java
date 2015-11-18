package gr.kzps.id2212.marketplace.server;

import java.io.Serializable;

public class BaseItem implements Serializable {
	
	private static final long serialVersionUID = 3890162552994568137L;
	
	private final String name;
	private final float price;
	
	public BaseItem(String name, float price) {
		this.name = name;
		this.price = price;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}
	
	@Override
	public String toString() {
		return "Item: " + name + " Price: " + price + " ";
	}
}
