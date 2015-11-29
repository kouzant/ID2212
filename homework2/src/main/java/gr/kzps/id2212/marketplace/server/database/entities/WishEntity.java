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
@Inheritance(strategy = InheritanceType.JOINED)
@Table(name = "T_WISH")
public class WishEntity {

    @Id
    @GeneratedValue
    @Column(name = "WISH_ID")
    private Long id;
    @Column(name = "ITEM_NAME")
    private String name;
    @Column(name = "ITEM_PRICE")
    private float price;
    @Column(name = "WISH_FULFILLED")
    private Boolean wishFulfilled;

    @OneToOne
    @JoinColumn(name = "USER_ASSOCIATION")
    private ClientEntity clientEntity;

    public WishEntity() {

    }

    public WishEntity(String name, float price, ClientEntity clientEntity) {
        this.name = name;
        this.price = price;
        this.clientEntity = clientEntity;
        this.wishFulfilled = false;
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

    public void setWishFulfilled(Boolean wishFulfilled) {
        this.wishFulfilled = wishFulfilled;
    }

    public Boolean getWishFulfilled() {
        return wishFulfilled;
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

    @Override
    public String toString() {
        return "Item: " + name + "  price " + price;
    }
}
