package gr.kzps.id2212.marketplace.server;

import java.io.Serializable;

/**
 * Basic item representation
  */
public class BaseItem implements Serializable {
	
	private static final long serialVersionUID = 3890162552994568137L;
	
	private final String name;
	private final float price;
        private int quantity;
	
	public BaseItem(String name, float price, int quantity) {
		this.name = name;
		this.price = price;
                this.quantity = quantity;
	}

	public String getName() {
		return name;
	}

	public float getPrice() {
		return price;
	}
        
        public int getQuantity(){
            return quantity;
        }
        
        public void setQuantity(int quant){
            this.quantity = quant;
        }
        
	@Override
	public String toString() {
		return "Item: " + name + " Price: " + price + " ";
	}
}
