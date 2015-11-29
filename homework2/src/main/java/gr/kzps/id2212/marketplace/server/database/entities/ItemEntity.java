package gr.kzps.id2212.marketplace.server.database.entities;


import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Inheritance(strategy=InheritanceType.JOINED)
@Table(name = "T_ITEM")
public class ItemEntity {
	@Id
	@GeneratedValue
	@Column(name = "ITEM_ID")
	private Long id;
	@Column(name = "ITEM_NAME", unique=true)
	private String name;
	@Column(name = "ITEM_PRICE")
	private float price;
        @Column(name = "ITEM_QUANTITY")
	private int quantity;
        
        @OneToOne
        @JoinColumn(name="USER_ASSOCIATION")
        private ClientEntity clientEntity;

        public ItemEntity(){
            
        }
	
	public ItemEntity(String name, float price, int quantity, ClientEntity clientEntity) {
		this.name = name;
		this.price = price;
		this.quantity = quantity;
                this.clientEntity = clientEntity;
	}
	
	public Long getId() {
		return id;
	}
        
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
        
        public ClientEntity getClientEntity() {
		return clientEntity;
	}
	public void setClientEntity(ClientEntity clientEntity) {
		this.clientEntity = clientEntity;
	}
        
	public float getPrice() {
		return price;
	}
	public void setPrice(float price) {
		this.price = price;
	}
        
        public int getQuantity() {
		return quantity;
	}
	public void setQuantity(int quantity) {
		this.quantity = quantity;
	}
	
	@Override
	public String toString() {
		return "Item: " + name + "  price " + price + "  quantity: " + quantity;
	}
}
